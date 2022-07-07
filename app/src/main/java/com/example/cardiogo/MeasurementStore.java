package com.example.cardiogo;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class MeasureStore {
    private static final CopyOnWriteArrayList<Measurement<Integer>> measurements = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Measurement<Float>> stdValues = new CopyOnWriteArrayList<>();
    private int minimum = 2147483647;
    private int maximum = -2147483648;

    /**
     * The latest N measurements are always averaged in order to smooth the values before it is
     * analyzed.
     *
     * This value may need to be experimented with - it is better on the class level than putting it
     * into local scope
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int rollingAverageSize = 4;

    void add(int measurement) {
        Measurement<Integer> finalMeasurement = new Measurement<>(measurement);

        measurements.add(finalMeasurement);
        if (measurement < minimum) minimum = measurement;
        if (measurement > maximum) maximum = measurement;
    }

    void clearMeasurementsList(Boolean status){

        if(status == true){
            System.out.println("list deleted");
            System.out.println("MSSBeforemeasurements" + measurements.size());
            System.out.println("MSSBeforestdValues" + stdValues.size());
            measurements.clear();
            stdValues.clear();
            System.out.println("MSSAftermeasuremetns" + measurements.size());
            System.out.println("MSSAfterstdValues" + stdValues.size());
        }
    }

    CopyOnWriteArrayList<Measurement<Float>> getStdValues() {

        for (int i = 0; i < measurements.size(); i++) {
            int sum = 0;
            for (int rollingAverageCounter = 0; rollingAverageCounter < rollingAverageSize; rollingAverageCounter++) {
                sum += measurements.get(Math.max(0, i - rollingAverageCounter)).measurement;
            }


            Measurement<Float> stdValue =
                    new Measurement<>(((float)sum / rollingAverageSize - minimum ) / (maximum - minimum));
            stdValues.add(stdValue);
        }
        return stdValues;
    }

    @SuppressWarnings("SameParameterValue") // this parameter can be set at OutputAnalyzer
    CopyOnWriteArrayList<Measurement<Integer>> getLastStdValues(int count) {
        if (count < measurements.size()) {
            return  new CopyOnWriteArrayList<>(measurements.subList(measurements.size() - 1 - count, measurements.size() - 1)); // Sublist(from index, to index)
        } else {
            return measurements;
        }
    }

}