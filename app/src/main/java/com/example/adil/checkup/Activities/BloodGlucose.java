package com.example.adil.checkup.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.Adapters.ReminderAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Fragment.Add_medication;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

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

public class BloodGlucose extends AppCompatActivity{
    FloatingActionButton fab;

    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    public Spinner spinner1;
    public String metrics;
    public Integer metric=0;
    public Integer checker=0;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_glucose);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_blood_glucose));

        final List<Glucose>all=mdataStore.select(Glucose.class).get().toList();
       int count=mdataStore.count(Glucose.class).get().value();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);

        mAdapter = new GlucoseAdapter(getApplicationContext(), new ArrayList(all));

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {

                     metric=0;
                    if(checker ==1) {
                        List<Glucose> glucose = mdataStore.select(Glucose.class).get().toList();
                        Toast.makeText(parent.getContext(),
                                "first : " + position + parent.getItemAtPosition(position).toString(),
                                Toast.LENGTH_SHORT).show();
                        metrics = parent.getItemAtPosition(position).toString();
                        for (Glucose g : glucose) {
                            Log.d("Glucose2", String.valueOf(g.getGlucose_level()));
                            List<Glucose> g1 = new ArrayList<Glucose>();
                            g.setGlucose_level(g.getGlucose_level()/18);
                            g1.add(g);
                            mAdapter = new GlucoseAdapter(getApplicationContext(), new ArrayList(g1));
                            mRecyclerView.getAdapter().notifyDataSetChanged();


                        }
                        checker=0;
                    }
                }
                else if(position == 1)
                {
                    metric=1;
                    checker=1;
                   List<Glucose>glucose2=mdataStore.select(Glucose.class).get().toList();
                    Toast.makeText(parent.getContext(),
                            "second: "+position + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                    metrics=parent.getItemAtPosition(position).toString();
                    for (Glucose g : glucose2)
                    {
                        Log.d("Glucose2", String.valueOf(g.getGlucose_level()*18));

                        List<Glucose> g2=new ArrayList<Glucose>();
                        g.setGlucose_level(g.getGlucose_level()*18);
                        g2.add(g);
                        mAdapter = new GlucoseAdapter(getApplicationContext(), new ArrayList(g2));
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("glucosee","adil");
//                List<Glucose> glucose = mdataStore.select(Glucose.class).get().toList();
//                Toast.makeText(parent.getContext(),
//                        "first : " + 0 + parent.getItemAtPosition(0).toString(),
//                        Toast.LENGTH_SHORT).show();
//                metrics = parent.getItemAtPosition(0).toString();
//                for (Glucose g : glucose) {
//                    Log.d("Glucose2", String.valueOf(g.getGlucose_level()));
//                    List<Glucose> g1 = new ArrayList<Glucose>();
//                    g.setGlucose_level(g.getGlucose_level()/18);
//                    g1.add(g);
//                    mAdapter = new GlucoseAdapter(getApplicationContext(), new ArrayList(g1));
//                    mRecyclerView.getAdapter().notifyDataSetChanged();
//
//
//                }
//                checker=0;
            }
        });

        final LineGraphSeries<DataPoint> series;
        final LineGraphSeries<DataPoint> series2;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        for(Glucose glucose : all)
        {

         series.appendData(new DataPoint(glucose.getGlucose_id(),glucose.getGlucose_level()),true,5);
            series2.appendData(new DataPoint(glucose.getGlucose_id(),5),true,5);
        }
        graph.addSeries(series);
        graph.addSeries(series2);
        series.setTitle(getResources().getString(R.string.recorded));
        series2.setTitle(getResources().getString(R.string.normal));
        series.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series2.setDrawDataPoints(true);
        series.setDataPointsRadius(6);
        series2.setDataPointsRadius(6);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setVerticalAxisTitle(metrics);
        gridLabel.setHorizontalAxisTitle("Recording");
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
                LayoutInflater li = LayoutInflater.from(BloodGlucose.this);
                View promptsView = li.inflate(R.layout.glucosedialogeform, null);


                TextView textView= (TextView) promptsView.findViewById(R.id.textViewMetrics);

                final EditText glucoseLevel = (EditText) promptsView.findViewById(R.id.glucose_level);
                final TextView glucoseDate = (TextView) promptsView.findViewById(R.id.glucose_date);
                final TextView glucoseTime = (TextView) promptsView.findViewById(R.id.glucose_time);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BloodGlucose.this);




                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                CheckUp app = ((CheckUp) getApplicationContext());
                final EntityDataStore<Persistable> dataStore = app.getDataStore();
                textView.setText(metrics);

//                DateFormat datedf = new SimpleDateFormat("d MMM yyyy");
//                String date = datedf.format(Calendar.getInstance().getTime());
//
//                DateFormat timedf= new SimpleDateFormat("h:mm a");
//                String time = timedf.format(Calendar.getInstance().getTime());

                final Date date = new Date();

// set format for date

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");

                // parse it like

                String check = dateFormat.format(date);
                String timed=timeFormat.format(date);

                glucoseDate.setText(check);
                glucoseTime.setText(timed);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        final Glucose glucose=new Glucose();
                                        glucose.setGlucose_date(date);
                                        try{

                                            final int GlucoseLevel= Integer.parseInt(glucoseLevel.getText().toString());
                                            if(metric == 0)
                                            {
                                                glucose.setGlucose_level(GlucoseLevel);
                                            }
                                            else
                                            {
                                                glucose.setGlucose_level(GlucoseLevel/18);
                                            }
                                            dataStore.insert(glucose);
                                           User user= mdataStore.select(User.class).get().firstOrNull();
                                            sendbg(String.valueOf(glucose.getGlucose_date()),String.valueOf(glucose.getGlucose_level()),String.valueOf(user.getUser_UID()),String.valueOf(glucose.getGlucose_id()));
                                            GlucoseAdapter adapter = ((GlucoseAdapter)mRecyclerView.getAdapter());
                                            if(metric == 0)
                                            {
                                                adapter.getMeds().add(glucose);
                                            }
                                            else {
                                                glucose.setGlucose_level(GlucoseLevel);
                                                adapter.getMeds().add(glucose);
                                            }
                                            mRecyclerView.getAdapter().notifyDataSetChanged();
                                            series.appendData(new DataPoint(glucose.getGlucose_id(),glucose.getGlucose_level()),true,5);
                                            series2.appendData(new DataPoint(glucose.getGlucose_id(),5),true,5);

                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(getApplicationContext(),"Enter A valid Number",Toast.LENGTH_SHORT).show();

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

    private void sendbg(final String date, final String level, final String id,final String glucoseid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.BLOOD_GLUCOSE,
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
                        Toast.makeText(BloodGlucose.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_GLUCOSEDATE, date);
                params.put(AppConfig.KEY_GLUCOSELEVEL, level);
                params.put(AppConfig.KEY_GLUCOSEUSERID, id);
                params.put(AppConfig.KEY_GLUCOSEID, glucoseid);

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
        if (id == R.id.action_info) {
            Rect displayRectangle = new Rect();
            Window window = BloodGlucose.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            LayoutInflater info = LayoutInflater.from(BloodGlucose.this);

            View promptsinfo = info.inflate(R.layout.glucoseinfo, null);
//            promptsinfo.setMinimumWidth((int)(displayRectangle.width() ));
//            promptsinfo.setMinimumHeight((int)(displayRectangle.height()));
            AlertDialog.Builder alertDialogBuilderinfo = new AlertDialog.Builder(
                    BloodGlucose.this);

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
