package com.example.cardiogo;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {

    ImageButton achievement_1;
    ImageButton achievement_2;
    ImageButton achievement_3;
    ImageButton achievement_4;
    ImageButton achievement_5;
    ImageButton achievement_6;
    String[] userAchievements;
    String[] achievement_pool;
    String userStreak;
    String userLevel;

    int a = 3;
    public static int user_level;
    public static int streak;
    public String sessionUsername;
    DatabaseHelper db;
    List<ImageButton> imageButtons = new ArrayList<ImageButton>();
    Animation alphagogo, btt;
    LinearLayout uppercontainer;
    GridLayout bottomcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManagement sessionManagement = new SessionManagement(AchievementsActivity.this);
        sessionUsername = sessionManagement.getSession();
        db = new DatabaseHelper(this);
        fetchLevel(sessionUsername);
        fetchHighestStreak(sessionUsername);

        setContentView(R.layout.activity_achievements);
        imageButtons.add((ImageButton) findViewById(R.id.badge_1));
        imageButtons.add((ImageButton) findViewById(R.id.badge_2));
        imageButtons.add((ImageButton) findViewById(R.id.badge_3));
        imageButtons.add((ImageButton) findViewById(R.id.badge_4));
        imageButtons.add((ImageButton) findViewById(R.id.badge_5));
        imageButtons.add((ImageButton) findViewById(R.id.badge_6));
        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);
        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);


        collectAvailableAchievement();
        collectAllWorkouts();
        for(int i=0; i < 6; i++){
            imageButtons.get(i).setEnabled(false);
        }

        for(int i=0; i < achievement_pool.length; i++){
            for (int j=0; j < userAchievements.length; j++){
                System.out.println("-->" + achievement_pool[i] +" == " +userAchievements[j]);
                if (achievement_pool[i].equals(userAchievements[j])){
                    imageButtons.get(i).setEnabled(true);
                }
            }
        }




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(AchievementsActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void fetchLevel(String username){
        Cursor cursor=db.getLevel(username);
        userLevel=cursor.getString(0);
        cursor.close();
    }

    public void fetchHighestStreak(String username){
        Cursor cursor=db.getHighestStreak(sessionUsername);
        userStreak = cursor.getString(0);
        cursor.close();
    }

    public void collectAvailableAchievement() {
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.myUserAchievements(userStreak,userLevel);

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        cursor.close();
        userAchievements = new String[test_i];
        Cursor cursor1 = db.myUserAchievements(userStreak, userLevel);
        while (i < test_i) {
            userAchievements[i] = cursor1.getString(0);
            cursor1.moveToNext();
            i++;
        }
        cursor1.close();
    }

    public void collectAllWorkouts() {
        int test_i = 0;
        int i = 0;
        Cursor cursor = db.achievements();

        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            test_i++;
        }
        cursor.close();
        achievement_pool = new String[test_i];
        Cursor cursor1 = db.achievements();
        while (i < test_i) {
            achievement_pool[i] = cursor1.getString(0);
            cursor1.moveToNext();
            i++;
        }
        cursor1.close();
    }

    public void achievement_1(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_fitness");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }

    public void achievement_2(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_lvl3");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }

    public void achievement_3(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_workout");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }

    public void achievement_4(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_lvl5");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }

    public void achievement_5(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_mentality");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }

    public void achievement_6(View view) {
        AchievementsDescriptionActivity achievementsDescriptionActivity = new AchievementsDescriptionActivity();
        Bundle bundle = new Bundle();
        bundle.putString("string1", "achievements_streak7");
        achievementsDescriptionActivity.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        achievementsDescriptionActivity.show(getSupportFragmentManager(),"FirstFragment");
    }


}
