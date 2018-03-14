package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Activities.AddVisitActivity;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Reminder;
import com.example.adil.checkup.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by adil on 7/9/17.
 */

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private int id;

    public List<Hospital> getMeds() {
        return mMeds;
    }

    private ArrayList<Hospital> mMeds;

    // Store the context for easy access
    private Context mContext;
    ;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView text_level;
        private final TextView textReminder;
        private final TextView text_id;
        private final ImageButton DeleteReminer;
        private final CardView cardview;


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText() + " clicked." + getTextView().getId());

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);
            text_id = (TextView) v.findViewById(R.id.text_id);
            textReminder= (TextView) v.findViewById(R.id.textViewReminder);
            text_level = (TextView) v.findViewById(R.id.text_level);
            DeleteReminer = (ImageButton) v.findViewById(R.id.buttonDeleteReminder);
            cardview= (CardView) v.findViewById(R.id.card_view);


        }

        public TextView getTextView() {
            return textView;
        }


    }

    public VisitAdapter(Context context, ArrayList<Hospital> timer) {
        mMeds = timer;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_visit, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Hospital contact = mMeds.get(position);

        TextView textView = viewHolder.textView;
        TextView textReminder=viewHolder.textReminder;
        CardView cardView=viewHolder.cardview;

        id = contact.getHospital_visit_id();
//        int level=contact.getGlucose_level();
        Date d = contact.getHospital_visit_date();

        Log.d("HDATE", String.valueOf(contact.getHospital_visit_date()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy ");
        String currentDateTimeString = sdf.format(d);
        textView.setText( getContext().getResources().getString(R.string.date)+":" + currentDateTimeString);


        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        Reminder reminder=dataStore.select(Reminder.class).where(Reminder.HOSPITAL_VISIT_ID.eq(contact.getHospital_visit_id())).get().firstOrNull();
if(reminder != null) {
    Date n = reminder.getReminder_date();
    SimpleDateFormat next = new SimpleDateFormat("dd-MMMM-yy ");
    String NextDateTimeString = next.format(n);
    textReminder.setText(getContext().getResources().getString(R.string.Next_Visit) + ":" + NextDateTimeString);
}

        textView.setId(contact.getHospital_visit_id());


//        Log.d(TAG, "Element "+contact.get()+"Recycler position" + position + " sqlite_id."+id);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditVisit=new Intent(v.getContext(), AddVisitActivity.class);
                EditVisit.putExtra("Visit_id",contact.getHospital_visit_id());
                v.getContext().startActivity(EditVisit);
                Toast.makeText(v.getContext(), String.valueOf(contact.getHospital_visit_id()), Toast.LENGTH_LONG).show();



            }
        });

//        viewHolder.text_level.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(v.getContext(),String.valueOf(contact.getGlucose_id()),Toast.LENGTH_LONG).show();
//                dele(contact);
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

    public void dele(Hospital hospital) {

        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();

        User user=dataStore.select(User.class).get().first();
        deleteserv(String.valueOf(hospital.getHospital_visit_id()),String .valueOf(user.getUser_UID()));
        dataStore.delete(hospital);
        mMeds.remove(hospital);
        notifyDataSetChanged();
    }

    public interface OnSaveListener {
        void onSave();
    }
    private void deleteserv(final String visit_id,final String user_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.DELETE_VISIT,
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
                params.put(AppConfig.KEY_VISITID, visit_id);
                params.put(AppConfig.KEY_USER, user_id);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}

