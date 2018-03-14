package com.example.adil.checkup.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.services.AlarmReceiver;
import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class SetTimer extends AppCompatActivity{
       public int value;
    public Button updatetimer;
    public CheckBox monday;
    public CheckBox tuesday;
    public CheckBox wednesday;
    public CheckBox thurdsday;
    public CheckBox friday;
    public CheckBox saturday;
    public CheckBox sunday;
    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private OnSaveListener onSaveListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);



        CheckUp app =   ((CheckUp)getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SET A REMINDER");
        if (extras != null) {
            value = extras.getInt("Timer_id");
            //The key argument here must match that used in the other activity
        }
//        Toast.makeText(getApplicationContext(),String.valueOf(value),Toast.LENGTH_LONG).show();

        final TimePicker timePicker= (TimePicker) findViewById(R.id.timePickerReminder);
        updatetimer= (Button) findViewById(R.id.buttonupdatetimer);
        monday= (CheckBox) findViewById(R.id.checkBoxMonday);
        tuesday= (CheckBox) findViewById(R.id.checkBoxTuesday);
        wednesday= (CheckBox) findViewById(R.id.checkBoxWednesday);
        thurdsday= (CheckBox) findViewById(R.id.checkBoxThursday);
        friday= (CheckBox) findViewById(R.id.checkBoxFriday);
        saturday= (CheckBox) findViewById(R.id.checkBoxSaturday);
        sunday= (CheckBox) findViewById(R.id.checkBoxSunday);


        final Timer timer=dataStore.findByKey(Timer.class,value);

        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        SimpleDateFormat hour=new SimpleDateFormat("hh");
        SimpleDateFormat mind=new SimpleDateFormat("mm");
        String hou=hour.format(timer.getTimer_time());
        String min=mind.format(timer.getTimer_time());
        String currentDateTimeString = sdf.format(timer.getTimer_time());

        timePicker.setCurrentHour(Integer.valueOf(hou));
        timePicker.setCurrentMinute(Integer.valueOf(min));





//       Toast.makeText(getApplicationContext(),String.valueOf(timer.isMonday()),Toast.LENGTH_LONG).show();

            monday.setChecked(timer.isMonday());
            tuesday.setChecked(timer.isTuesday());
            wednesday.setChecked(timer.isWednesday());
            thurdsday.setChecked(timer.isThursday() );
            friday.setChecked(timer.isFriday() );
            saturday.setChecked(timer.isSaturday());
            sunday.setChecked(timer.isSunday());


        updatetimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                Date desiredDate = calendar.getTime();

                timer.setTimer_time(desiredDate);
                timer.setMonday(monday.isChecked());
                timer.setTuesday(tuesday.isChecked());
                timer.setWednesday(wednesday.isChecked());
                timer.setThursday(thurdsday.isChecked());
                timer.setFriday(friday.isChecked());
                timer.setSaturday(saturday.isChecked());
                timer.setSunday(sunday.isChecked());


                dataStore.update(timer);
                Toast.makeText(getApplicationContext(),"Update Successfull"+timer.getMedicine_id(),Toast.LENGTH_SHORT).show();


                /////////////////Alarm/////////////////////////
                SimpleDateFormat hour=new SimpleDateFormat("HH");
                String hourString = hour.format(desiredDate);

                SimpleDateFormat minute=new SimpleDateFormat("mm");
                String minuteString = minute.format(desiredDate);



                Calendar cal=Calendar.getInstance();
                Calendar now=Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourString));
                cal.set(Calendar.MINUTE, Integer.parseInt(minuteString));
                cal.set(Calendar.SECOND, 0);
                int Id=timer.getMedicine_id();
                Medicine medicine=dataStore.select(Medicine.class).where(Medicine.MEDICINE_ID.eq(Id)).get().first();



                if(cal.getTimeInMillis() > now.getTimeInMillis())
                {
//            Log.d("dates", "more");
//            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
//
//            Log.d("datescal", String.valueOf(localDateFormat.format(date)));
//            Log.d("datescal", String.valueOf(cal.getTime()));
//            Log.d("datesnow", String.valueOf(now.getTime()));
                   Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);

                    intent.putExtra("Medicine_id",medicine.getMedicine_id());
                    intent.putExtra("Med_name",medicine.getMedicine_name());
                    intent.putExtra("Medicine_img",medicine.getMedicine_image());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),medicine.getMedicine_id(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(),pendingIntent );
                    Toast.makeText(getApplicationContext(), "Alarm Set.", Toast.LENGTH_LONG).show();

                }
                else if(cal.getTimeInMillis() < now.getTimeInMillis())
                {

//            Log.d("dates", ";lesser");SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
//            Log.d("datescal", String.valueOf(localDateFormat.format(date)));
//            Log.d("datescal", String.valueOf(cal.getTime()));
//            Log.d("datesnow", String.valueOf(now.getTime()));
                    Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                    intent.putExtra("Med_name",medicine.getMedicine_name());
                    intent.putExtra("Medicine_id",medicine.getMedicine_id());
                    intent.putExtra("Medicine_img",medicine.getMedicine_image());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),medicine.getMedicine_id(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis()+ (AlarmManager.INTERVAL_DAY+1),pendingIntent );
                    Toast.makeText(getApplicationContext(), "Alarm Set.", Toast.LENGTH_LONG).show();
                }

                /////////////////////////////////////////
                if(onSaveListener != null)
                {
                    onSaveListener.onSave();
                }

            }
        });


    }
    public interface OnSaveListener
    {
        void onSave();
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
