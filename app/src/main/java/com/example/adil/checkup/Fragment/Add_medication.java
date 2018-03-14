package com.example.adil.checkup.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.DatabaseHelper;
import com.example.adil.checkup.R;
import com.example.adil.checkup.SQLiteHandler;
import com.example.adil.checkup.models.Medicine;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import com.example.adil.checkup.AbstractViewModel;
import com.example.adil.checkup.models.Models;

import java.util.List;


/**
 * Created by adil on 5/3/17.
 */
public class Add_medication extends DialogFragment {

    private EditText medicinename;
    private EditText medicine_dosage;
    private EditText medicine_description;
    private EditText medicine_whatfor;
    private Button save;
    private DatabaseHelper db;

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

//    protected Context mContext;
    protected EntityDataStore<Persistable> mDataStore;
//    protected CheckUp mApplication;
//    protected Context context;
//
//    public void AbstractViewModel()
//    {
//        mContext = context;
//        mApplication = (CheckUp) context.getApplicationContext();
//        mDataStore = mApplication.getDataStore();
//    }



    public Add_medication() {

        // Empty constructor is required for DialogFragment

        // Make sure not to add arguments to the constructor

        // Use `newInstance` instead as shown below

    }

    public static Add_medication newInstance(String title) {

        Add_medication frag = new Add_medication();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }






    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        db = new DatabaseHelper(view.getContext());

        Intent intent = getActivity().getIntent();
       int id = intent.getIntExtra("id",0);
        if (intent != null)
        {
           Log.d("visit", String.valueOf(id));
        }


        medicinename = (EditText) view.findViewById(R.id.medicine_name);
        medicine_description= (EditText) view.findViewById(R.id.medicine_description);
        medicine_dosage= (EditText) view.findViewById(R.id.medicine_dosage);
        medicine_whatfor= (EditText) view.findViewById(R.id.medicine_whatfor);



        save= (Button) view.findViewById(R.id.save);
        // Fetch arguments from bundle and set title


        // Show soft keyboard automatically and request focus to field
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(medicine_description.getText().toString().equalsIgnoreCase(""))
                {
                    medicine_description.setError("please enter description");//it gives user to info message //use any one //
                }
                if(medicinename.getText().toString().equalsIgnoreCase(""))
                {
                    medicinename.setError("please enter Name");//it gives user to info message //use any one //
                }
                if(medicine_dosage.getText().toString().equalsIgnoreCase(""))
                {
                    medicine_dosage.setError("please enter Dosage");//it gives user to info message //use any one //
                }
                if(medicine_whatfor.getText().toString().equalsIgnoreCase(""))
                {
                    medicine_whatfor.setError("please enter field");//it gives user to info message //use any one //
                }
                else {
                Medicine m=new Medicine();
                m.setMedicine_description(medicine_description.getText().toString());
                m.setMedicine_name(medicinename.getText().toString());
                m.setMedicine_dosage(medicine_dosage.getText().toString());
                m.setMedicine_whatfor(medicine_whatfor.getText().toString());

                CheckUp app =   ((CheckUp)getContext().getApplicationContext());
                EntityDataStore<Persistable> dataStore = app.getDataStore();

                dataStore.insert(m);
                if(onSaveListener != null)
                {
                    onSaveListener.onSave();
                }

                dismiss();
                }


            }
        });


        medicinename.requestFocus();

        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample_dialog, container, false);
        getDialog().setTitle(R.string.add_medication);
        return rootView;
    }
    public interface OnSaveListener
    {
        void onSave();
    }
}
