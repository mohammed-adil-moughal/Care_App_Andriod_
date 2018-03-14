package com.example.adil.checkup.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.VisitMedicationAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Fragment.HospitalFragment;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Reminder;
import com.example.adil.checkup.models.User;
import com.example.adil.checkup.services.ReminderReceiver;
import com.example.adil.checkup.services.ScheduleClient;
import com.rey.material.app.TimePickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.example.adil.checkup.R.layout.activity_add_visit;

public class AddVisitActivity extends AppCompatActivity{
    private RecyclerView.Adapter mAdapter;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public Hospital hospital;
    public Reminder reminder;
    public Reminder old_reminder;
    public Hospital hospitalFill;
    private LayoutManagerType mCurrentLayoutManagerType;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    public EditText a_time;
    public ScheduleClient scheduleClient;
    public  User user;
    public Calendar newDate = Calendar.getInstance();

    public SimpleDateFormat dateFormatter;

    public int hos_id;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_visit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hospital Visit");
        FloatingActionButton fab= (FloatingActionButton)findViewById(R.id.fab_add);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final Button AddVisitMedication= (Button) findViewById(R.id.buttonaddvisitmed);

        final EditText height_feet= (EditText) findViewById(R.id.heightfeet);
        final EditText height_inches= (EditText) findViewById(R.id.heightinches);
        final EditText weight= (EditText) findViewById(R.id.TextViewWeight);
        final EditText bloodpressure= (EditText) findViewById(R.id.bloodpressure);
        final EditText temperature= (EditText) findViewById(R.id.temperature);
        final EditText diagnosis= (EditText) findViewById(R.id.diagnosis);
        final EditText treatment= (EditText) findViewById(R.id.treatment);
        final TextView BMI= (TextView) findViewById(R.id.textbmi);
        height_feet.setText("0");
        weight.setText("0");
        height_inches.setText("0");


        final DecimalFormat format=new DecimalFormat("##.00");
        final EditText a_date= (EditText) findViewById(R.id.nextappointment);
        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    double w= Integer.parseInt(String.valueOf(weight.getText().toString()));
                    double h_f=Integer.parseInt(String.valueOf(height_feet.getText().toString()));
                    int h_i=Integer.parseInt(String.valueOf(height_inches.getText().toString()));
                    h_f=h_f*12 +h_i;
                    h_f=h_f*0.025;
                    h_f=h_f*h_f;
                    w=w/h_f;
                    Log.d("BMI", String.valueOf(format.format(w)));
                    BMI.setText(getResources().getString(R.string.BMI)+":" + String.valueOf(format.format(w)));
                }
            }
        });
//        height_feet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    double w= Integer.parseInt(String.valueOf(weight.getText().toString()));
//                    double h_f=Integer.parseInt(String.valueOf(height_feet.getText().toString()));
//                    int h_i=Integer.parseInt(String.valueOf(height_inches.getText().toString()));
//                    h_f=h_f*12 +h_i;
//                    h_f=h_f*0.025;
//                    h_f=h_f*h_f;
//                    w=w/h_f;
//                    Log.d("BMI", String.valueOf(format.format(w)));
//                    BMI.setText(getResources().getString(R.string.BMI)+":" + String.valueOf(format.format(w)));
//                }
//            }
//        });

