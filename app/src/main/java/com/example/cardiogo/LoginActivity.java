package com.example.cardiogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText loginUsername, loginPassword;
    String username, password;
    private final int REQUEST_CODE_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onStart(){
        super.onStart();

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String sessionUsername = sessionManagement.getSession();

        if(!sessionUsername.equals("admin")){
            loginVerified();
        }else{

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "CARDIO GO NEEDS CAMERA PERMISSION", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    public void createAccount(View view) {
        LoginActivity.this.finish();
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        db = new DatabaseHelper(this);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        username = loginUsername.getText().toString();
        password = loginPassword.getText().toString();

        if(TextUtils.isEmpty(loginUsername.getText().toString()) || TextUtils.isEmpty(loginPassword.getText().toString())){
            Toast.makeText(LoginActivity.this, "FILL OUT ALL FIELDS!", Toast.LENGTH_SHORT).show();
            return;
        }

        Boolean loginVerify = db.loginVerification(username,password);

        if(!loginVerify){
            Toast.makeText(LoginActivity.this, "INVALID CREDENTIALS!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(LoginActivity.this, "WELCOME TO CARDIO GO", Toast.LENGTH_SHORT).show();

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        sessionManagement.saveSession(username);

        loginVerified();

    }

    private void loginVerified(){
        LoginActivity.this.finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}