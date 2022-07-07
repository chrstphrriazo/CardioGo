package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.res.Resources;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class DoWorkoutActivity extends AppCompatActivity {
    int display;
    ImageView myImageView;
    TextView workoutName;
    TextView timer;
    LinearLayout linearLayout;
    ProgressBar timerProgress;
    private static final long START_TIME_IN_MILLIS = 60000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    Animation shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);
        myImageView = findViewById(R.id.gifView);
        workoutName = findViewById(R.id.textView);
        timer = findViewById(R.id.mTextField);
        linearLayout = findViewById(R.id.bottomcontainer);
        timerProgress = findViewById(R.id.timerprogress);
        shake =  AnimationUtils.loadAnimation(this,R.anim.shake);
        int resID;
        String drawableString;

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            display = bundle.getInt("wCounter");
            System.out.println("Got: "+display);
            workoutName.setText(StartWorkoutActivity.finalWorkoutSet[display][0]);
            drawableString= StartWorkoutActivity.finalWorkoutSet[display][3];
            resID = getResources().getIdentifier(drawableString, "drawable", getPackageName());
            System.out.println(getPackageName());
            System.out.println("res id here="+resID);
            Glide.with(this).load(resID).into(myImageView);
            System.out.println(drawableString);
        }

    startTimer();

    }
    public void startTimer(){
        new CountDownTimer(mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateTimerOutput();
            }
            public void onFinish() {
                StartWorkoutActivity.eCounter++;
                DoWorkoutActivity.this.finish();
                Intent intent = new Intent(DoWorkoutActivity.this, CheckHeartRate.class);
                startActivity(intent);

            }
        }.start();
    }

    public void updateTimerOutput(){
        int seconds = (int) mTimeLeftInMillis /1000 %60;
        String timeLeftFormat = String.format(Locale.getDefault(),"%02d",seconds);
        timerProgress.setProgress(seconds);
        if(seconds==5){
            timer.setAnimation(shake);
            timer.setTextColor(ContextCompat.getColor(this,R.color.red));
            timer.startAnimation(shake);

        }
        timer.setText(timeLeftFormat);
    }
}