//        height_inches.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    double w= Integer.parseInt(String.valueOf(weight.getText().toString()));
//                    double h_f=Integer.parseInt(String.valueOf(height_feet.getText().toString()));
//                    int h_i=Integer.parseInt(String.valueOf(height_inches.getText().toString()));
//                    h_f=h_f*12 +h_i;
//                    h_f=h_f*0.025;
//                    h_f=h_f*h_f;
//                    w=w/h_f;
//                    Log.d("BMI", String.valueOf(format.format(w)));
//                    BMI.setText(getResources().getString(R.string.BMI)+":" + String.valueOf(format.format(w)));
//                }
//            }
//        });
        a_time= (EditText) findViewById(R.id.nextappointmenttime);
        a_date.setInputType(InputType.TYPE_NULL);
        a_date.requestFocus();
        a_time.setInputType(InputType.TYPE_NULL);
        a_time.requestFocus();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            hos_id = extras.getInt("Visit_id");
            Log.d("hos_id", String.valueOf(hos_id));
            hospital=mdataStore.select(Hospital.class).where(Hospital.HOSPITAL_VISIT_ID.eq(hos_id)).get().first();

            reminder=mdataStore.select(Reminder.class).where(Reminder.HOSPITAL_VISIT_ID.eq(hos_id)).get().firstOrNull();

            if(reminder != null)
      {
          Date old=reminder.getReminder_date();
          SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
          SimpleDateFormat hour=new SimpleDateFormat("hh");
          SimpleDateFormat mind=new SimpleDateFormat("mm");
          SimpleDateFormat day = new SimpleDateFormat("dd-MM-yyyy");
          String c_time=sdf.format(old);
          String c_year=day.format(old);
          a_time.setText(c_time);
          a_date.setText(c_year);
      }
//            old.getDay();


            height_feet.setText(String.valueOf(hospital.getHospital_visit_height_feet()));
            height_inches.setText(String.valueOf(hospital.getHospital_visit_height_inches()));
            weight.setText(String.valueOf(hospital.getHospital_visit_weight()));
            bloodpressure.setText(String.valueOf(hospital.getHospital_visit_bloodPressure()));
            temperature.setText(String.valueOf(hospital.getHospital_visit_temperature()));
            diagnosis.setText(hospital.getHospital_visit_diagnosis());
            treatment.setText(hospital.getHospital_visit_treatment());

                        double w= Integer.parseInt(String.valueOf(weight.getText().toString()));
                        double h_f=Integer.parseInt(String.valueOf(height_feet.getText().toString()));
                        int h_i=Integer.parseInt(String.valueOf(height_inches.getText().toString()));
                        h_f=h_f*12 +h_i;
                        h_f=h_f*0.025;
                        h_f=h_f*h_f;
                        w=w/h_f;
                        Log.d("BMI", String.valueOf(w));
                        BMI.setText(getResources().getString(R.string.BMI)+":" + String.valueOf(format.format(w)));

        }

        else{
            hospitalFill=mdataStore.select(Hospital.class).get().firstOrNull();

            Calendar cal = Calendar.getInstance();
            final Date date=cal.getTime();

            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DAY_OF_WEEK, 7);
            final Date date1=cal1.getTime();

            hospital=new Hospital();
            if(hospitalFill != null)
            {
                height_feet.setText(String.valueOf(hospitalFill.getHospital_visit_height_feet()));
                height_inches.setText(String.valueOf(hospitalFill.getHospital_visit_height_inches()));
                weight.setText(String.valueOf(hospitalFill.getHospital_visit_weight()));
                hospital.setHospital_visit_height_inches(hospitalFill.getHospital_visit_height_inches());
                hospital.setHospital_visit_height_feet(hospitalFill.getHospital_visit_height_feet());
                hospital.setHospital_visit_weight(hospital.getHospital_visit_weight());

                double w= Integer.parseInt(String.valueOf(weight.getText().toString()));
                double h_f=Integer.parseInt(String.valueOf(height_feet.getText().toString()));
                int h_i=Integer.parseInt(String.valueOf(height_inches.getText().toString()));
                h_f=h_f*12 +h_i;
                h_f=h_f*0.025;
                h_f=h_f*h_f;
                w=w/h_f;
                Log.d("BMI", String.valueOf(w));
                BMI.setText(getResources().getString(R.string.BMI)+":" + String.valueOf(format.format(w)));

            }


            hospital.setHospital_visit_date(date);

            mdataStore.insert(hospital);
            reminder = new Reminder();
            reminder.setHospital_visit_id(hospital.getHospital_visit_id());
            reminder.setReminder_date(date1);
            mdataStore.insert(reminder);


        }

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(AddVisitActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                newDate.set(year, monthOfYear, dayOfMonth);
                a_date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);



        a_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AddVisitActivity.this,"Clisteds",Toast.LENGTH_LONG).show();
                fromDatePickerDialog.show();
            }
        });


        a_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               TimePickerFragment1 newFragment = new TimePickerFragment1();
