package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public TextView username;
    public TextView streakView;
    public TextView day;
    public TextView current_level;
    public ImageView avatar;
    public TextView progress;
    public static int user_level;
    public static int user_xp;
    public static int xp_bar; //(int)(100 * (user_level * 1.5));
    public String sessionUsername;
    DatabaseHelper db;

    ImageButton startWokroutButton;
    Animation alphagogo, btt;
    LinearLayout bottomcontainer, uppercontainer;


    Calendar calendar;
    int streak = 0;
    public static int highest_streak;
    static int myWeightClass;
    static float BMI;
    String[] workoutHolder = new String[0];
    String[] workoutDate = new String[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to db
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionUsername = sessionManagement.getSession();
        db = new DatabaseHelper(this);
        //instantiate components
        avatar = findViewById(R.id.avatarMain);
        startWokroutButton = findViewById(R.id.startWokroutButton);
        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);
        progressBar = findViewById(R.id.progressBar);
        username = findViewById(R.id.textView_username);
        current_level = findViewById(R.id.textView_level);
        streakView = findViewById(R.id.textView_streak);
        progress=findViewById(R.id.progress);
        day = findViewById(R.id.textView_day);
        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);
        username.setText(sessionUsername);

        //gather info from db
        getDatabaseData();
        updateProgressBar();
        getAvatar();
        getWorkoutData();
        checkStreak();
        updateHighestStreak();
        getHW();
        progress.setText(user_xp+"/"+xp_bar);


        streakView.setText(Integer.toString(streak));
        if(streak<2){
            day.setText("Day");
        }else{
            day.setText("Days");
        }

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
            workoutHolder = Arrays.copyOf(workoutHolder, workoutHolder.length +1);
            workoutHolder[i] = cursor1.getString(1);

            workoutDate = Arrays.copyOf(workoutDate, workoutDate.length +1);
            workoutDate[i] = cursor1.getString(0);
            System.out.println(workoutHolder[i]);
            System.out.println(workoutDate[i]);
            cursor1.moveToNext();
            i++;
        }
    }

    public void checkStreak(){
        db = new DatabaseHelper(this);
        Log.d("Check streak error", "Entrance here");
        if(workoutHolder.length == 0){
            Log.d("Check streak error", "Oh loko");
            return;
        }
        String cwHolder = workoutHolder[0];
        String wdHolder = workoutDate[0];
        String workoutDateHolder;
        boolean daySteak = false;


        for(int i = 0; i < workoutHolder.length; i++){

            // System.out.println(streak);
            System.out.println("daystreak" + daySteak);

            workoutDateHolder = workoutDate[i];

            if(i == 0){
                if(wdHolder.equals(workoutDate[i]) && cwHolder.equals("SUCCESS")){
                    streak++;
                    daySteak = true;
                    System.out.println("1 Streak : " + streak);
                    continue;
                }else{
                    System.out.println("1 Streak : " + streak);
                    continue;
                }
            }

            if(workoutHolder[i].equals("STOPPED")){
                System.out.println("Streak STOPPED" );
                cwHolder = workoutHolder[i];
                wdHolder = workoutDate[i];
                continue;
            }

            if(!(wdHolder.equals(workoutDateHolder))){
                daySteak = false;
            }

//            if(daySteak){
//                System.out.println("2 Streak : " + streak);
//                continue;
//            }

            if(wdHolder.equals(workoutDateHolder)){
                if(daySteak){
                    System.out.println("streak is : daystreak");
                    continue;
                }
            }



            if((addDate(wdHolder)).equals(workoutDateHolder)){
                System.out.println("THISSSSSSSSSS" + addDate(wdHolder));
                wdHolder = workoutDateHolder;
                daySteak = true;
                if(workoutHolder[i].equals("SUCCESS")){
                    streak++;
                    System.out.println("3 Streak : " + streak);
                    continue;
                }else{
                    streak = 0;
                    wdHolder = workoutDateHolder;
                    System.out.println("3 Streak : " + streak);
                    continue;
                }
            }else{
                if(workoutHolder[i].equals("SUCCESS")){
                    daySteak = true;
                    streak++;
                    wdHolder = workoutDateHolder;
                    System.out.println("4 Streak : " + streak);
                    continue;
                }else{
                    streak = 0;
                    wdHolder = workoutDateHolder;
                    System.out.println("4 Streak : " + streak);
                    continue;
                }
            }


        }
        db.updateStreak(sessionUsername, streak);
        System.out.println("streak is : " + streak); // STREAK VARIABLE CAN BE SET TEXTED
        Cursor cursor = db.getStreak(sessionUsername);
        String checker = cursor.getString(0);
        System.out.println("streak in db : " + checker);

    }

    public void updateHighestStreak(){
        if(streak>highest_streak){
            highest_streak=streak;
            db.update_highestStreak(sessionUsername,highest_streak);

        }

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


    private void getDatabaseData(){
        try {

            //user_level
            Cursor cursor=db.getLevel(sessionUsername);
            user_level=cursor.getInt(0);
            current_level.setText(Integer.toString(user_level));

            //exp bar
            cursor=db.getExpbar(sessionUsername);
            xp_bar=cursor.getInt(0);
            progressBar.setMax(xp_bar);

            //current exp
            cursor=db.getExp(sessionUsername);
            user_xp=cursor.getInt(0);
            progressBar.setProgress(user_xp);


            System.out.println("name:"+sessionUsername);
            System.out.println("level:"+user_level);
            System.out.println("level:"+user_xp+"/"+xp_bar);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateProgressBar(){
        //test dynamic expbar
        if (user_xp/xp_bar >= 1){

            if (user_xp == xp_bar){
                user_xp = 0;
                db.update_exp(sessionUsername);
            }
            else{
                user_xp = user_xp - xp_bar;
                db.update_exp(sessionUsername);
            }
            System.out.println("level up");
            user_level += 1;
            db.update_level(sessionUsername);
            Cursor cursor = db.getLevel(sessionUsername);
            user_level=cursor.getInt(0);
            db.update_level(sessionUsername);
            current_level.setText(Integer.toString(user_level));
            //update new xpbar
            xp_bar = (int)(100 * (user_level * 1.2));
            db.update_expBar(sessionUsername);
            progressBar.setMax(xp_bar);
            progressBar.setProgress(user_xp);

            System.out.println("current:"+user_xp+"/"+xp_bar);
        }
    }



    public void startWorkout(View view) {
        MainActivity.this.finish();
        Intent intent = new Intent(MainActivity.this, DisclaimerActivity.class);
        startActivity(intent);


    }

    public void achievementsButton(View view) {

        try {
            MainActivity.this.finish();
            Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void exercises_button(View view) {
        MainActivity.this.finish();
        Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
        startActivity(intent);
    }

    public void historyButton(View view) {
        MainActivity.this.finish();
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void changeAvatar(View view) {
    Intent intent = new Intent(MainActivity.this, ChangeAvatarActivity.class);
    startActivity(intent);
    MainActivity.this.finish();

    }

    public void getAvatar(){
        Cursor cursor=db.getAvatar(sessionUsername);
        String myAvatar = cursor.getString(0);
        System.out.println("it should be"+myAvatar);
        int resID = getResources().getIdentifier(myAvatar, "drawable", getPackageName());
        avatar.setImageResource(resID);
    }

    public void getHW(){
        //get user height and weight
        Cursor cursor=db.getUserData(sessionUsername);
        float userHeight = cursor.getFloat(1);
        float userWeight = cursor.getFloat(2);

        BMI = userWeight / (userHeight * userHeight);
        if(BMI < 18.5){
            myWeightClass = 1;
        }else if (BMI >= 18.5 && BMI <=24.9){
            myWeightClass = 1;
        }else if (BMI >= 25 && BMI <= 29.9){
            myWeightClass = 2;
        }else{
            myWeightClass = 3;
        }
        db.update_bmi(sessionUsername);
    }


}