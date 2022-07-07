package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryGraphActivity extends AppCompatActivity {

    DatabaseHelper db;
    public String sessionUsername;

    BarChart barChart;
    TextView workoutDate1, workoutDate2, workoutDate3, workoutDate4, workoutDate5,
            highestStreakTextView, highestHeartRateTextView;
    int heartRateHolder[] = {0,0,0,0,0};
    String dateHolder[] = {"N/A","N/A","N/A","N/A","N/A"};

    int[] workoutHeartRate = new int[0];
    String[] workoutDate = new String[0];

    String highestStreak;
    int highestHeartRate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);
        SessionManagement sessionManagement = new SessionManagement(HistoryGraphActivity.this);
        sessionUsername = sessionManagement.getSession();

        barChart = findViewById(R.id.bargraph);
        workoutDate1 = findViewById(R.id.workoutDate1);
        workoutDate2 = findViewById(R.id.workoutDate2);
        workoutDate3 = findViewById(R.id.workoutDate3);
        workoutDate4 = findViewById(R.id.workoutDate4);
        workoutDate5 = findViewById(R.id.workoutDate5);

        getWorkoutData();

        List<BarEntry> entries = new ArrayList<>();
        System.out.println("PEKPEK");
        for(int i = 1, counter = 0; i <= 5; i++){

            System.out.println("THISSSS " + workoutHeartRate.length);
            System.out.println("THISSSS " + workoutDate.length);

            if(counter < workoutHeartRate.length){
                heartRateHolder[counter] = workoutHeartRate[workoutHeartRate.length-i];
                System.out.println("INSIDE " + heartRateHolder[counter]);
            }

            if(counter < workoutDate.length) {
                dateHolder[counter] = workoutDate[workoutDate.length-i];
            }
            counter++;
        }

        System.out.println("TANGINA0 " + heartRateHolder[0]);
        System.out.println("TANGINA1 " + heartRateHolder[1]);
        System.out.println("TANGINA2 " + heartRateHolder[2]);
        System.out.println("TANGINA3 " + heartRateHolder[3]);
        System.out.println("TANGINA4 " + heartRateHolder[4]);
        System.out.println("TANGINA5 " + heartRateHolder.length);

        for(int i = 0, counter = 1; i < 5; i++){
//            System.out.println("CHECKKK " + (heartRateHolder.length - counter) + " " + heartRateHolder[heartRateHolder.length - counter]);
//            System.out.println("PEKPEK " + heartRateHolder[heartRateHolder.length - counter]);
            entries.add(new BarEntry(counter,heartRateHolder[i]));
            counter++;
        }

        workoutDate1.setText(dateHolder[0]);
        workoutDate2.setText(dateHolder[1]);
        workoutDate3.setText(dateHolder[2]);
        workoutDate4.setText(dateHolder[3]);
        workoutDate5.setText(dateHolder[4]);

        getHighestStreak();
        getHighestHeartRate();

        BarDataSet set = new BarDataSet(entries, "Heart Rate");
        set.setColors(new int[] { R.color.red}, this);

        BarData data = new BarData(set);
        // sets colors for the dataset, resolution of the resource name to a "real" color is done internally
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        barChart.animateY(1000); // animate vertical 3000 milliseconds
    }

    public void getWorkoutData(){

        db = new DatabaseHelper(this);
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.userHistory(sessionUsername);

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        System.out.println("entries" + test_i);
        System.out.println();

        Cursor cursor1 = db.userHistory(sessionUsername);
        while (i < test_i) {
            workoutHeartRate = Arrays.copyOf(workoutHeartRate, workoutHeartRate.length +1);
            workoutHeartRate[i] = cursor1.getInt(2);

            workoutDate = Arrays.copyOf(workoutDate, workoutDate.length +1);
            workoutDate[i] = cursor1.getString(0);
            System.out.println(workoutHeartRate[i]);
            System.out.println(workoutDate[i]);
            cursor1.moveToNext();
            i++;
        }
    }

    public void getHighestStreak(){
        highestStreakTextView = findViewById(R.id.highestStreakTextview);
        Cursor cursor=db.getHighestStreak(sessionUsername);
        highestStreak = cursor.getString(0);
        highestStreakTextView.setText(highestStreak);
        cursor.close();
    }

    public void getHighestHeartRate(){
        highestHeartRateTextView = findViewById(R.id.highestHeartRateTextview);

        for(int i = 0; i<workoutHeartRate.length;i++){
            if(workoutHeartRate[i] > highestHeartRate){
                highestHeartRate = workoutHeartRate[i];
            }
        }
        highestHeartRateTextView.setText(String.valueOf(highestHeartRate));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(HistoryGraphActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}