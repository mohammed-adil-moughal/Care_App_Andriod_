package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.Activities.Profile;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Doctors;
import com.example.adil.checkup.models.Glucose;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by adil on 6/18/17.
 */

/**
 * Created by adil on 5/13/17.
 */
import com.example.adil.checkup.Activities.EditMedication;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Tag;
import com.example.adil.checkup.models.User;
import com.example.adil.checkup.models.meds;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private  int id;

    // Store the context for easy access
    private Context mContext;
    ;
    public List<Doctors
            > getMeds() {
        return mMeds;
    }
    private ArrayList<Doctors> mMeds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());
//                    Intent intent = new Intent(v.getContext(), EditMedication.class);
//                    intent.putExtra("key",getTextView().getId());
//
//                    v.getContext().startActivity(intent);

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);

        }

        public TextView getTextView() {
            return textView;
        }



    }

    public DoctorAdapter(Context context, ArrayList<Doctors> medicines) {
        mMeds = medicines;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_doctor, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


       final Doctors contact = mMeds.get(position);

        TextView textView = viewHolder.textView;
        id= contact.getDoctor_id();
        textView.setText(contact.getDoctor_name());
        textView.setId(contact.getDoctor_id());
        Log.d(TAG, "Element "+contact.getDoctor_name()+"Recycler position" + position + " sqlite_id."+id);
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user=dataStore.select(User.class).get().first();
                user.setDoctor_id(contact.getDoctor_id());
                user.setDoctor_name(contact.getDoctor_name());
                dataStore.update(user);
                Toast.makeText(getContext(),String.valueOf(contact.getDoctor_id()),Toast.LENGTH_LONG).show();
                Intent i=new Intent(v.getContext(), Profile.class);
                v.getContext().startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }

//    public void dele(Medicine medicine)
//    {
//        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
//        EntityDataStore<Persistable> dataStore = app.getDataStore();
////        mMeds.remove(pos);
////        this.notifyItemRemoved(pos);
////        Log.d("positions", String.valueOf(pos));
////        Medicine med = mMeds.get(pos);
////
////        id= med.getMedicine_id();
////        Log.d(TAG, "Element id to delete"+id);
//        // mMeds.
//        //  mMeds.remove(mMeds.indexOf(id));
//        dataStore.delete(medicine);
//
//        mMeds.remove(medicine);
//        notifyDataSetChanged();
//
//
//    }
}