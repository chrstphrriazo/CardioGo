package com.example.cardiogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AchievementsDescriptionActivity extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        String myPackage = this.getClass().getName().replace("."+this.getClass().getSimpleName(),"");
        Bundle bundle=getArguments();
        String myString=bundle.getString("string1");
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.activity_achievements_description, container, false);
        TextView achievementsDesc = (TextView) view.findViewById(R.id.achievementDesc);
        ImageView imageView = (ImageView) view.findViewById(R.id.achievement);
        int resID = getResources().getIdentifier(myString, "drawable", myPackage);
        int resID1 = getResources().getIdentifier(myString, "string", myPackage);
        achievementsDesc.setText(resID1);
        imageView.setImageResource(resID);
        return view;

    }
}
