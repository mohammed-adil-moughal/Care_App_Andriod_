package com.example.adil.checkup;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.adil.checkup.Activities.IntroActivity;
import com.example.adil.checkup.Activities.Profile;
import com.example.adil.checkup.Activities.chatactivity;
import com.example.adil.checkup.Fragment.HomeFragment;
import com.example.adil.checkup.Fragment.HospitalFragment;
import com.example.adil.checkup.Fragment.InfoFragment;
import com.example.adil.checkup.Fragment.MedicationFragment;
import com.example.adil.checkup.Fragment.TrackerFragment;
import com.example.adil.checkup.Other.CircleTransform;
import com.example.adil.checkup.R;
import com.example.adil.checkup.SessionManager;
import com.example.adil.checkup.SQLiteHandler;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Emergency;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Models;
import com.example.adil.checkup.models.Pressure;
import com.example.adil.checkup.models.Reminder;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.User;
import com.example.adil.checkup.services.AlarmReceiver;
import com.example.adil.checkup.services.TutorialIntentService;
import com.example.adil.checkup.services.TutorialService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import io.requery.util.CloseableIterator;

public class MainActivity extends AppCompatActivity {


    private TextView txtEmail;
    private Button btnLogout;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private SQLiteHandler db;
    private SessionManager session;
    private EntityDataStore<Persistable> mDataStore;
    private static final String urlNavHeaderBg = "https://www.colourbox.com/preview/2366300-heart-pulse-an-abstract-medical-background.jpg";
    private static final String urlProfileImg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_MEDICATION = "Medication";
    private static final String TAG_TRACKER = "Records";
    private static final String TAG_VISITS="Visits";
    private static final String TAG_RECORDS="Photo|Files";
    public static String CURRENT_TAG = TAG_HOME;
    public static String name;
    public static String email;
    private static MainActivity inst;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private ReactiveEntityStore<Persistable> dataStore;

    public EntityDataStore<Persistable> getDataStore() {
        if (mDataStore == null) {
            // override onUpgrade to handle migrating to a new version
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT, 1);
            if (BuildConfig.DEBUG) {
                // use this in development mode to drop and recreate the tables on every upgrade
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            }
            Configuration configuration = source.getConfiguration();
            mDataStore = new EntityDataStore<>(configuration);
        }
        
        return mDataStore;
    }
    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base, "sw"));
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("s");

//        Intent intent_service = new Intent(this, TutorialIntentService.class);
//        startService(intent_service);

//        Intent service = new Intent(this, TutorialService.class);
//        startService(service);

//Alarm
        /* In your onCreate method */
        //  Declare a new thread to do a preference check

//        end
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> xdataStore = app.getDataStore();

        User user_details = xdataStore.select(User.class).get().firstOrNull();
        if (user_details == null) {
            Toast.makeText(getApplicationContext(), "NO User", Toast.LENGTH_LONG).show();
            Log.d("T123", "T123");
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();

        }
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);



            mHandler = new Handler();

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            fab = (FloatingActionButton) findViewById(R.id.fab);

            // Navigation view header
            navHeader = navigationView.getHeaderView(0);
            txtName = (TextView) navHeader.findViewById(R.id.name);
            txtWebsite = (TextView) navHeader.findViewById(R.id.website);
            imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
            db = new SQLiteHandler(getApplicationContext());

            HashMap<String, String> user = db.getUserDetails();

            name = user.get("name");
            email = user.get("email");
            // load toolbar titles from string resources
            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            // load nav menu header data
            loadNavHeader();

            // initializing navigation menu
            setUpNavigationView();

            if (savedInstanceState == null) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }

//        txtName = (TextView) findViewById(R.id.name);
//        txtEmail = (TextView) findViewById(R.id.email);
//        btnLogout = (Button) findViewById(R.id.btnLogout);


            // session manager
            session = new SessionManager(getApplicationContext());
            txtName.setText(user_details.getUser_email());

