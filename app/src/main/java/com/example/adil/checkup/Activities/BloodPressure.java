package com.example.adil.checkup.Activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.PressureAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.Adapters.ReminderAdapter;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Fragment.Add_medication;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Pressure;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class BloodPressure extends AppCompatActivity {
    FloatingActionButton fab;

    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_blood_pressure));

        List<Pressure>all=mdataStore.select(Pressure.class).get().toList();
        int count=mdataStore.count(Pressure.class).get().value();
//
         mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
         setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
//
         mAdapter = new PressureAdapter(getApplicationContext(), new ArrayList(all));
//
//        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        final LineGraphSeries<DataPoint> series;
        final LineGraphSeries<DataPoint> series_normal;
        final LineGraphSeries<DataPoint> series2;
        final LineGraphSeries<DataPoint> series2_normal;
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series_normal = new LineGraphSeries<>();
        series2_normal=new LineGraphSeries<>();
        for(Pressure pressure : all)
        {

            series.appendData(new DataPoint(pressure.getPressure_id(),pressure.getPressure_diastolic()),true,5);
          series2.appendData(new DataPoint(pressure.getPressure_id(),pressure.getPressure_systolic()),true,5);
            series_normal.appendData(new DataPoint(pressure.getPressure_id(),80),true,5);
            series2_normal.appendData(new DataPoint(pressure.getPressure_id(),120),true,5);


        }

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series_normal);
        graph.addSeries(series2_normal);
        series.setTitle("Diastolic");
        series_normal.setTitle("D_Normal");
        series2.setTitle("Systolic");
        series2_normal.setTitle("S_Normal");
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series_normal.setColor(Color.GREEN);
        series_normal.setDrawDataPoints(true);
        series_normal.setDataPointsRadius(6);
        series2_normal.setColor(Color.MAGENTA);
        series2_normal.setDrawDataPoints(true);
        series2_normal.setDataPointsRadius(6);
        series2.setDrawDataPoints(true);
        series.setDataPointsRadius(6);
        series2.setDataPointsRadius(6);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);





        fab= (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(BloodPressure.this);
                View promptsView = li.inflate(R.layout.pressuredialogeform, null);

                final EditText PressureSystolic = (EditText) promptsView.findViewById(R.id.pressure_systolic);
                final EditText PressureDiastolic = (EditText) promptsView.findViewById(R.id.pressure_disatolic);
                final EditText PressurePulse = (EditText) promptsView.findViewById(R.id.pressure_pulse);
                final TextView PressureDate= (TextView) promptsView.findViewById(R.id.pressure_date);
                final TextView PressureTime= (TextView) promptsView.findViewById(R.id.pressure_time);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BloodPressure.this);

                final Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
                String check = dateFormat.format(date);
                String timed=timeFormat.format(date);
                PressureDate.setText(check);
                PressureTime.setText(timed);



                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try{
                                            Pressure pressure=new Pressure();
                                            pressure.setPressure_diastolic(Integer.parseInt(PressureDiastolic.getText().toString()));
                                            pressure.setPressure_systolic(Integer.parseInt(PressureSystolic.getText().toString()));
                                            pressure.setPressure_pulse(Integer.parseInt(PressurePulse.getText().toString()));
                                            pressure.setPressure_date(date);
                                            mdataStore.insert(pressure);
                                            User user=mdataStore.select(User.class).get().first();

                                            sendbp(String.valueOf(user.getUser_UID()),String.valueOf(pressure.getPressure_id()),String.valueOf(pressure.getPressure_diastolic()),String.valueOf(pressure.getPressure_systolic()),String.valueOf(pressure.getPressure_pulse()),String.valueOf(pressure.getPressure_date()));
                                            Toast.makeText(getApplicationContext(),"Record Saved",Toast.LENGTH_SHORT).show();
                                           PressureAdapter adapter = ((PressureAdapter)mRecyclerView.getAdapter());
                                            adapter.getMeds().add(pressure);
                                            mRecyclerView.getAdapter().notifyDataSetChanged();
                                            series.appendData(new DataPoint(pressure.getPressure_id(),pressure.getPressure_diastolic()),true,5);
                                            series2.appendData(new DataPoint(pressure.getPressure_id(),pressure.getPressure_systolic()),true,5);
                                            series_normal.appendData(new DataPoint(pressure.getPressure_id(),80),true,5);
                                            series2_normal.appendData(new DataPoint(pressure.getPressure_id(),120),true,5);

                                              }
                                        catch (Exception e){
                                            if(PressureDiastolic.getText().toString().equalsIgnoreCase(""))
                                            {
                                                PressureDiastolic.setError(getResources().getString(R.string.diastolic_error));//it gives user to info message //use any one //
                                            }
                                            if(PressureSystolic.getText().toString().equalsIgnoreCase(""))
                                            {
                                                PressureSystolic.setError(getResources().getString(R.string.systolic_error));//it gives user to info message //use any one //
                                            }
                                            if(PressurePulse.getText().toString().equalsIgnoreCase(""))
                                            {
                                                PressurePulse.setError(getResources().getString(R.string.pulse_error));//it gives user to info message //use any one //
                                            }

                                            Toast.makeText(getApplicationContext(),"Please Enter Values",Toast.LENGTH_SHORT).show();
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


    public interface OnSaveListener
    {
        void onSave();
    }

    private void sendbp(final String patient_id, final String pressure_id, final String pressure_diastolic,final String pressure_systolic,final String pressure_pulse,final String pressure_date){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.BLOOD_PRESSURE,
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
                        Toast.makeText(BloodPressure.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_PATIENTID, patient_id);
                params.put(AppConfig.KEY_PRESSUREID, pressure_id);
                params.put(AppConfig.KEY_PRESSUREDIASTOLIC, pressure_diastolic);
                params.put(AppConfig.KEY_PRESSURESYSTOLIC, pressure_systolic);
                params.put(AppConfig.KEY_PRESSUREPULSE, pressure_pulse);
                params.put(AppConfig.KEY_PRESSUREDATE, pressure_date);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.glucose, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.action_info) {
            Rect displayRectangle = new Rect();
            Window window = BloodPressure.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            LayoutInflater info = LayoutInflater.from(BloodPressure.this);

            View promptsinfo = info.inflate(R.layout.pressureinfo, null);
//            promptsinfo.setMinimumWidth((int)(displayRectangle.width() ));
//            promptsinfo.setMinimumHeight((int)(displayRectangle.height()));
            AlertDialog.Builder alertDialogBuilderinfo = new AlertDialog.Builder(
                    BloodPressure.this);

            alertDialogBuilderinfo.setView(promptsinfo);

            // set dialog message
            alertDialogBuilderinfo
                    .setCancelable(false)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialoginfo = alertDialogBuilderinfo.create();

            // show it
            alertDialoginfo.show();


            //return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
