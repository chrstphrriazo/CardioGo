package com.example.cardiogo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogImages extends BottomSheetDialogFragment {


    ImageView avatar;
    ImageButton avatar1, avatar2, avatar3, avatar4, avatar5;
    Button save, cancel;
    int [] images = new int[]{R.drawable.default_avatar,R.drawable.boy_avatar, R.drawable.boy_avatar_2, R.drawable.girl_avatar,R.drawable.girl_avatar_2};
    int selected;
    String avatarName;
    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_images,container, false);

        avatar = v.findViewById(R.id.avatar);
        avatar1 = v.findViewById(R.id.setAvatar1);
        avatar2 = v.findViewById(R.id.setAvatar2);
        avatar3 = v.findViewById(R.id.setAvatar3);
        avatar4 = v.findViewById(R.id.setAvatar4);
        avatar5 = v.findViewById(R.id.setAvatar5);
        save = v.findViewById(R.id.saveButton);
        cancel = v.findViewById(R.id.cancelbutton);

        avatar.setImageResource(ChangeAvatarActivity.resID);
        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setImageResource(images[0]);
                selected = images[0];
                avatarName = "default_avatar";
            }
        });
        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setImageResource(images[1]);
                selected = images[1];
                avatarName = "boy_avatar";
            }
        });
        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setImageResource(images[2]);
                selected = images[2];
                avatarName = "boy_avatar_2";
            }
        });
        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setImageResource(images[3]);
                selected = images[3];
                avatarName = "girl_avatar";
            }
        });
        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setImageResource(images[4]);
                selected = images[4];
                avatarName = "girl_avatar_2";
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonClicked(selected,avatarName);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return v;
    }

    public interface BottomSheetListener{
         void onButtonClicked(int newResId, String avatarName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener = (BottomSheetListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



