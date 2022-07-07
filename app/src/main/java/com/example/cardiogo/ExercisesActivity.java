package com.example.cardiogo;

import static com.example.cardiogo.StartWorkoutActivity.finalWorkoutSet;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExercisesActivity extends AppCompatActivity {
    DatabaseHelper db;
    String userClass;
    String userLevel;
    String[] userWorkout;
    String[][] workouts;
    List<ImageButton> imageButtons = new ArrayList<ImageButton>();
    Animation alphagogo, btt;
    LinearLayout uppercontainer;
    GridLayout bottomcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManagement sessionManagement = new SessionManagement(ExercisesActivity.this);
        String sessionUsername = sessionManagement.getSession();
        db = new DatabaseHelper(this);
        fetchWeightClass(sessionUsername);
        fetchLevel(sessionUsername);

        if(userClass.equals("1"))
            setContentView(R.layout.activity_exercises_type1);
        else if (userClass.equals("2"))
            setContentView(R.layout.activity_exercises_type2);
        else
            setContentView(R.layout.activity_exercises_type3);

        imageButtons.add((ImageButton) findViewById(R.id.exercise_1));
        imageButtons.add((ImageButton) findViewById(R.id.exercise_2));
        imageButtons.add((ImageButton) findViewById(R.id.exercise_3));
        imageButtons.add((ImageButton) findViewById(R.id.exercise_4));
        imageButtons.add((ImageButton) findViewById(R.id.exercise_5));
        imageButtons.add((ImageButton) findViewById(R.id.exercise_6));
        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);
        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);

        //get workout pool and unlocked workouts
        collectAllWorkouts();
        collectAvailableWorkouts();

        //set all buttons to disabled as default
        for(int i=0; i < 6; i++){
           imageButtons.get(i).setEnabled(false);
        }


        //collect unlocked from the pool
        for(int i=0; i < workouts.length; i++){
            for (int j=0; j < userWorkout.length; j++){
                System.out.println("-->" + workouts[i][0] +" == " +userWorkout[j]);
                if (workouts[i][0].equals(userWorkout[j])){
                    imageButtons.get(i).setEnabled(true);
                }
            }
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void fetchWeightClass(String username){
        Cursor cursor=db.getWeightClass(username);
        userClass=cursor.getString(0);
        cursor.close();
    }

    //get user level
    public void fetchLevel(String username){
        Cursor cursor=db.getLevel(username);
        userLevel=cursor.getString(0);
        cursor.close();
    }

    //get available workout (consider user level and weight class) and store into array
    public void collectAvailableWorkouts() {
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.myUserWorkouts(userClass,userLevel);

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        cursor.close();
        userWorkout = new String[test_i];
        Cursor cursor1 = db.myUserWorkouts(userClass, userLevel);
        while (i < test_i) {
            userWorkout[i] = cursor1.getString(0);
            cursor1.moveToNext();
            i++;
        }
        cursor1.close();
    }

    //get available workout and store into array
    public void collectAllWorkouts() {
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.workOuts(userClass);

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        cursor.close();
        workouts = new String[test_i][5];
        Cursor cursor1 = db.workOuts(userClass);
        while (i < test_i) {
            workouts[i][0] = cursor1.getString(0);
            workouts[i][1] = cursor1.getString(1);
            workouts[i][2] = cursor1.getString(2);
            workouts[i][3] = cursor1.getString(3);
            workouts[i][4] = cursor1.getString(4);
            cursor1.moveToNext();
            i++;
        }
        cursor1.close();
    }

    public void workout1(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[0]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout2(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[1]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout3(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[2]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout4(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[3]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout5(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[4]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout6(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", workouts[5]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }



}
