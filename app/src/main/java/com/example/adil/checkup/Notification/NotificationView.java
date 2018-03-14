package com.example.adil.checkup.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.services.AlarmReceiver;

import java.io.File;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class NotificationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.TAKE_YOUR_MEDICATION);

        CheckUp app =   ((CheckUp)getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        Button cancel= (Button) findViewById(R.id.buttoncancel);
        final int med_id=getIntent().getIntExtra("Medicine_id",0);

        Log.d("NotificationViewID", String.valueOf(med_id));

//

        TextView Medicine= (TextView) findViewById(R.id.lbltitle);
        TextView Dosage= (TextView) findViewById(R.id.lbltext);
        ImageView myImage= (ImageView) findViewById(R.id.imageView);
String img=getIntent().getStringExtra("img");
        Medicine.setText(getIntent().getStringExtra("title"));
        Dosage.setText(getIntent().getStringExtra("name"));

        File imgFile =    new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+img+".jpg");
             Toast.makeText(getApplicationContext(),img,Toast.LENGTH_LONG).show();
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            myImage.setImageBitmap(myBitmap);

        }
        final PendingIntent alarmIntent;
       alarmIntent = PendingIntent.getBroadcast(this, med_id,new Intent(this, AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


//                        alarmIntent.g
                        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent i=new Intent(NotificationView.this,AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationView.this.getApplicationContext(),
                                med_id, i, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                        alarmManager.cancel(pendingIntent);
                        NotificationView.this.stopService(i);

                        Intent intent = AlarmReceiver.getMedDetailsIntent(med_id);

                        sendBroadcast(intent);
                        moveTaskToBack(true);                    }
                }
        );
//        Intent intentt = new Intent(MainActivity., NextActivity.class);
//        PendingIntent pintent = PendingIntent.getService(this, 10, intentt, 0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                Toast.makeText(getApplicationContext(),"Back Pressed",Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;
//                if(getSupportFragmentManager().getBackStackEntryCount()>0)
//                    getSupportFragmentManager().popBackStack();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
