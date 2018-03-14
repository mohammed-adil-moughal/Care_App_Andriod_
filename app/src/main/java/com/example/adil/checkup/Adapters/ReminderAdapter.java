package com.example.adil.checkup.Adapters;
import com.example.adil.checkup.Activities.EditMedication;
import com.example.adil.checkup.Activities.SetTimer;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Tag;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.meds;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private  int id;

    public List<Timer> getMeds() {
        return mMeds;
    }

    private ArrayList<Timer> mMeds;

    // Store the context for easy access
    private Context mContext;
    ;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton DeleteReminer;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);
            DeleteReminer= (ImageButton) v.findViewById(R.id.buttonDeleteReminder);


        }

        public TextView getTextView() {
            return textView;
        }



    }

    public ReminderAdapter(Context context, ArrayList<Timer> timer) {
        mMeds = timer;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_timer, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Timer contact = mMeds.get(position);

        TextView textView = viewHolder.textView;
        id= contact.getTimer_id();
        Date d=contact.getTimer_time();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        textView.setText(currentDateTimeString);
        textView.setId(contact.getTimer_id());
        Log.d(TAG, "Element "+contact.getTimer_time()+"Recycler position" + position + " sqlite_id."+id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),String.valueOf(contact.getTimer_id()),Toast.LENGTH_LONG).show();
                Intent timepicker=new Intent(v.getContext(), SetTimer.class);
                timepicker.putExtra("Timer_id",contact.getTimer_id());


                v.getContext().startActivity(timepicker);


            }
        });

        viewHolder.DeleteReminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),String.valueOf(contact.getTimer_id()),Toast.LENGTH_LONG).show();
                dele(contact);
                if(onSaveListener != null)
                {
                    onSaveListener.onSave();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }

    public void dele(Timer timer)
    {
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();


        Log.d("positions", String.valueOf(timer));
        dataStore.delete(timer);


        id= timer.getTimer_id();
        Log.d(TAG, "Element id to delete"+id);

//        dataStore.delete(Timer.class).where(Timer.TIMER_ID.eq(id)).get().value();
        mMeds.remove(timer);
        notifyDataSetChanged();

    }
    public interface OnSaveListener
    {
        void onSave();
    }

}