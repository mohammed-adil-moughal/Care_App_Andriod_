package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Pressure;
import com.example.adil.checkup.models.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by adil on 6/21/17.
 */

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.ViewHolder> {

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private  int id;

    public List<Pressure> getMeds() {
        return mMeds;
    }

    private ArrayList<Pressure> mMeds;

    // Store the context for easy access
    private Context mContext;
    ;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_diastolic;
        private final TextView text_sysastolic;
        private  TextView textView;
        private final TextView text_id;
        private final TextView text_pulse;
        private final TextView text_date;
        private final ImageView Delete;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());

                }
            });
          //  text_textview = (TextView) v.findViewById(R.id.textview);
            text_diastolic = (TextView) v.findViewById(R.id.text_diastolic);
            text_sysastolic = (TextView) v.findViewById(R.id.text_sysastolic);

            text_id= (TextView) v.findViewById(R.id.text_id);
            text_pulse= (TextView) v.findViewById(R.id.text_pulse);
            text_date= (TextView) v.findViewById(R.id.text_date);

            Delete= (ImageView) v.findViewById(R.id.imagedelete);


        }





    }

    public PressureAdapter(Context context, ArrayList<Pressure> timer) {
        mMeds = timer;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_pressure, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Pressure contact = mMeds.get(position);
        Log.d("pressure", String.valueOf(contact.getPressure_date()));

        TextView text_id=viewHolder.text_id;
        TextView textViewssytolic=viewHolder.text_sysastolic;
        TextView textViewdyastolic=viewHolder.text_diastolic;
        TextView textViewpulse=viewHolder.text_pulse;
        TextView textView = viewHolder.text_date;
        ImageView del=viewHolder.Delete;


        id= contact.getPressure_id();
        Date d=contact.getPressure_date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String currentDateTimeString = sdf.format(d);

        textView.setText(currentDateTimeString);
        textView.setId(contact.getPressure_id());
        text_id.setText(String.valueOf(id));
        textViewdyastolic.setText(String.valueOf(contact.getPressure_diastolic()));
        textViewssytolic.setText(String.valueOf(contact.getPressure_systolic()));
        textViewpulse.setText(String.valueOf(contact.getPressure_pulse()));


//        Log.d(TAG, "Element "+contact.get()+"Recycler position" + position + " sqlite_id."+id);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),String.valueOf(contact.getPressure_id()),Toast.LENGTH_LONG).show();
//
//            }
//        });


       del.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               Toast.makeText(v.getContext(),"DELETING",Toast.LENGTH_LONG).show();
               dele(contact);
                if(onSaveListener != null)
                {
                    onSaveListener.onSave();
                }
           }
       });
//        viewHolder.text_level.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(v.getContext(),String.valueOf(contact.getPressure_id()),Toast.LENGTH_LONG).show();
////                dele(contact);
//                if(onSaveListener != null)
//                {
//                    onSaveListener.onSave();
//                }
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }

    public void dele(Pressure timer)
    {
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();


        Log.d("positions", String.valueOf(timer));
        User user=dataStore.select(User.class).get().first();
        deleteserv(String.valueOf(timer.getPressure_id()),String.valueOf(user.getUser_UID()));

        dataStore.delete(timer);
        id= timer.getPressure_id();
        Log.d(TAG, "Element id to delete"+id);

//        dataStore.delete(Timer.class).where(Timer.TIMER_ID.eq(id)).get().value();
        mMeds.remove(timer);
        notifyDataSetChanged();

    }
    public interface OnSaveListener
    {
        void onSave();
    }
    private void deleteserv(final String pressureid,final String user_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.DELETE_PRESSURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_PRESSUREID, pressureid);
                params.put(AppConfig.KEY_USER, user_id);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}