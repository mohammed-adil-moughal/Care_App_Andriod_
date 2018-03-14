package com.example.adil.checkup.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.adil.checkup.Fragment.BlankFragment;
import com.example.adil.checkup.LoginActivity;
import com.example.adil.checkup.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static com.example.adil.checkup.R.id.image;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("CARE APP");
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.



        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Welcome To CareAPP","medicare Re-invented", R.drawable.logo, getResources().getColor(R.color.blue_semi_transparent)));
        addSlide(AppIntroFragment.newInstance("Keep Track of your Glucose Levels ","Glucose", R.drawable.diabetes, getResources().getColor(R.color.blue_semi_transparent_pressed)));
        addSlide(AppIntroFragment.newInstance("Keep Track of your Blood Pressure ","Pressure", R.drawable.bloodpressure,  getResources().getColor(R.color.blue_semi_transparent)));
        addSlide(AppIntroFragment.newInstance("Keep Track Of Medication","Never miss a pill", R.drawable.medication,  getResources().getColor(R.color.blue_semi_transparent_pressed)));
        addSlide(AppIntroFragment.newInstance("Real time Communication with your Doctor","instant support", R.drawable.doctor,  getResources().getColor(R.color.blue_semi_transparent)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));


        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent i=new Intent(IntroActivity.this,LoginActivity.class);
        startActivity(i);


    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}