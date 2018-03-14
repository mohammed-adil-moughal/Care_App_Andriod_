package com.example.adil.checkup.Adapters;

/**
 * Created by adil on 5/13/17.
 */
import com.example.adil.checkup.Activities.EditMedication;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Tag;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.meds;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private  int id;

    // Store the context for easy access
    private Context mContext;
   ;
    public List<Medicine> getMeds() {
        return mMeds;
    }
    private ArrayList<Medicine> mMeds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textDosage;
        private final TextView textTimer;
        private final ImageView imageView;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());
                    Intent intent = new Intent(v.getContext(), EditMedication.class);
                    intent.putExtra("key",getTextView().getId());
                    v.getContext().startActivity(intent);

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);
            textDosage= (TextView) v.findViewById(R.id.textViewdosage);
            textTimer= (TextView) v.findViewById(R.id.textViewTimer);
            imageView= (ImageView) v.findViewById(R.id.list_avatar_1);

        }

        public TextView getTextView() {
            return textView;
        }



    }

    public CustomAdapter(Context context, ArrayList<Medicine> medicines) {
        mMeds = medicines;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        Medicine contact = mMeds.get(position);
        CheckUp app = ((CheckUp)getContext().getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        List<Timer> all = dataStore.select(Timer.class).where(Timer.MEDICINE_ID.eq(contact.getMedicine_id())).get().toList();

        TextView textView = viewHolder.textView;
        TextView textView1=viewHolder.textDosage;
        TextView textView2=viewHolder.textTimer;
        ImageView myImage=viewHolder.imageView;

        ArrayList timers = new ArrayList();

        for (Timer t : all)
        {
            Date date = t.getTimer_time();
            SimpleDateFormat hour = new SimpleDateFormat("HH:mm");
            String hourString = hour.format(date);
            Log.d("strat", hourString);
            timers.add(hourString);
        }
        id= contact.getMedicine_id();
        textView.setText(contact.getMedicine_name());
        textView1.setText("Dosage : "+ contact.getMedicine_dosage());

        String timer_s=String.valueOf(timers);
        timer_s= timer_s.replace("[", " ");
        timer_s = timer_s.replace("]", " ");
        textView2.setText("Reminders :"+ String.valueOf(timer_s));
        Log.d("strat",String.valueOf(timer_s));

        textView.setId(contact.getMedicine_id());
        Log.d(TAG, "Element "+contact.getMedicine_name()+"Recycler position" + position + " sqlite_id."+id);

        File imgFile =    new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+contact.getMedicine_image()+".jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myImage.setImageBitmap(myBitmap);

        }


    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }

    public void dele(Medicine medicine)
    {
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();
//        mMeds.remove(pos);
//        this.notifyItemRemoved(pos);
//        Log.d("positions", String.valueOf(pos));
//        Medicine med = mMeds.get(pos);
//
//        id= med.getMedicine_id();
//        Log.d(TAG, "Element id to delete"+id);
       // mMeds.
       //  mMeds.remove(mMeds.indexOf(id));
        dataStore.delete(medicine);

        mMeds.remove(medicine);
        notifyDataSetChanged();


    }
}