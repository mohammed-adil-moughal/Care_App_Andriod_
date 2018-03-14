package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.adil.checkup.Activities.BloodGlucose;
import com.example.adil.checkup.Activities.SetTimer;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.Timer;
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
 * Created by adil on 6/18/17.
 */


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class GlucoseAdapter extends RecyclerView.Adapter<GlucoseAdapter.ViewHolder> {

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private  int id;

    public List<Glucose> getMeds() {
        return mMeds;
    }

    private ArrayList<Glucose> mMeds;

    // Store the context for easy access
    private Context mContext;
    ;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView text_level;
        private final TextView text_id;
        private final ImageView Delete;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);
            text_id= (TextView) v.findViewById(R.id.text_id);
            text_level= (TextView) v.findViewById(R.id.text_level);
            Delete= (ImageView) v.findViewById(R.id.imagedelete);


        }

        public TextView getTextView() {
            return textView;
        }



    }

    public GlucoseAdapter(Context context, ArrayList<Glucose> timer) {
        mMeds = timer;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_glucose, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Glucose contact = mMeds.get(position);

        TextView textView = viewHolder.textView;
        TextView textViewlevel=viewHolder.text_level;
        TextView text_id=viewHolder.text_id;
        ImageView del=viewHolder.Delete;
        id= contact.getGlucose_id();
        int level=contact.getGlucose_level();
        Date d=contact.getGlucose_date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String currentDateTimeString = sdf.format(d);

        textView.setText(currentDateTimeString);
        textView.setId(contact.getGlucose_id());
        textViewlevel.setText(level+"");
        text_id.setText(String.valueOf(contact.getGlucose_id()));

//        Log.d(TAG, "Element "+contact.get()+"Recycler position" + position + " sqlite_id."+id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),String.valueOf(contact.getGlucose_id()),Toast.LENGTH_LONG).show();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),String.valueOf(contact.getGlucose_id()),Toast.LENGTH_LONG).show();
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

    public void dele(Glucose timer)
    {
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();

        User user=dataStore.select(User.class).get().first();
        Log.d("positions", String.valueOf(timer));
        deleteserv(String.valueOf(timer.getGlucose_id()), String.valueOf(user.getUser_UID()));

        dataStore.delete(timer);


        id= timer.getGlucose_id();
        Log.d(TAG, "Element id to delete"+id);

//        dataStore.delete(Timer.class).where(Timer.TIMER_ID.eq(id)).get().value();
        mMeds.remove(timer);
        notifyDataSetChanged();

    }
    public interface OnSaveListener
    {
        void onSave();
    }
    private void deleteserv(final String glucoseid, final String user_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.DELETE_GLUCOSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                           Log.d("G_res",""+glucoseid);
                        Log.d("G_res",""+user_id);

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
                params.put(AppConfig.KEY_GLUCOSEID, glucoseid);
                params.put(AppConfig.KEY_USER, user_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}