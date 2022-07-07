package com.example.cardiogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CheckHeartRate extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    TextView mTextField;
    ProgressBar timerProgress, heartRateProgress;
    LinearLayout LinearLayoutOver, LinearLayoutUnder;
    int passNo = StartWorkoutActivity.eCounter;
    DatabaseHelper db;

    String movingDate;
    String dateToday;
    String setDate;
    String[] workoutDate = new String[0];

    int streak;
    final int maximumHR = 220;

    String sessionUsername;

    private OutputAnalyzer analyzer;

    public static long startTime = 0;
    private static final long START_TIME_IN_MILLIS = 60000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private final int REQUEST_CODE_CAMERA = 0;
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;
    public static final int MESSAGE_UPDATE_TIMER = 4;
    public static final int MESSAGE_DO_NOT_MOVE = 5;
    public static final int MESSAGE_FINAL_READING = 6;
    public static final int MESSAGE_SPO2 = 7;


    public TextureView cameraTextureView;

    int heartRateHolder = 0;
    String measuringPrompt = "";
    final String WORKOUT_STOPPED = "STOPPED";

    int SPO2Holder = 0;

    //Checks if the acquirement of HR is done.
    String statusChecker = "";

    String doneMeasuringHRPrompt = "MEASURING IS DONE.";


    private boolean justShared = false;

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what ==  MESSAGE_UPDATE_REALTIME) {
                ((TextView) findViewById(R.id.heartRateTextView)).setText(msg.obj.toString());
//                heartRateHolder = Integer.parseInt(msg.obj.toString());
            }

            if (msg.what == MESSAGE_UPDATE_FINAL) {
                statusChecker = msg.obj.toString();
                LinearLayoutUnder.setVisibility(View.GONE);
                heartRateProgress.setVisibility(View.GONE);
                LinearLayoutOver.setVisibility(View.VISIBLE);


            }

            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());

                ((TextView) findViewById(R.id.heartRateTextView)).setText("CAMERA NOT FOUND");
                analyzer.stop();
            }

            if(msg.what == MESSAGE_UPDATE_TIMER){
                int timer = Integer.parseInt(msg.obj.toString());
                heartRateProgress.setProgress(timer);

            }

            if(msg.what == MESSAGE_DO_NOT_MOVE){
                measuringPrompt = msg.obj.toString();
                System.out.println("MPP" + measuringPrompt);
                if(measuringPrompt.equals(doneMeasuringHRPrompt)){
                    System.out.println("MPP" + measuringPrompt);
                    //int resIDGreen = getResources().getIdentifier("gStart", "colors", getPackageName());
                    ((TextView) findViewById(R.id.promptTextview)).setTextColor(getResources().getColor(R.color.gStart));
                    ((TextView) findViewById(R.id.promptTextview)).setText(measuringPrompt);
                }else{
                    System.out.println("MPP" + measuringPrompt);
                    ((TextView) findViewById(R.id.promptTextview)).setText(measuringPrompt);
                }

            }

            if(msg.what == MESSAGE_UPDATE_TIMER){
                int timer = Integer.parseInt(msg.obj.toString());
                heartRateProgress.setProgress(timer);

            }

            if(msg.what == MESSAGE_FINAL_READING){
                ((TextView) findViewById(R.id.bpmOver)).setText(msg.obj.toString());
                heartRateHolder = Integer.parseInt(msg.obj.toString());

            }


            if(msg.what == MESSAGE_SPO2){
                SPO2Holder = Integer.parseInt(msg.obj.toString());
                ((TextView) findViewById(R.id.spo2Over)).setText(msg.obj.toString());

            }





        }
    };

    private final CameraService cameraService = new CameraService(this, mainHandler);

    @Override
    protected void onResume() {
        super.onResume();

        if(statusChecker.equals("DONE")){
            return;
        }
        checkHeartRate();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop();
        if (analyzer != null) analyzer.stop();
        analyzer = new OutputAnalyzer(this, mainHandler);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_heart_rate);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);

        cameraTextureView = findViewById(R.id.textureView2);

        mTextField = findViewById(R.id.mTextField);
        timerProgress = findViewById(R.id.timerprogress);
        heartRateProgress = findViewById(R.id.heartRateProgress);
        LinearLayoutOver = findViewById(R.id.LinearLayoutOver);
        LinearLayoutUnder = findViewById(R.id.LinearLayoutUnder);
        db = new DatabaseHelper(this);


        SessionManagement sessionManagement = new SessionManagement(CheckHeartRate.this);
        sessionUsername = sessionManagement.getSession();

        Cursor cursor = db.getStreak(sessionUsername);
        String checker = cursor.getString(0);

        streak = Integer.parseInt(checker);
        System.out.println("counter = " + StartWorkoutActivity.eCounter);

        new CountDownTimer(mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateTimerOutput();
            }
            public void onFinish() {
                if(passNo==5){
                    if(SPO2Holder < 90){
                        String dateToday = getDateToday();
                        Boolean createUser =db.insertWorkoutData(sessionUsername, 0, 0, dateToday, WORKOUT_STOPPED);
                        if(createUser){
                            Toast.makeText(CheckHeartRate.this, "INPUT SUCCESS", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CheckHeartRate.this, "INPUT FAILED", Toast.LENGTH_SHORT).show();
                        }
                        CheckHeartRate.this.finish();
                        Intent intent = new Intent(CheckHeartRate.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        System.out.println("Should stop here");
                        StartWorkoutActivity.heartRateHolder[StartWorkoutActivity.eCounter - 1] = heartRateHolder;
                        StartWorkoutActivity.SPO2Holder[StartWorkoutActivity.eCounter - 1] = SPO2Holder;
                        db = new DatabaseHelper(CheckHeartRate.this);
                        getLatestDate();
                        db.update_exp(sessionUsername);
                        StartWorkoutActivity.getInstance().finish();
                        CheckHeartRate.this.finish();
                        Intent intent = new Intent(CheckHeartRate.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    if(SPO2Holder < 90){
                        String dateToday = getDateToday();
                        Boolean createUser =db.insertWorkoutData(sessionUsername, 0, 0, dateToday, WORKOUT_STOPPED);
                        if(createUser){
                            Toast.makeText(CheckHeartRate.this, "INPUT SUCCESS", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CheckHeartRate.this, "INPUT FAILED", Toast.LENGTH_SHORT).show();
                        }
                        CheckHeartRate.this.finish();
                        Intent intent = new Intent(CheckHeartRate.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        StartWorkoutActivity.heartRateHolder[StartWorkoutActivity.eCounter - 1] = heartRateHolder;
                        StartWorkoutActivity.SPO2Holder[StartWorkoutActivity.eCounter - 1] = SPO2Holder;
                        CheckHeartRate.this.finish();
                        Intent intent = new Intent(CheckHeartRate.this, DoWorkoutActivity.class);
                        intent.putExtra("wCounter", StartWorkoutActivity.eCounter);
                        startActivity(intent);
                    }
                }
            }
        }.start();


    }

    public void updateTimerOutput(){
        int seconds = (int) mTimeLeftInMillis /1000 %60;
        String timeLeftFormat = String.format(Locale.getDefault(),"%02d",seconds);
        timerProgress.setProgress(seconds);
        if(seconds==5){
            //mTextField.setAnimation(shake);
            mTextField.setTextColor(ContextCompat.getColor(this,R.color.red));
            //mTextField.startAnimation(shake);

        }
        mTextField.setText(timeLeftFormat);
    }

    public void getLatestDate(){
        db = new DatabaseHelper(this);
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.workoutHistory();

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }

        if(test_i < 1){
            movingDate = getDateToday();
            insertWorkoutData();
            return;
        }

        System.out.println("entries" + test_i);
        System.out.println();

        Cursor cursor1 = db.workoutHistory();
        System.out.println("CHECK LAMANG");
        while (i < test_i) {
            System.out.println("CHECK ULIT");
            workoutDate = Arrays.copyOf(workoutDate, workoutDate.length +1);
            workoutDate[i] = cursor1.getString(0);
            System.out.println(workoutDate[i]);
            cursor1.moveToNext();
            i++;
        }

        movingDate = workoutDate[workoutDate.length-1];
        movingDate = addDate(movingDate);

        String dateToday = getDateToday();
        System.out.println("DDATETODAY " + dateToday);
        insertWorkoutData();
        return;


    }

    public String addDate(String date){

        Calendar local = Calendar.getInstance();

        String[] words = date.split("/");
        int[] intDateHolder = new int[words.length];

        for(int i = 0; i< words.length;i++){
            intDateHolder[i] = Integer.parseInt(words[i]);
            System.out.println(intDateHolder[i]);
        }

        local.set(Calendar.MONTH, (intDateHolder[0]+11));
        local.set(Calendar.DAY_OF_MONTH, intDateHolder[1]);
        local.set(Calendar.YEAR, intDateHolder[2]-1);

        Log.d("addDate", local.get(Calendar.MONTH) + "/" + local.get(Calendar.DAY_OF_MONTH) + "/" + local.get(Calendar.YEAR));

        local.add(Calendar.DAY_OF_MONTH, 1);

        Log.d("addDatee", local.get(Calendar.MONTH) + "/" + local.get(Calendar.DAY_OF_MONTH) + "/" + local.get(Calendar.YEAR));

        String returnDate = (local.get(Calendar.MONTH)+1) + "/" + local.get(Calendar.DAY_OF_MONTH) + "/" + local.get(Calendar.YEAR);


        return returnDate;
    }

    String getDateToday(){

        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");
        calendar.setTimeZone(timeZone);
        int day,month,year;

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        dateToday = month +1 + "/" + day + "/" + year;

        System.out.println("date today is " + dateToday);

        return dateToday;
    }

    public void insertWorkoutData(){

        db = new DatabaseHelper(this);
        int userMaxHR = 0;
        int aveHR = 0;
        int aveSPO2 = 0;
//        String dateToday = "01/13/2022";
        String dateToday = getDateToday();
//        String dateToday = movingDate;
        String workoutStatus = "";

        for (int i = 0; i<StartWorkoutActivity.heartRateHolder.length;i++){
            aveHR += StartWorkoutActivity.heartRateHolder[i];
            aveSPO2 += StartWorkoutActivity.SPO2Holder[i];
        }

        aveHR = aveHR / 5;
        aveSPO2 = aveSPO2 / 5;

        Cursor cursor = db.getAge(sessionUsername);
        int playerAge = cursor.getInt(0);

        userMaxHR = maximumHR - playerAge;

        if(aveHR < ((int) userMaxHR * 0.55)){
            workoutStatus = "FAILED";
        }else{
            workoutStatus = "SUCCESS";
        }

        System.out.println("WORKOUTSTATUS " + workoutStatus);
        System.out.println("STREAK " + streak);
        System.out.println("USER XP" + MainActivity.user_xp);

        if(workoutStatus.equals("SUCCESS")){
            if(streak == 0){
                System.out.println("USER XP if" + MainActivity.user_xp);
                MainActivity.user_xp+=(100);
                System.out.println("USER XP if" + MainActivity.user_xp);
            }else {
                System.out.println("USER XP else" + MainActivity.user_xp);
                MainActivity.user_xp += (100 + (streak * 1.3));
                System.out.println("USER XP else" + MainActivity.user_xp);
            }
        }


        Boolean createUser =db.insertWorkoutData(sessionUsername, aveHR, aveSPO2, dateToday, workoutStatus);
        if(createUser){
            Toast.makeText(CheckHeartRate.this, "INPUT SUCCESS", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CheckHeartRate.this, "INPUT FAILED", Toast.LENGTH_SHORT).show();
        }
    }

    void checkHeartRate(){

        analyzer = new OutputAnalyzer(this, mainHandler);


        TextureView cameraTextureView = findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        // justShared is set if one clicks the share button.
        if ((previewSurfaceTexture != null) && !justShared) {
            // this first appears when we close the application and switch back
            // - TextureView isn't quite ready at the first onResume.
            Surface previewSurface = new Surface(previewSurfaceTexture);

            // show warning when there is no flash
            if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Toast.makeText(getApplicationContext(), "WELCOME TO APP", Toast.LENGTH_SHORT).show();
            }

            cameraService.start(previewSurface);
            startTime = (int) System.currentTimeMillis();
            analyzer.measurePulse(cameraTextureView, cameraService);
        }

    }

    public void heartRateInfo(View view) {
        HeartRateInfoActivity heartRateInfoActivity = new HeartRateInfoActivity();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        heartRateInfoActivity.show(getSupportFragmentManager(),"FirstFragment");


    }
}