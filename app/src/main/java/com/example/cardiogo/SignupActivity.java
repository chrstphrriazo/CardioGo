package com.example.cardiogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    DatabaseHelper db;

    EditText signupUsername, signupPassword, signupConfirmPassword, signupHeight, signupWeight, signupBirthday;
    String username, password, confirmPassword, birthday;
    int BMIClass;
    int age, streak = 0;
    float height, weight, BMI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupBirthday = findViewById(R.id.signupBirthday);
        signupBirthday.setInputType(InputType.TYPE_NULL);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) {
        db = new DatabaseHelper(this);

        int birthYear, birthMonth, birthDay;
        String birthdaySplit[];

        signupUsername = findViewById(R.id.signupUsername);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirmPassword = findViewById(R.id.signupConfirmPassword);
        signupHeight = findViewById(R.id.signupHeight);
        signupWeight = findViewById(R.id.signupWeight);
        signupBirthday = findViewById(R.id.signupBirthday);

        username = signupUsername.getText().toString();
        password = signupPassword.getText().toString();
        confirmPassword = signupConfirmPassword.getText().toString();



        //Checks if the editText is empty
        if(TextUtils.isEmpty(signupUsername.getText().toString())
                || TextUtils.isEmpty(signupPassword.getText().toString())
                || TextUtils.isEmpty(signupConfirmPassword.getText().toString())
                || TextUtils.isEmpty(signupHeight.getText().toString())
                || TextUtils.isEmpty(signupWeight.getText().toString())
                || TextUtils.isEmpty(signupBirthday.getText().toString())
        ){
            Toast.makeText(SignupActivity.this, "FILL OUT ALL FIELDS!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username is existing
        if(username.toUpperCase().equals("ADMIN")){
            Toast.makeText(SignupActivity.this, "ADMIN CANNOT BE USED AS USERNAME", Toast.LENGTH_SHORT).show(); // admin is for session username default
            return;
        }
        Boolean checkUsername =db.checkExistingUsername(username);
        if(!checkUsername) {
            Toast.makeText(SignupActivity.this, "USERNAME ALREADY EXIST", Toast.LENGTH_SHORT).show();
            return;
        }

        //CHECK IF PASSWORD HAS AT LEAST 5 CHARACTERS
        if(password.length() < 5){
            Toast.makeText(SignupActivity.this, "PASSWORD SHOULD HAVE AT LEAST 5 CHARACTERS.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if password and confirm password is the same
        password = signupPassword.getText().toString();
        confirmPassword = signupConfirmPassword.getText().toString();

        if(!password.equals(confirmPassword)){
            Toast.makeText(SignupActivity.this, "PASSWORD DO NOT MATCH", Toast.LENGTH_SHORT).show();
            return;
        }



        // Check if the height and weight is in float data type
        try {
            height = Float.parseFloat(signupHeight.getText().toString());
            weight = Float.parseFloat(signupWeight.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(SignupActivity.this, "HEIGHT AND WEIGHT SHOULD HAVE DECIMAL", Toast.LENGTH_SHORT).show();
            return;
        }

        //CHECK HEIGHT INPUT
        if (height < 0.546){
            Toast.makeText(SignupActivity.this, "MINIMUM HEIGHT INPUT IS 0.546m", Toast.LENGTH_SHORT).show();
            return;
        }else if (height > 2.72){
            Toast.makeText(SignupActivity.this, "MAXIMUM HEIGHT INPUT IS 2.72m", Toast.LENGTH_SHORT).show();
            return;
        }

        //CHECK WEIGHT INPUT
        if (weight < 2.1){
            Toast.makeText(SignupActivity.this, "MINIMUM WEIGHT INPUT IS 2.1 KG", Toast.LENGTH_SHORT).show();
            return;
        }else if (weight > 635){
            Toast.makeText(SignupActivity.this, "MAXIMUM WEIGHT INPUT IS 635 KG", Toast.LENGTH_SHORT).show();
            return;
        }


        //Check the BMI and get the BMI Class
        BMI = weight / (height * height);

        if(BMI <= 0){
            Toast.makeText(SignupActivity.this, "INVALID BMI", Toast.LENGTH_SHORT).show();
            return;
        }else if(BMI < 18.5){
            BMIClass = 1;
        }else if (BMI >= 18.5 && BMI <=24.9){
            BMIClass = 1;
        }else if (BMI >= 25 && BMI <= 29.9){
            BMIClass = 2;
        }else{
            BMIClass = 3;
        }

        // Get the player's age
        birthday = signupBirthday.getText().toString();

        birthdaySplit = birthday.split("/");

        birthYear = Integer.parseInt(birthdaySplit[2]);
        birthMonth = Integer.parseInt(birthdaySplit[0]); // mm-DD-yyyy
        birthDay = Integer.parseInt(birthdaySplit[1]);

        age = getAge(birthDay, birthMonth, birthYear);

        if(age < 18 || age > 25){
            Toast.makeText(SignupActivity.this, "PLAYERS SHOULD BE 18 - 25 YEARS OLD", Toast.LENGTH_SHORT).show();
            return;
        }


        //Inserting info to database
        Boolean createUser =db.createUser(username, password, birthday, age, height, weight, BMI, BMIClass, streak);
        if(createUser){
            Toast.makeText(SignupActivity.this, "SIGN-UP COMPLETED", Toast.LENGTH_SHORT).show();
            SignupActivity.this.finish();
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    private static int getAge(int day, int month, int year){

        Calendar today = Calendar.getInstance();

        int birthyear = year;
        int birthmonth = month;
        int birthday = day;

        int age = today.get(Calendar.YEAR) - birthyear;

        if (birthmonth > today.get(Calendar.MONTH)+1){
            age--;
        }else if(birthmonth == today.get(Calendar.MONTH)+1){
            if(birthday > today.get(Calendar.DAY_OF_MONTH)){
                age--;
            }
        }
        return age;
    }

    public void selectBirthday(View view) {
        signupBirthday = findViewById(R.id.signupBirthday);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = month+"/"+day+"/"+year;
                        signupBirthday.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
    }

}