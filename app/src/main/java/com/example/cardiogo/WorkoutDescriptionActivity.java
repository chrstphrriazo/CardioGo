package com.example.cardiogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

public class WorkoutDescriptionActivity extends DialogFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        String myPackage = this.getClass().getName().replace("."+this.getClass().getSimpleName(),"");
        Bundle bundle=getArguments();
        String[] myStrings=bundle.getStringArray("myArray");


        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.activity_workout_description, container, false); //pass the correct layout name for the fragment

        TextView exerciseName = (TextView) view.findViewById(R.id.workoutName);
        TextView exerciseDesc = (TextView) view.findViewById(R.id.workoutDesc);

        exerciseName.setText(myStrings[0]);

        ImageView imageView = (ImageView) view.findViewById(R.id.workoutGif);
        int resID = getResources().getIdentifier(myStrings[3], "drawable", myPackage);
        int resID1 = getResources().getIdentifier(myStrings[3], "string", myPackage);
        exerciseDesc.setText(resID1);
        Glide.with(this).load(resID).into(imageView);


        return view;

        //return inflater.inflate(R.layout.activity_workout_description, container, false);


    }


}
