package com.example.cardiogo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class ChangeAvatarActivity extends AppCompatActivity implements BottomSheetDialogImages.BottomSheetListener, BottomSheetDialogUsername.BottomSheetListener{

    DatabaseHelper db;
    static int resID;
    static String myAvatarName;
    static String myUsername;
    static float myHeight;
    static float myWeight;
    static String sessionUsername;
    TextView textView_Username, userUsername, userHeight, userWeight, userWeightClass;
    ImageView avatar;
    static List<String> userInfo;
    Animation alphagogo, btt;
    LinearLayout bottomcontainer, uppercontainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        SessionManagement sessionManagement = new SessionManagement(ChangeAvatarActivity.this);
        sessionUsername = sessionManagement.getSession();
        db = new DatabaseHelper(this);
        userInfo = new ArrayList<String>();
        //instantiate components
        textView_Username = findViewById(R.id.textView_username);
        userUsername = findViewById(R.id.userUsername);
        userHeight = findViewById(R.id.userHeight);
        userWeight = findViewById(R.id.userWeight);
        userWeightClass = findViewById(R.id.userWeightClass);
        avatar = findViewById(R.id.avatar);
        bottomcontainer = findViewById(R.id.bottomcontainer);
        uppercontainer = findViewById(R.id.uppercontainer);
        alphagogo = AnimationUtils.loadAnimation(this,R.anim.alphagogo);
        btt =  AnimationUtils.loadAnimation(this,R.anim.btt);

        uppercontainer.setAnimation(btt);
        bottomcontainer.setAnimation(alphagogo);

        getInfo();
        plugInfo();
        getWeightClass();

        System.out.println(userInfo.get(0));
        System.out.println(userInfo.get(1));
        System.out.println(userInfo.get(2));
        System.out.println(userInfo.get(3));
        System.out.println("this is len" + userInfo.size());
    }

    public void getInfo(){
        Cursor cursor=db.getUserData(sessionUsername);
        userInfo.add(cursor.getString(0));
        userInfo.add(cursor.getString(1));
        userInfo.add(cursor.getString(2));
        userInfo.add(cursor.getString(3));
    }

    public void plugInfo(){
        textView_Username.setText(userInfo.get(0));
        userUsername.setText(userInfo.get(0));
        userHeight.setText(userInfo.get(1));
        userWeight.setText(userInfo.get(2));
        String myAvatar = userInfo.get(3);
        resID = getResources().getIdentifier(myAvatar, "drawable", getPackageName());
        System.out.println("updated"+myAvatar);
        avatar.setImageResource(resID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ChangeAvatarActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void changeUserAvatar(View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogImages();
        bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomExample");
    }

    @Override
    public void onButtonClicked(int newResId, String avatarName) {
        avatar.setImageResource(newResId);
        myAvatarName = avatarName;
        db.update_avatar(sessionUsername);
    }


    @Override
    public void changeUsername(String type, String value) {
        if (type == "Username"){
            setNewUsername(value);
        }else if(type == "Height"){
            setNewHeight(value);
        }else{
            setNewWeight(value);
        }


    }

    public void editUsername(View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogUsername();
        Bundle bundle = new Bundle();
        bundle.putString("buttonType", "Username");
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomExamplee");
    }

    public void editHeight(View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogUsername();
        Bundle bundle = new Bundle();
        bundle.putString("buttonType", "Height");
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomExamplee");
    }

    public void editWeight(View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogUsername();
        Bundle bundle = new Bundle();
        bundle.putString("buttonType", "Weight");
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomExamplee");
    }

    public void setNewUsername(String value){
        userUsername.setText(value);
        textView_Username.setText(value);
        myUsername = value;
        db.update_username(sessionUsername);
        db.update_usernameHistory(sessionUsername);
        SessionManagement sessionManagement = new SessionManagement(ChangeAvatarActivity.this);
        sessionManagement.saveSession(myUsername);
        sessionUsername = sessionManagement.getSession();
        userInfo.clear();
        getInfo();
        plugInfo();
    }

    public void setNewHeight(String value){
        userHeight.setText(value);
        getWeightClass();
        myHeight = Float.parseFloat(value);
        db.update_height(sessionUsername);
        userInfo.clear();
        getInfo();
        plugInfo();
    }

    public void setNewWeight(String value){
        userWeight.setText(value);
        getWeightClass();
        myWeight = Float.parseFloat(value);
        db.update_weight(sessionUsername);
        userInfo.clear();
        getInfo();
        plugInfo();
    }

    public void getWeightClass(){
        float height = Float.parseFloat(userHeight.getText().toString());
        float weight = Float.parseFloat(userWeight.getText().toString());
        float wClass = weight / (height * height);
        if(wClass < 18.5){
            userWeightClass.setText("Underweight");
        }else if (wClass >= 18.5 && wClass <=24.9){
            userWeightClass.setText("Normal Weight");
        }else if (wClass >= 25 && wClass <= 29.9){
            userWeightClass.setText("Overweight");
        }else{
            userWeightClass.setText("Obese");
        }
    }


    public void logoutButton(View view) {
        SessionManagement sessionManagement = new SessionManagement(ChangeAvatarActivity.this);
        sessionManagement.removeSession();
        logoutVerified();
    }


    private void logoutVerified(){
        ChangeAvatarActivity.this.finish();
        Intent intent = new Intent(ChangeAvatarActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
