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

import com.example.adil.checkup.Activities.AddVisitActivity;
import com.example.adil.checkup.Activities.ImageViewActivity;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Image;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by adil on 7/9/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;

    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private int id;

    public List<Image> getMeds() {
        return mMeds;
    }

    private ArrayList<Image> mMeds;

    // Store the context for easy access
    private Context mContext;
    ;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView text_level;
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
            text_level = (TextView) v.findViewById(R.id.text_level);
            DeleteReminer = (ImageButton) v.findViewById(R.id.buttonDeleteReminder);
            cardview= (CardView) v.findViewById(R.id.card_view);


        }

        public TextView getTextView() {
            return textView;
        }


    }

    public ImageAdapter(Context context, ArrayList<Image> timer) {
        mMeds = timer;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_image, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Image contact = mMeds.get(position);

        TextView textView = viewHolder.textView;
        CardView cardView=viewHolder.cardview;

        id = contact.getImage_id();
//        int level=contact.getGlucose_level();
//        Date d = contact.getHospital_visit_date();
//
//        Log.d("HDATE", String.valueOf(contact.getHospital_visit_date()));
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy ");
//        String currentDateTimeString = sdf.format(d);

        textView.setText(contact.getImage_name());
        textView.setId(contact.getImage_id());


//        Log.d(TAG, "Element "+contact.get()+"Recycler position" + position + " sqlite_id."+id);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ImageActivity=new Intent(v.getContext(), ImageViewActivity.class);
                ImageActivity.putExtra("Image_id",contact.getImage_id());
                v.getContext().startActivity(ImageActivity);
                Toast.makeText(v.getContext(), String.valueOf(contact.getImage_id()), Toast.LENGTH_LONG).show();
//
//

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

    public void dele(Image image) {
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();
        dataStore.delete(image);
        mMeds.remove(image);
        notifyDataSetChanged();
    }

    public interface OnSaveListener {
        void onSave();
    }
}

