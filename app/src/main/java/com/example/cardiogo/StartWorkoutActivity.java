package com.example.cardiogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.Random;

public class StartWorkoutActivity extends AppCompatActivity {
    //all testing
    public String out;
    public Bundle results;


    //Initial workouts storage
    public static StartWorkoutActivity mySelf;
    String[][] userWorkout;
    //temporary storage of 5 workout types
    String[][] type1 = {{"0","0","0","0","0"}};
    String[][] type2 = {{"0","0","0","0","0"}};
    String[][] type3 = {{"0","0","0","0","0"}};
    String[][] type4 = {{"0","0","0","0","0"}};
    String[][] type5 = {{"0","0","0","0","0"}};
    //storage of final workout set
    public static String[][] finalWorkoutSet = new String[0][5];
    String userClass;
    String userLevel;
    static int eCounter = 0;
    static int heartRateHolder[] = new int[5];
    static int SPO2Holder[] = new int[5];
    DatabaseHelper db;

    Button exercise1;
    Button exercise2;
    Button exercise3;
    Button exercise4;
    Button exercise5;
    Animation alphagogo, btt;
    LinearLayout bottomcontainer, uppercontainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        exercise1 = findViewById(R.id.button1);
        exercise2 = findViewById(R.id.button2);
        exercise3 = findViewById(R.id.button3);
        exercise4 = findViewById(R.id.button4);
        exercise5 = findViewById(R.id.button5);

        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);

        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);

        //temporary storage of 5 workout types
        type1 = new String[][] {{"0","0","0","0","0"}};
        type2 = new String[][] {{"0","0","0","0","0"}};
        type3 = new String[][] {{"0","0","0","0","0"}};
        type4 = new String[][] {{"0","0","0","0","0"}};
        type5 = new String[][] {{"0","0","0","0","0"}};
        //storage of final workout set
        finalWorkoutSet = new String[0][5];
        eCounter = new Integer(0);



        System.out.println("StartWorkout--Activity");
        db = new DatabaseHelper(this);
        mySelf =this;
        double streak = 1.3;
        SessionManagement sessionManagement = new SessionManagement(StartWorkoutActivity.this);
        String sessionUsername = sessionManagement.getSession();

        //fetch level and weight class to be used
        fetchWeightClass(sessionUsername);
        fetchLevel(sessionUsername);
        //generate user workout set
        collectAvailableWorkouts();
        sortByType(userWorkout);
        getFinalWorkoutSet(type1,0);
        getFinalWorkoutSet(type2,1);
        getFinalWorkoutSet(type3,2);
        getFinalWorkoutSet(type4,3);
        getFinalWorkoutSet(type5,4);

        exercise1.setText("1. "+finalWorkoutSet[0][0]);
        exercise2.setText("2. "+finalWorkoutSet[1][0]);
        exercise3.setText("3. "+finalWorkoutSet[2][0]);
        exercise4.setText("4. "+finalWorkoutSet[3][0]);
        exercise5.setText("5. "+finalWorkoutSet[4][0]);

        System.out.println("This is my plan:");

        int looper = finalWorkoutSet.length;
        System.out.println(looper);
        for (int i=0;i<looper;i++){
            System.out.println(i+1 + finalWorkoutSet[i][0]);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("hello");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(StartWorkoutActivity.this, DisclaimerActivity.class);
        startActivity(intent);
    }

    //get user weight class
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
        userWorkout = new String[test_i][5];
        Cursor cursor1 = db.myUserWorkouts(userClass, userLevel);
        while (i < test_i) {
            userWorkout[i][0] = cursor1.getString(0);
            userWorkout[i][1] = cursor1.getString(1);
            userWorkout[i][2] = cursor1.getString(2);
            userWorkout[i][3] = cursor1.getString(3);
            userWorkout[i][4] = cursor1.getString(4);
            cursor1.moveToNext();
            i++;
        }
        cursor1.close();
    }

    //Distribute workout to list per workout type
    public void sortByType(@NonNull String[][] workoutList) {
        int stopper = workoutList.length;
        int t1 = 0;
        int t2= 0;
        int t3=0;
        int t4=0;
        int t5 = 0;
        int i = 0;
        while (i < stopper) {
            String myType = userWorkout[i][4];
            switch (myType) {
                case "type1":
                    if(t1>0)
                        type1 = Arrays.copyOf(type1,type1.length+1);
                    type1[t1]=userWorkout[i];
                    t1++;
                    break;
                case "type2":
                    if(t2>0)
                        type2 = Arrays.copyOf(type2,type2.length+1);
                    type2[t2]=userWorkout[i];
                    t2++;
                    break;
                case "type3":
                    if(t3>0)
                        type3 = Arrays.copyOf(type3,type3.length+1);
                        type3[t3]=userWorkout[i];
                    t3++;
                    break;
                case "type4":
                    if(t4>0)
                        type4 = Arrays.copyOf(type4,type4.length+1);
                    type4[t4]=userWorkout[i];
                    t4++;
                    break;
                case "type5":
                    if(t5>0)
                        type1 = Arrays.copyOf(type5,type5.length+1);
                    type5[t5]=userWorkout[i];
                    t5++;
                    break;
            }
            i++;
        }
    }

    //Finalize workout set
    public void getFinalWorkoutSet(String[][] set, int index){
        Random rand = new Random();
        int myPick;
        finalWorkoutSet = Arrays.copyOf(finalWorkoutSet,finalWorkoutSet.length+1);
        if(set.length>1){
            myPick = rand.nextInt(set.length);
                finalWorkoutSet[index] = set[myPick];
        }else{
                finalWorkoutSet[index] = set[0];
        }
        //finalWorkoutSet
    }

    public static StartWorkoutActivity getInstance(){
        return mySelf;
    }

    public void startButton(View view) {
        Intent intent = new Intent(StartWorkoutActivity.this, DoWorkoutActivity.class);
        intent.putExtra("wCounter",eCounter);
        startActivity(intent);
    }



    public void workout1(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", finalWorkoutSet[0]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout2(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", finalWorkoutSet[1]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout3(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", finalWorkoutSet[2]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout4(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", finalWorkoutSet[3]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    public void workout5(View view) {
        WorkoutDescriptionActivity workoutDescriptionActivity = new WorkoutDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putStringArray("myArray", finalWorkoutSet[4]);
        workoutDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        workoutDescriptionActivity.show(getSupportFragmentManager(), "showDescription");
    }

    //End of Activity
}