//                newFragment.show(getFragmentManager(),"TimePicker");   LayoutInflater li = LayoutInflater.from(AddVisitActivity.this);
                LayoutInflater li = LayoutInflater.from(AddVisitActivity.this);
                View promptsView = li.inflate(R.layout.dialog_time, null);
                final android.widget.TimePicker timePicker= (android.widget.TimePicker) promptsView.findViewById(R.id.dialog_time_picker);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        AddVisitActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        Calendar calendar = Calendar.getInstance();
                                        newDate.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                                        newDate.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                                        newDate.set(Calendar.SECOND,0);
                                        Date desiredDate = newDate.getTime();
                                        //      a_time.setText(String.valueOf(timePicker.getCurrentHour())+":"+String.valueOf(timePicker.getCurrentMinute()));
                                        a_time.setText(String.valueOf(desiredDate.getHours())+":"+String.valueOf(desiredDate.getMinutes()));




                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(height_feet.getText().toString())) {
                    height_feet.setError("empty field");
                    return;
                }
                else if(TextUtils.isEmpty(height_inches.getText().toString()))
                {
                    height_inches.setError("empty field");
                    return;
                }
                else if(TextUtils.isEmpty(weight.getText().toString()))
                {
                    weight.setError("empty field");
                    return;
                }
                else if(TextUtils.isEmpty(bloodpressure.getText().toString()))
                {
                    bloodpressure.setError("empty field");
                    return;
                }
                else if(TextUtils.isEmpty(temperature.getText().toString()))
                {
                    temperature.setError("empty field");
                    return;
                }
                else if(TextUtils.isEmpty(diagnosis.getText().toString()))
                {
                 diagnosis.setError("empty field");
                }
                else if(TextUtils.isEmpty(treatment.getText().toString()))
                {
                    treatment.setError("empty field");
                }
                else {
                    hospital.setHospital_visit_height_feet(Integer.parseInt(height_feet.getText().toString()));
                    hospital.setHospital_visit_height_inches(Integer.parseInt(height_inches.getText().toString()));
                    hospital.setHospital_visit_weight(Integer.parseInt(weight.getText().toString()));
                    hospital.setHospital_visit_bloodPressure(Integer.parseInt(bloodpressure.getText().toString()));
                    hospital.setHospital_visit_temperature(Integer.parseInt(temperature.getText().toString()));
                    hospital.setHospital_visit_diagnosis(diagnosis.getText().toString());
                    hospital.setHospital_visit_treatment(treatment.getText().toString());


                    Date date = newDate.getTime();
                    Calendar now=Calendar.getInstance();
                    reminder.setReminder_date(date);
                    mdataStore.update(reminder);

                    if(newDate.getTimeInMillis() > now.getTimeInMillis())
                    {
                        Intent intent = new Intent(getApplicationContext(),ReminderReceiver.class);

                        intent.putExtra("Med_id",hospital.getHospital_visit_id());
                        intent.putExtra("M_id",hospital.getHospital_visit_id());
                        intent.putExtra("Med_name","te3st");

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, newDate.getTimeInMillis(),pendingIntent );
                        Toast.makeText(getApplicationContext(), "Reminder Set.", Toast.LENGTH_LONG).show();

                    }

                    else if(now.getTimeInMillis() > newDate.getTimeInMillis())
                    {
                        Intent intent = new Intent(getApplicationContext(),ReminderReceiver.class);

                        intent.putExtra("Med_id",hospital.getHospital_visit_id());
                        intent.putExtra("M_id",hospital.getHospital_visit_id());
                        intent.putExtra("Med_name","te3st");

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, newDate.getTimeInMillis()+ (AlarmManager.INTERVAL_DAY+1),pendingIntent );
                        Toast.makeText(getApplicationContext(), "Reminder Set.", Toast.LENGTH_LONG).show();

                    }



                    mdataStore.update(hospital);
                    user=mdataStore.select(User.class).get().first();
                    sendbg(user.getUser_UID(),hospital.getHospital_visit_id(),hospital.getHospital_visit_height_feet(),hospital.getHospital_visit_height_inches(),hospital.getHospital_visit_weight(),hospital.getHospital_visit_bloodPressure(),hospital.getHospital_visit_temperature(),hospital.getHospital_visit_diagnosis(),hospital.getHospital_visit_treatment());
                    Toast.makeText(AddVisitActivity.this,"Visit Information Saved",Toast.LENGTH_SHORT).show();

                }
            }
        });
        AddVisitMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(AddVisitActivity.this);
                View promptsView = li.inflate(R.layout.fragment_hospital_visit_medication, null);

                final EditText MedicineName = (EditText) promptsView.findViewById(R.id.medicine_name);
                final EditText MedicineDosage = (EditText) promptsView.findViewById(R.id.medicine_dosage);
                final EditText MedicineDescription = (EditText) promptsView.findViewById(R.id.medicine_description);
                final EditText MedicineWhatfor= (EditText) promptsView.findViewById(R.id.medicine_whatfor);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        AddVisitActivity.this);


                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try{
                                            Medicine medicine= new Medicine();
                                            medicine.setMedicine_name(MedicineName.getText().toString());
                                            medicine.setMedicine_dosage(MedicineDosage.getText().toString());
                                            medicine.setMedicine_description(MedicineDescription.getText().toString());
                                            medicine.setMedicine_whatfor(MedicineWhatfor.getText().toString());
                                            medicine.setMedicine_hospital_visit(hospital.getHospital_visit_id());
                                            mdataStore.insert(medicine);
                                            VisitMedicationAdapter adapter = (VisitMedicationAdapter) mRecyclerView.getAdapter();
                                            adapter.getMeds().add(medicine);
                                            mRecyclerView.getAdapter().notifyDataSetChanged();

                                        }
                                        catch (Exception e){
                                            Toast.makeText(getApplicationContext(),"Please Enter an Integer",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



            }
        });
        List<Medicine> all=mdataStore.select(Medicine.class).where(Medicine.MEDICINE_HOSPITAL_VISIT.eq(hospital.getHospital_visit_id())).get().toList();

        Log.d("allMedicines", String.valueOf(all));
        mAdapter = new VisitMedicationAdapter(getApplicationContext(), new ArrayList(all));
        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);



    }
    private void sendbg(final int user_id, final int visit_id, final int height_feet, final int height_inches,
                        final int weight, final int bloodpressure, final int temperature, final String diagnosis, final String treatment){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.POST_VISIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.d("RResponse",response);




                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddVisitActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_HEIGHTFEET, String.valueOf(height_feet));
                params.put(AppConfig.KEY_HEIGHTINCHES, String.valueOf(height_inches));
                params.put(AppConfig.KEY_WEIGHT, String.valueOf(weight));
                params.put(AppConfig.KEY_PRESSURE, String.valueOf(bloodpressure));
                params.put(AppConfig.KEY_TEMPERATURE, String.valueOf(temperature));
                params.put(AppConfig.KEY_DIAGNOSIS,diagnosis);
                params.put(AppConfig.KEY_TREATMENT, String.valueOf(treatment));
                params.put(AppConfig.KEY_USER, String.valueOf(user_id));
                params.put(AppConfig.KEY_VISITID, String.valueOf(visit_id));


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getApplicationContext(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
    private static final int SPAN_COUNT = 2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
