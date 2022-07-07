package com.example.cardiogo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

class OutputAnalyzer {
    private final Activity activity;


    //Arraylist
    public ArrayList<Double> RedAvgList = new ArrayList<Double>();
    public ArrayList<Double> BlueAvgList = new ArrayList<Double>();
    public int tickCounter = 0;


    Boolean lol;

    int endTime = 0;

    private MeasureStore store;


    private final int measurementInterval = 45;
    private int measurementLength = 25000; // ensure the number of data points is the power of two
    private final int clipLength = 4000;

    long startTime = 0;

    private int detectedValleys = 0;
    private int ticksPassed = 0;

    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Measurement<Integer>> subList = new CopyOnWriteArrayList<>();

    Integer referenceValue;

    private CountDownTimer timer;

    private final Handler mainHandler;

    private final String doNotMovePrompt= "MEASURING HR, DO NOT MOVE YOUR FINGER.";
    private final String placeFingerPrompt = "PLACE YOUR FINGER ON YOUR CAMERA.";
    private final String doneMeasuringHRPrompt= "MEASURING IS DONE.";


    double rMeasurementHolder = 0;
    double bMeasurementHolder = 0;

    double rMeasurementSum = 0;
    double bMeasurementSum = 0;

    double stdB = 0;
    double stdR = 0;

    int finalSPo2 = 0;



    OutputAnalyzer(Activity activity, Handler mainHandler) {
        this.activity = activity;
        this.mainHandler = mainHandler;
    }



