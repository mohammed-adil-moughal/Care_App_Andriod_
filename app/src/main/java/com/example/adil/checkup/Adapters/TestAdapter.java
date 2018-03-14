package com.example.adil.checkup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Chat;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static java.security.AccessController.getContext;

/**
 * Created by Adil on 30/09/2017.
 */

public class TestAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static  int check=0;

    private Context mContext;
    private ArrayList<Chat> mMessageList;

    public TestAdapter(Context context, ArrayList<Chat> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    public List<Chat> getMeds() {
        return mMessageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Chat message = mMessageList.get(position);
//
//        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
//        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        if (message.getReceiver_id().equals(4)) {
            // If the current user is the sender of the message
            check=1;
            return check;

        } else {
            // If some other user sent the message
            check=2;
            return check;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (check == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);

             Log.d("chaty","chatty");
            return new SentMessageHolder(view);
        } else if (check == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mesage_reveived, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat message =  mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.textView);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Chat message) {
            messageText.setText(message.getSender_id());

            // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
//        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.textView);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
//            nameText = (TextView) itemView.findViewById(R.id.textView);
//            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Chat message) {
            messageText.setText(message.getReceiver_id());

            // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreated_at()));

//            nameText.setText(message.getSender().getNickname());

            // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}