//        txtEmail.setText("Moughal");

            try {
                List<Timer> timers = xdataStore.select(Timer.class).get().toList();
                for (Timer t : timers) {

                    Date date = t.getTimer_time();
                    SimpleDateFormat hour = new SimpleDateFormat("HH");
                    String hourString = hour.format(date);

                    SimpleDateFormat minute = new SimpleDateFormat("mm");
                    String minuteString = minute.format(date);


                    Calendar cal = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourString));
                    cal.set(Calendar.MINUTE, Integer.parseInt(minuteString));
                    cal.set(Calendar.SECOND, 0);


                    if (cal.getTimeInMillis() > now.getTimeInMillis()) {
//            Log.d("dates", "more");
//            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
//
//            Log.d("datescal", String.valueOf(localDateFormat.format(date)));
//            Log.d("datescal", String.valueOf(cal.getTime()));
//            Log.d("datesnow", String.valueOf(now.getTime()));
                        Medicine medicine = xdataStore.select(Medicine.class).where(Medicine.MEDICINE_ID.eq(t.getMedicine_id())).get().firstOrNull();
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        intent.putExtra("Med_name", medicine.getMedicine_name());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                        Toast.makeText(this, "Alarm Set.", Toast.LENGTH_LONG).show();

                    } else if (cal.getTimeInMillis() < now.getTimeInMillis()) {

//            Log.d("dates", ";lesser");SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
//            Log.d("datescal", String.valueOf(localDateFormat.format(date)));
//            Log.d("datescal", String.valueOf(cal.getTime()));
//            Log.d("datesnow", String.valueOf(now.getTime()));
                        Medicine medicine = xdataStore.select(Medicine.class).where(Medicine.MEDICINE_ID.eq(t.getMedicine_id())).get().firstOrNull();
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        intent.putExtra("Med_name", medicine.getMedicine_name());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1), pendingIntent);
                        Toast.makeText(this, "Alarm Set.", Toast.LENGTH_LONG).show();
                    }


                }


            } catch (Exception e) {

            }



    }


    public void logoutUser() {
        session.setLogin(false);


        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void loadNavHeader() {
        // name, website
        txtName.setText(name);
        txtWebsite.setText(email);

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        Log.d("Response", String.valueOf(navItemIndex));
        switch (navItemIndex) {
            case 0:
                //Home
                HomeFragment HomeFrag = new HomeFragment();
                return HomeFrag;
            case 1:
                //Medication
                MedicationFragment MedFrag = new MedicationFragment();
                 return MedFrag;
            case 2:
                //VitalsTracker
                TrackerFragment TFrag=new TrackerFragment();
                return  TFrag;
            case 3:
                //HospitalTracker
                HospitalFragment HFrag=new HospitalFragment();
                return  HFrag;
            case 4:
                InfoFragment IFrag=new InfoFragment();
                return  IFrag;


            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        Log.d("RESPOSE", String.valueOf(navItemIndex));
    }
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        Log.d("RESPOSE", String.valueOf(navItemIndex));
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                Log.d("Response","invavview");
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_medication:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MEDICATION;
                        break;
                    case R.id.nav_tracker:
                        navItemIndex= 2;
                        CURRENT_TAG= TAG_TRACKER;
                        break;
                    case R.id.nav_hospital:
                        navItemIndex=3;
                        CURRENT_TAG=TAG_VISITS;
                        break;
                    case R.id.nav_records:
                        navItemIndex=4;
                        CURRENT_TAG=TAG_RECORDS;
                        break;
                    case R.id.nav_chat:
                        Intent chat=new Intent(MainActivity.this, chatactivity.class);
                        startActivity(chat);
//                        Toast.makeText(getApplicationContext(),"chat",Toast.LENGTH_LONG).show();

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifactions, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        if(id ==R.id.action_settings)
        {
            Intent profile=new Intent(MainActivity.this,Profile.class);
            startActivity(profile);
            return  true;
        }
        if(id ==R.id.action_enroll)
        {
            Intent intent = new Intent(this, PersonVerificationActivity.class);
            intent.putExtra("Type",2);
            startActivity(intent);
        }
        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_sync) {
            CheckUp app = ((CheckUp)getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        final User user=mdataStore.select(User.class).get().first();
        try {
            mdataStore.delete(Hospital.class).get().value();
            mdataStore.delete(Reminder.class).get().value();
            mdataStore.delete(Glucose.class).get().value();
            mdataStore.delete(Pressure.class).get().value();
        }
        catch (Exception e)
        {
            Log.d("DELETE",e.toString());
        }
        // TODO Auto-generated method stub
//////////////////////VISITS//////////////////////////////////////////////////////
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.d("User123", String.valueOf(user.getUser_UID()));
        final String url = AppConfig.GET_VISIT+user.getUser_UID();
        Log.d("User123", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                       // Log.d("User123", String.valueOf(response));
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject doc = null;
                            try {
                                doc = response.getJSONObject(i);
                                Log.d("User123",doc.toString());
                                Hospital hospital=new Hospital();
                                hospital.setHospital_visit_height_feet(doc.getInt("height_feet"));
                                hospital.setHospital_visit_height_inches(doc.getInt("height_inches"));
                                hospital.setHospital_visit_weight(doc.getInt("weight"));
                                hospital.setHospital_visit_bloodPressure(doc.getInt("bloodpressure"));
                                hospital.setHospital_visit_temperature(doc.getInt("temperature"));
                                hospital.setHospital_visit_diagnosis(doc.getString("diagnosis"));
                                hospital.setHospital_visit_treatment(doc.getString("treatment"));
                                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");

                                Calendar cal1 = Calendar.getInstance();
                                cal1.add(Calendar.DAY_OF_WEEK, 7);
                                final Date date1=cal1.getTime();

                                Date parsedDate = new Date();
                                try {
                                  parsedDate = formatter.parse(doc.getString("created_at"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                hospital.setHospital_visit_date(parsedDate);
                                mdataStore.insert(hospital);
                                Log.d("User12345", String.valueOf(hospital.getHospital_visit_id()));
                                Reminder reminder=new Reminder();
                                reminder.setHospital_visit_id(hospital.getHospital_visit_id());
                                reminder.setReminder_date(date1);
                                mdataStore.insert(reminder);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                Log.d("User123", String.valueOf(error));
            }
        }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    //////////////////GLUCOSE///////////////////////////////////////////////////////
            RequestQueue Gqueue = Volley.newRequestQueue(getApplicationContext());
            Log.d("User123", String.valueOf(user.getUser_UID()));
            final String Gurl = AppConfig.GET_GLUCOSE+user.getUser_UID();
            Log.d("User123", url);
            JsonArrayRequest GjsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Gurl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject doc = null;
                                try {
                                    doc = response.getJSONObject(i);
                                    Log.d("User123",doc.toString());
                                    Glucose glucose=new Glucose();

                                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
                                    Date parsedDate = new Date();
                                    try {
                                        parsedDate = formatter.parse(doc.getString("created_at"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    glucose.setGlucose_level(doc.getInt("glucose_level"));
                                    glucose.setGlucose_date(parsedDate);
                                    mdataStore.insert(glucose);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("User123", String.valueOf(error));
                }
            }

            );

            RequestQueue GrequestQueue = Volley.newRequestQueue(getApplicationContext());
            GrequestQueue.add(GjsonArrayRequest);
/////////////////////////////////////Pressure/////////////////////////////////////////////////////////
            RequestQueue Pqueue = Volley.newRequestQueue(getApplicationContext());
            Log.d("User123", String.valueOf(user.getUser_UID()));
            final String Purl = AppConfig.GET_PRESSURE+user.getUser_UID();
            Log.d("User123", url);
            JsonArrayRequest PjsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Purl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject doc = null;
                                try {
                                    doc = response.getJSONObject(i);
                                    Log.d("User123",doc.toString());
                                    Pressure pressure=new Pressure();

                                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
                                    Date parsedDate = new Date();
                                    try {
                                        parsedDate = formatter.parse(doc.getString("created_at"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    pressure.setPressure_date(parsedDate);
                                    pressure.setPressure_diastolic(doc.getInt("pressure_diastolic"));
                                    pressure.setPressure_systolic(doc.getInt("pressure_systolic"));
                                    pressure.setPressure_pulse(doc.getInt("pressure_pulse"));

                                    mdataStore.insert(pressure);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("User123", String.valueOf(error));
                }
            }

            );

            RequestQueue PrequestQueue = Volley.newRequestQueue(getApplicationContext());
            PrequestQueue.add(PjsonArrayRequest);
//////////////////////////////////////////////
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

}

//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.util.Log;
//import android.view.View;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.example.adil.checkup.Other.CircleTransform;
//import com.example.adil.checkup.R;
//
//public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//            Log.d("response","camers");
//        } else if (id == R.id.nav_gallery) {
//            Log.d("response","gallery");
//        } else if (id == R.id.nav_slideshow) {
//            Log.d("response","slider");
//        } else if (id == R.id.nav_manage) {
//            Log.d("response",",anager");
//        } else if (id == R.id.nav_share) {
//            Log.d("response","shaere");
//        } else if (id == R.id.nav_send) {
//            Log.d("response","gsend");
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


