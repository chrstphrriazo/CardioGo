package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WorkoutHistoryAdapter workoutHistoryAdapter;
    DatabaseHelper db;

    String sessionUsername;

    //Arrays to use
    String[] aveHR = new String[0];
    String[] aveSPo2 = new String[0];

    String[] dateToday = new String[0];
    String[] workoutStatus = new String[0];

    Animation alphagogo, btt;
    LinearLayout bottomcontainer, uppercontainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SessionManagement sessionManagement = new SessionManagement(HistoryActivity.this);
        sessionUsername = sessionManagement.getSession();

        recyclerView = findViewById(R.id.recyclerView);
        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);

        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);
        getWorkoutHistoryData(sessionUsername);
        setRecyclerview();
    }

    private void setRecyclerview() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutHistoryAdapter = new WorkoutHistoryAdapter(this,getList());
        recyclerView.setAdapter(workoutHistoryAdapter);
    }

    private List<WorkoutHistoryModel> getList() {
        List<WorkoutHistoryModel> workoutHistoryModelList = new ArrayList<>();
        for(int i =1; i <= aveHR.length; i++) {
            workoutHistoryModelList.add(new WorkoutHistoryModel(aveHR[aveHR.length-i], aveSPo2[aveHR.length-i], dateToday[aveHR.length-i], workoutStatus[aveHR.length-i]));
        }

        return workoutHistoryModelList;
    }

    public void getWorkoutHistoryData(String username){
        db = new DatabaseHelper(this);
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.userWorkoutHistoryData(sessionUsername);

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        System.out.println("entries" + test_i);
        System.out.println();

        Cursor cursor1 = db.userWorkoutHistoryData(sessionUsername);
        while (i < test_i) {
            aveHR = Arrays.copyOf(aveHR, aveHR.length +1);
            aveHR[i] = cursor1.getString(0);

            aveSPo2 = Arrays.copyOf(aveSPo2, aveSPo2.length +1);
            aveSPo2[i] = cursor1.getString(1);

            dateToday = Arrays.copyOf(dateToday, dateToday.length +1);
            dateToday[i] = cursor1.getString(2);

            workoutStatus = Arrays.copyOf(workoutStatus, workoutStatus.length +1);
            workoutStatus[i] = cursor1.getString(3);

            System.out.println(aveHR[i]);
            System.out.println(aveSPo2[i]);
            System.out.println(dateToday[i]);
            System.out.println(workoutStatus[i]);
            cursor1.moveToNext();
            i++;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void seeGraph(View view) {
        this.finish();
        Intent intent= new Intent(HistoryActivity.this, HistoryGraphActivity.class);
        startActivity(intent);
    }
}