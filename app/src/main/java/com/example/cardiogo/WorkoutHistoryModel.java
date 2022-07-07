package com.example.cardiogo;

public class WorkoutHistoryModel {
    String aveHeartRate, aveSPo2;
    String dateToday, workoutStatus;

    public WorkoutHistoryModel(String aveHeartRate,String aveSPo2, String dateToday, String workoutStatus) {
        this.aveHeartRate = aveHeartRate;
        this.aveSPo2 = aveSPo2;
        this.dateToday = dateToday;
        this.workoutStatus = workoutStatus;
    }

    public String getAveHeartRate() {
        return aveHeartRate;
    }

    public String getAveSPo2() {
        return aveSPo2;
    }

    public String getDateToday() {
        return dateToday;
    }

    public String getWorkoutStatus() {
        return workoutStatus;
    }
}
