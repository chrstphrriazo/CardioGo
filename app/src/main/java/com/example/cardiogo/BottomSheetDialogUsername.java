
package com.example.cardiogo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogUsername extends BottomSheetDialogFragment {



    TextView typeTextview;
    EditText userEditText;
    Button save, cancel;
    static String passed;
    static String value;
    private BottomSheetListener UsernameListener;
    DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_username,container, false);

        //Get extras
        db = new DatabaseHelper(getContext());
        Bundle bundle=getArguments();
        passed=bundle.getString("buttonType");
        //Instantiate components
        typeTextview = v.findViewById(R.id.typeTextview);
        userEditText = v.findViewById(R.id.userEditText);
        save = v.findViewById(R.id.saveButton);
        cancel = v.findViewById(R.id.cancelbutton);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = userEditText.getText().toString();
                if (passed == "Username"){//call for username edit
                    checkNewUsername(value);
                }else if(passed == "Height"){//call for height edit
                    checkNewHeight(value);
                }else{//call for weight edit
                    checkNewWeight(value);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dismiss();
            }
        });

        //Determine which info to edit
        passType(passed);

        return v;
    }

    public interface BottomSheetListener{
        void changeUsername(String type, String value);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            UsernameListener = (BottomSheetListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void passType(String type){
        typeTextview.setText(passed);

        if (passed == "Username"){
            userEditText.setText(ChangeAvatarActivity.userInfo.get(0));
            //set cursor on the right
            int editLength = userEditText.getText().toString().length();
            Editable editTextValue = userEditText.getText();
            Selection.setSelection(editTextValue, editLength);
        }else if(passed == "Height"){
            userEditText.setText(ChangeAvatarActivity.userInfo.get(1));
            //set cursor on the right
            int editLength = userEditText.getText().toString().length();
            Editable editTextValue = userEditText.getText();
            Selection.setSelection(editTextValue, editLength);
        }else{
            userEditText.setText(ChangeAvatarActivity.userInfo.get(2));
            //set cursor on the right
            int editLength = userEditText.getText().toString().length();
            Editable editTextValue = userEditText.getText();
            Selection.setSelection(editTextValue, editLength);
        }
    }

    public void checkNewUsername(String value){
        Boolean existingUser =db.checkExistingUsername(value);
        if(value.length() == 0){
            Toast.makeText(getContext(), "INVALID USERNAME", Toast.LENGTH_SHORT).show(); // admin is for session username default
            System.out.println("new is zero");
            return;
        }
        if(value.toUpperCase().equals("ADMIN")){
            Toast.makeText(getContext(), "INVALID USERNAME", Toast.LENGTH_SHORT).show(); // admin is for session username default
            System.out.println("new is admin");
            return;
        }
        if(!existingUser) {
            Toast.makeText(getContext(), "USERNAME ALREADY EXIST", Toast.LENGTH_SHORT).show();
            System.out.println("new is here");
            return;
        }
        UsernameListener.changeUsername(passed,value);
        dismiss();
    }

    public void checkNewHeight(String value){

        try {
            float newHeight = Float.parseFloat(value);
        }catch (NumberFormatException e){
            Toast.makeText(getContext(), "HEIGHT AND WEIGHT SHOULD HAVE DECIMAL", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Float.parseFloat(value) < 0.546){
            Toast.makeText(getContext(), "MINIMUM HEIGHT INPUT IS 0.546m", Toast.LENGTH_SHORT).show();
            return;
        }else if(Float.parseFloat(value) > 2.72){
            Toast.makeText(getContext(), "MAXIMUM HEIGHT INPUT IS 2.72m", Toast.LENGTH_SHORT).show();
            return;
        }
        UsernameListener.changeUsername(passed,value);
        dismiss();
    }

    public void checkNewWeight(String value){

        try {
            float newWeight = Float.parseFloat(value);
        }catch (NumberFormatException e){
            Toast.makeText(getContext(), "HEIGHT AND WEIGHT SHOULD HAVE DECIMAL", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Float.parseFloat(value) < 2.1){
            Toast.makeText(getContext(), "MINIMUM WEIGHT INPUT IS 2.1 KG", Toast.LENGTH_SHORT).show();
            return;
        }else if(Float.parseFloat(value) > 635){
            Toast.makeText(getContext(), "MAXIMUM WEIGHT INPUT IS 635 KG", Toast.LENGTH_SHORT).show();
            return;
        }
        UsernameListener.changeUsername(passed,value);
        dismiss();
    }

}



