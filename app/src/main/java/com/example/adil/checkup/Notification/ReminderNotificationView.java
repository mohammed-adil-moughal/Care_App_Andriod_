package com.example.adil.checkup.Notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.services.AlarmReceiver;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class ReminderNotificationView extends AppCompatActivity {
public  int H_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remider_notification_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("UP COMMING APPOINTMENT");
        CheckUp app =   ((CheckUp)getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        Button cancel= (Button) findViewById(R.id.buttoncancel);
        final int med_id=getIntent().getIntExtra("Medicine_id",0);
        H_id=getIntent().getIntExtra("med_id",0);



        Log.d("NotificationViewID", String.valueOf(H_id));

//        Medicine medicine=dataStore.select(Medicine.class).where(Medicine.MEDICINE_ID.eq(med_id)).get().first();
//
        Hospital hospital=dataStore.select(Hospital.class).where(Hospital.HOSPITAL_VISIT_ID.eq(H_id)).get().first();
        List<Medicine>medicine=dataStore.select(Medicine.class).where(Medicine.MEDICINE_HOSPITAL_VISIT.eq(H_id)).get().toList();
        String names = "";
        for (Medicine t : medicine) {
            names+= ","+ t.getMedicine_name();
        }
        TextView diagnosis= (TextView) findViewById(R.id.textViewDiagnosis);
        TextView treatment= (TextView) findViewById(R.id.textViewTreatment);
        TextView weight= (TextView) findViewById(R.id.TextViewWeight);
        TextView height= (TextView) findViewById(R.id.TextViewheight);
        TextView bloodpressure= (TextView) findViewById(R.id.TextViewBloodPressure);
        TextView temperature= (TextView) findViewById(R.id.TextViewTemperature);

//        Medicine.setText(getIntent().getStringExtra("title"));
//        Dosage.setText(getIntent().getStringExtra("name"));
        diagnosis.setText(hospital.getHospital_visit_diagnosis());
        treatment.setText(hospital.getHospital_visit_treatment()+names);
        weight.setText(getResources().getString(R.string.weight)+ ":"+hospital.getHospital_visit_weight());
        height.setText(getResources().getString(R.string.height)+ ":"+hospital.getHospital_visit_height_feet() + " ' " +hospital.getHospital_visit_height_inches());
        bloodpressure.setText(getResources().getString(R.string.blood_pressure)+ ":" +hospital.getHospital_visit_bloodPressure());
        temperature.setText(getResources().getString(R.string.temperature)+ ":"+hospital.getHospital_visit_temperature());

        final PendingIntent alarmIntent;
        alarmIntent = PendingIntent.getBroadcast(this, med_id,new Intent(this, AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


//                        alarmIntent.g
                        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent i=new Intent(ReminderNotificationView.this,AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderNotificationView.this.getApplicationContext(),
                                med_id, i, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                        alarmManager.cancel(pendingIntent);
                        ReminderNotificationView.this.stopService(i);

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
