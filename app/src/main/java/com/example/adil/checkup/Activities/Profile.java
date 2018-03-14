package com.example.adil.checkup.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDoneException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.DoctorAdapter;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.RegisterActivity;
import com.example.adil.checkup.models.Doctors;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class Profile extends AppCompatActivity {

    public List<String> spinnerArray = new ArrayList<String>();
    public EditText Doctor;

    public List<User>doctor;

    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;

    private static final int SPAN_COUNT = 2;

    public
    List<String> values = new ArrayList<String>();
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    @Override
    protected void onStart()
    {
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        try {
            mdataStore.delete(Doctors.class).get().value();
        }
        catch (Exception e)
        {
            Log.d("DELETE",e.toString());
        }
        // TODO Auto-generated method stub
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_profile);
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();

        final User user = mdataStore.select(User.class).get().firstOrNull();



        final EditText Name = (EditText) findViewById(R.id.editName);
        final EditText Email = (EditText) findViewById(R.id.editEmail);
        final EditText Password = (EditText) findViewById(R.id.editPassword);
        Doctor= (EditText) findViewById(R.id.editDoctor);
        Name.setText(user.getUser_name());
        Email.setText(user.getUser_email());
        Password.setText(user.getUser_password());
       ;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AppConfig.DOCTOR_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("json response", String.valueOf(response));

                  try {
                            Log.d("json response", String.valueOf(response));
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject doc = response.getJSONObject(i);
                                values.add(doc.getString("name"));
                                int id1= Integer.parseInt(doc.getString("id"));
                                String name=doc.getString("name");
                                Doctors doctor=new Doctors();
                                doctor.setDoctor_name(doc.getString("name"));
                                doctor.setDoctor_id(Integer.parseInt(doc.getString("id")));
                                mdataStore.insert(doctor);
                            //    values.set(doc.getInt("id"),doc.getString("name"));
                                  Log.d("Stru", doc.getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                Log.d("tagg", String.valueOf(error));
            }
        }

        ) {
//            @Override
//            protected Map<String, String> getParams() {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put(AppConfig.KEY_EMAIL, email);
////                params.put(AppConfig.KEY_PASWORD2, password);
////
////                return params;
//                return ;
//            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
        Doctors select=mdataStore.select(Doctors.class).where(Doctors.DOCTOR_ID.eq(user.getDoctor_id())).get().firstOrNull();
//Doctor.setText(String.valueOf(select.getDoctor_name()));
        final List<Doctors>all=mdataStore.select(Doctors.class).get().toList();
//        Log.d("doctors", String.valueOf(all));
//        Log.d("USer",String.valueOf(user.getDoctor_id())+String.valueOf(select.getDoctor_name()));
        Doctor.setText(String.valueOf(user.getDoctor_name()));

        Doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Profile.this);
                dialog.setContentView(R.layout.doclist);
                dialog.setTitle("Select Doctor");
                // Initialize a new RequestQueue instance
                mAdapter = new DoctorAdapter(getApplicationContext(), new ArrayList(all));

               mRecyclerView = (RecyclerView) dialog.findViewById(R.id.list);

                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);

                mRecyclerView.setAdapter(mAdapter);
                dialog.show();

//                DoctorAdapter doctorAdapter = new DoctorAdapter(doctor);
//                list.setAdapter(adapter);

//                mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        int itemPosition = position;
//
//                        String itemValue = (String) listView.getItemAtPosition(position);
//                        Doctor.setText(itemValue);
//                        // Show Alert
//                        Toast.makeText(
//                                getApplicationContext(),
//                                "Position :" + itemPosition + "  ListItem : "
//                                        + itemValue +"id :"+id, Toast.LENGTH_LONG).show();
//                        dialog.cancel();
//
//                    }
//
//                });
            }

        });

        final Button save = (Button) findViewById(R.id.profilesave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUser_password(Password.getText().toString());
                user.setUser_email(Email.getText().toString());
                user.setUser_name(Name.getText().toString());
                mdataStore.update(user);

                final String userid=String.valueOf(user.getUser_UID());
                final String userdocid=String.valueOf(user.getDoctor_id());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.U_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                                Log.d("Response",response);
                                Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
//
//                                try
//                                {
//                                    JSONObject jsonObject = new JSONObject(response);
//
//                                    if(jsonObject.getString("error").equals("True"))
//                                    {
//                                        Toast.makeText(Profile.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                                    }
//                                    else if(jsonObject.getString("error").equals("False"))
//                                    {
//
//                                        Toast.makeText(Profile.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//
//
//                                    }
//
//
//
//                                }
//                                catch (Exception e){
//                                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();
//                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(AppConfig.KEY_USERNAME,Name.getText().toString());
                        params.put(AppConfig.KEY_PASSWORD, Password.getText().toString());
                        params.put(AppConfig.KEY_EMAIL, Email.getText().toString());
                        params.put(AppConfig.KEY_USER,userid);
                        params.put(AppConfig.KEY_USER_DOC,userdocid);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
                requestQueue.add(stringRequest);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                       10000,2,2.0f));
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();

            }
        });

//


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
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back Pressed",Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;
//                if(getSupportFragmentManager().getBackStackEntryCount()>0)
//                    getSupportFragmentManager().popBackStack();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}