package com.example.adil.checkup.services;

/**
 * Created by adil on 5/20/17.
 */
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.BroadcastReceiver;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Fragment.MedicationFragment;
import com.example.adil.checkup.Notification.NotificationView;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Medicine;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 101;
    public static final String MED_DETAILS = "com.example.adil.checkup.services.cancel";
    public int med_id;


    @Override
    public void onReceive(Context context, Intent intent) {
        CheckUp checkUp = (CheckUp)context.getApplicationContext();
        String action=intent.getAction();

        if(action != null){
            if(action.equals(MED_DETAILS))
            {
                int med_id = intent.getIntExtra("med_id", -1);
                if(med_id == this.med_id){
                    if(checkUp.getRingtone().isPlaying())
                        checkUp.getRingtone().stop();
                    return;
                }

//                Toast.makeText(context,MED_DETAILS,Toast.LENGTH_LONG).show();
            }
        }



        String neme=intent.getStringExtra("Med_name");
        String img=intent.getStringExtra("Medicine_img");
        int med_id=intent.getIntExtra("Medicine_id",0);
        Log.d("Alarm Receiver", String.valueOf(med_id));

        Toast.makeText(context,"Alarm wOrked"+neme,Toast.LENGTH_LONG).show();

        checkUp.getRingtone().play();
        Notification(context,"Take Your Medication "+neme,med_id,neme,img );




    }


    public void Notification(Context context, String message, int med_id,String name,String img) {
// Set Notification Title
        String strtitle = context.getString(R.string.notificationtitle);
// Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, NotificationView.class);
// Send data to NotificationView Class
        intent.putExtra("med_id", med_id);
        intent.putExtra("title", strtitle);
        intent.putExtra("text", message);
        intent.putExtra("name" ,name);
        intent.putExtra("img",img);
// Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

// Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
// Set Icon
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
// Set Ticker Message
                .setTicker(message)
// Set Title
                .setContentTitle(context.getString(R.string.notificationtitle))
// Set Text
                .setContentText(message)
// Add an Action Button below Notification
//                .addAction(R.drawable.ic_drawer, "Cancel", pIntent)
// Set PendingIntent into Notification
                .setContentIntent(pIntent)
// Dismiss Notification
                .setAutoCancel(true);

// Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
// Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }




    public static Intent getMedDetailsIntent(int med_id){

        Intent intent=new Intent(MED_DETAILS);
        intent.putExtra("med_id",med_id);
        return intent;
    }
}