    private boolean detectValley() {
        final int valleyDetectionWindowSize = 10; // FINAL 10?
        subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize) {
            return false;
        } else {
            referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;

            //Checks for 10 consecutive times to return true
            for (Measurement<Integer> measurement : subList) {
                System.out.println(measurement.measurement + " vs " + referenceValue);
                if (measurement.measurement < referenceValue) return false;
            }

//            // filter out consecutive measurements due to too high measurement rate

            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                    subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));//will always return true
        }
    }

    void measurePulse(TextureView textureView, CameraService cameraService) {

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.

        store = new MeasureStore();

        detectedValleys = 0;

        timer = new CountDownTimer(measurementLength, measurementInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                // skip the first measurements, which are broken by exposure metering
                if (clipLength > (++ticksPassed * measurementInterval)) {
                    sendMessage(CheckHeartRate.MESSAGE_UPDATE_TIMER, String.valueOf(millisUntilFinished / 1000));
                    return;
                }

                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurement = 0;
                    double SPo2measurement = 0;
                    int[] pixels = new int[pixelCount];

                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                        SPo2measurement += (pixels[pixelIndex]) & 0xff;
                    }

                    rMeasurementHolder = measurement / pixelCount;
                    bMeasurementHolder = SPo2measurement / pixelCount;

                    if (rMeasurementHolder < 160){
                        measurement = 0;
                        store.clearMeasurementsList(true);
                        valleys.clear();
                        subList.clear();
                        referenceValue = 0;
                        detectedValleys = 0;
                        ticksPassed = 0;
                        rMeasurementHolder = 0;
                        rMeasurementSum = 0;
                        bMeasurementHolder = 0;
                        bMeasurementSum = 0;
                        RedAvgList.clear();
                        BlueAvgList.clear();
                        tickCounter = 0;
                        sendMessage(CheckHeartRate.MESSAGE_UPDATE_REALTIME, "0");
                        sendMessage(CheckHeartRate.MESSAGE_DO_NOT_MOVE, placeFingerPrompt);
                        timer.cancel();
                        timer.start();
                        return;
                    }

                    System.out.println("MHr" + rMeasurementHolder);
                    System.out.println("MHb "+ bMeasurementHolder);

                    rMeasurementSum += rMeasurementHolder;
                    bMeasurementSum += bMeasurementHolder;

                    System.out.println("MSr" + rMeasurementSum);
                    System.out.println("MSb "+ bMeasurementSum);

                    RedAvgList.add(rMeasurementHolder);
                    BlueAvgList.add(bMeasurementHolder);

                    ++tickCounter;

                    System.out.println("TC " + tickCounter);
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit

                    store.add(measurement);

                    sendMessage(CheckHeartRate.MESSAGE_UPDATE_TIMER, String.valueOf(millisUntilFinished / 1000));

                    if (detectValley()) {
                        System.out.println("Try lang " + lol);
                        detectedValleys = detectedValleys + 1;
                        valleys.add(System.currentTimeMillis());
                        // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69

                        String currentValue = String.valueOf((int) ((valleys.size() == 1)
                                ? (60f * (detectedValleys) / (Math.max(1, (measurementLength - millisUntilFinished - clipLength) / 1000f)))
                                : (60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)))));


                        sendMessage(CheckHeartRate.MESSAGE_UPDATE_REALTIME, currentValue);
                        sendMessage(CheckHeartRate.MESSAGE_DO_NOT_MOVE, doNotMovePrompt);

                    }

                });
                thread.start();
            }

            @Override
            public void onFinish() {
                CopyOnWriteArrayList<Measurement<Float>> stdValues = store.getStdValues();

                Double Red[] =RedAvgList.toArray(new Double[RedAvgList.size()]);
                Double Blue[] = BlueAvgList.toArray(new Double[BlueAvgList.size()]);


                System.out.println("OFFSIZER " + RedAvgList.size());
                System.out.println("OFFSIZEB " + BlueAvgList.size());


                System.out.println("OFFREDSIZE " + Red.length);
                System.out.println("OFFBLUESIZE " + Blue.length);


                double meanR = rMeasurementSum / tickCounter;
                double meanB =bMeasurementSum / tickCounter;

                System.out.println("OFFMRSM " + rMeasurementSum);
                System.out.println("OFFMBSM " + bMeasurementSum);
                System.out.println("OFFMFINISH " + tickCounter);

                System.out.println("OFFMR " + meanR);
                System.out.println("OFFMB " + meanB);

                for (int i = 0; i < tickCounter - 1; i++){
                    Double bufferR = Red[i];

                    stdR = stdR+ ((bufferR - meanR) * (bufferR - meanR));

                    Double bufferB = Blue[i];

                    stdB = stdB + ((bufferB - meanB) * (bufferB - meanB));
                }

                System.out.println("OFFSTDR " + meanR);
                System.out.println("OFFSTDB " + meanB);

                double varR = Math.sqrt(stdR / (tickCounter - 1));
                double varB = Math.sqrt(stdB / (tickCounter - 1));

                System.out.println("VARB " + varB);
                System.out.println("VARR " + varR);

                System.out.println("OFFVARB " + varB);
                System.out.println("OFFVARR " + varR);

                double R = ((varR / meanR) / (varB / meanB));

                System.out.println("OFFRRR " + R);

                System.out.println("RRR " + R);

                double spo2 = 100 - 5 * (R);

                System.out.println("OFFSPO2" + spo2);

                finalSPo2 = (int) spo2;

                System.out.println("OFFFINALSPO2 " + finalSPo2);

                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods

                // If the camera only provided a static image, there are no valleys in the signal.
                // A camera not available error is shown, which is the most likely cause.
                if (valleys.size() == 0) {
                    mainHandler.sendMessage(Message.obtain(
                            mainHandler,
                            CheckHeartRate.MESSAGE_CAMERA_NOT_AVAILABLE,
                            "No valleys detected - there may be an issue when accessing the camera."));
                    return;
                }

                String currentValue = String.valueOf((int) (60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f))));


//                sendMessage(CheckHeartRate.MESSAGE_UPDATE_REALTIME, currentValue);
                sendMessage(CheckHeartRate.MESSAGE_UPDATE_FINAL, "DONE");
                sendMessage(CheckHeartRate.MESSAGE_DO_NOT_MOVE, doneMeasuringHRPrompt);
                sendMessage(CheckHeartRate.MESSAGE_FINAL_READING, currentValue);
                sendMessage(CheckHeartRate.MESSAGE_SPO2, finalSPo2);

                cameraService.stop();
            }
        };

        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    void sendMessage(int what, Object message) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }


}