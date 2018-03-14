package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.Activities.Profile;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Doctors;
import com.example.adil.checkup.models.User;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String TAG = "ChatAdapter";
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private String[] mDataSet;
    private  int id;

    // Store the context for easy access
    private Context mContext;
    ;
    public List<Chat> getMeds() {
        return mMeds;
    }

    private ArrayList<Chat> mMeds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textViewStatus;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getTextView().getText()+ " clicked."+getTextView().getId());

                }
            });

            textView = (TextView) v.findViewById(R.id.textView);
            textViewStatus = (TextView) v.findViewById(R.id.text_message_name);

        }

        public TextView getTextView() {
            return textView;
        }



    }

    public ChatAdapter(Context context, ArrayList<Chat> medicines) {
        mMeds = medicines;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_chat, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


       final Chat contact = mMeds.get(position);
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        User user=dataStore.select(User.class).get().first();
        TextView textView = viewHolder.textView;
        TextView textViewStatus=viewHolder.textViewStatus;
        Log.d("chat",contact.getReceiver_id());
        Log.d("chat", String.valueOf(user.getUser_UID()));
        if(Integer.parseInt(contact.getSender_id())== (user.getUser_UID()))
        {
//            textView.setBackgroundColor(Color.parseColor("#00FF7F"));
            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded2));
//            textViewStatus.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textView.setLayoutParams(params);
        }
        else{
            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            textView.setLayoutParams(params);

        }

        id= contact.getEntry_id();
        textView.setText(contact.getMessages());
        textView.setId(contact.getEntry_id());
        int d = Log.d(TAG, "Element " + contact.getEntry_id() + "Recycler position" + position + " sqlite_id." + id);
        textView.setGravity(6);


//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                User user=dataStore.select(User.class).get().first();
//                user.setDoctor_id(contact.getDoctor_id());
//                user.setDoctor_name(contact.getDoctor_name());
//                dataStore.update(user);
//                Toast.makeText(getContext(),String.valueOf(contact.getDoctor_id()),Toast.LENGTH_LONG).show();
//                Intent i=new Intent(v.getContext(), Profile.class);
//                v.getContext().startActivity(i);
//
//            }
//        });


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