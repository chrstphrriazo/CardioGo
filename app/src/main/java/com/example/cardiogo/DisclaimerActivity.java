package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DisclaimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
    }

    public void proceedButton(View view) {
        DisclaimerActivity.this.finish();
        Intent intent = new Intent(DisclaimerActivity.this, StartWorkoutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DisclaimerActivity.this.finish();
        Intent intent = new Intent(DisclaimerActivity.this, MainActivity.class);
        startActivity(intent);
    }
}