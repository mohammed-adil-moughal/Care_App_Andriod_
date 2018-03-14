package com.example.adil.checkup.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adil.checkup.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodGlucoseFragment extends Fragment {

    public BloodGlucoseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_glucose, container, false);
    }
}
