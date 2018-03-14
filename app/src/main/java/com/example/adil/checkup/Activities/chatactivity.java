package com.example.adil.checkup.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.ChatAdapter;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Doctors;
import com.example.adil.checkup.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class chatactivity extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    ChatAdapter adapter;

    public List<String> values = new ArrayList<String>();
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
            mdataStore.delete(Chat.class).get().value();
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
        setContentView(R.layout.activity_chatactivity);
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();
        final User user=dataStore.select(User.class).get().first();
        if(user.getDoctor_id() == 0)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(chatactivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Select a Doctor To Use this Section of the Application");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i=new Intent(chatactivity.this,Profile.class);
                            startActivity(i);
                        }
                    });
            alertDialog.show();
        }
        RequestQueue queue = Volley.newRequestQueue(chatactivity.this);
        Log.d("User", String.valueOf(user.getUser_UID()));
        final String url = AppConfig.GET_CHAT+user.getUser_UID();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("json", String.valueOf(response));
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject doc = null;
                            try {
                                doc = response.getJSONObject(i);
                                Log.d("jsonuser",doc.toString());
                                Chat chat=new Chat();
                                chat.setServer_id(doc.getString("id"));
                                chat.setSender_id(doc.getString("sender_id"));
                                chat.setReceiver_id(doc.getString("receiver_id"));
                                chat.setMessages(doc.getString("message"));
                                chat.setCreated_at(doc.getString("created_at"));
                                dataStore.insert(chat);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                Log.d("tagg", String.valueOf(error));
            }
        }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        final List<Chat>all=dataStore.select(Chat.class).get().toList();
        Log.d("jssonchat",all.toString());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setRecyclerViewLayoutManager(chatactivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        mAdapter = new ChatAdapter(getApplicationContext(), new ArrayList(all));

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        final EditText message= (EditText) findViewById(R.id.editTextmessage);

        ImageView send= (ImageView) findViewById(R.id.buttonsendmessage);
        adapter = ((ChatAdapter)mRecyclerView.getAdapter());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messagesend=message.getText().toString();
                message.setText(null);
                Chat chat=new Chat();
                chat.setMessages(messagesend);
                chat.setSender_id(String.valueOf(user.getUser_UID()));
                chat.setReceiver_id(String.valueOf(user.getDoctor_id()));
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                chat.setCreated_at(timeStamp);
//                dataStore.insert(chat);
//                adapter.getMeds().add(chat);
//                mRecyclerView.getAdapter().notifyDataSetChanged();
                postchat(messagesend,String.valueOf(user.getUser_UID()),String.valueOf(user.getDoctor_id()),timeStamp);
//                Log.d("jsonsend",messagesend);
//                Toast.makeText(chatactivity.this,messagesend,Toast.LENGTH_LONG).show();

            }
        });


        syncChat();
    }
    public void setRecyclerViewLayoutManager(chatactivity.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getApplicationContext(), SPAN_COUNT);
                mCurrentLayoutManagerType = chatactivity.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mCurrentLayoutManagerType = chatactivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mCurrentLayoutManagerType = chatactivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        ((LinearLayoutManager)mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
    private static final int SPAN_COUNT = 2;
    public void postchat(final String message, final String sender_id, final String receiver_id, final String created_at)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.POST_CHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.d("Response",response);
                        Toast.makeText(chatactivity.this, response, Toast.LENGTH_LONG).show();
                  }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(chatactivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_MESSAGE,message);
                params.put(AppConfig.KEY_SENDERID, sender_id);
                params.put(AppConfig.KEY_RECEIVERID, receiver_id);
                params.put(AppConfig.KEY_CREATEDAT,created_at);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(chatactivity.this);
        requestQueue.add(stringRequest);
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
    }

    private void syncChat()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {CheckUp app = ((CheckUp) getApplicationContext());
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final EntityDataStore<Persistable> dataStore = app.getDataStore();
                    final User user=dataStore.select(User.class).get().first();

                    RequestQueue queue = Volley.newRequestQueue(chatactivity.this);
                    List s = dataStore.select(Chat.class).orderBy(Chat.CREATED_AT.desc()).get().toList();
                    String temp_url;
                    try{
                        String created_at = new ArrayList<Chat>(s).get(0).getCreated_at();
                        Log.d("User", String.valueOf(user.getUser_UID()));
                        temp_url = URLEncoder.encode(created_at);
                        temp_url = AppConfig.GET_CHAT+user.getUser_UID()+"?created_at="+ temp_url;
                    }
                    catch (Exception e){
                        temp_url = AppConfig.GET_CHAT+user.getUser_UID();
                    }
                    final String url = temp_url;
                    RequestFuture<JSONArray> future = RequestFuture.newFuture();
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, future, future);

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonArrayRequest);

                    JSONArray response = null;
                    while(response == null){
                        try {
                            response = future.get(30, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        Log.d("json_thread", String.valueOf(response));
                        int length = 0;
                        try{
                            length = response.length();
                        }
                        catch (Exception e){

                        }
                        for (int i = 0; i < length; i++) {

                            JSONObject doc = null;
                            try {
                                doc = response.getJSONObject(i);
                                Log.d("jsonuser",doc.toString());
                                final Chat chat=new Chat();
                                chat.setServer_id(doc.getString("id"));
                                chat.setSender_id(doc.getString("sender_id"));
                                chat.setReceiver_id(doc.getString("receiver_id"));
                                chat.setMessages(doc.getString("message"));
                                chat.setCreated_at(doc.getString("created_at"));
                                dataStore.insert(chat);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapter.getMeds().add(chat);
                                        int last_idx = adapter.getItemCount()-1;
                                        mRecyclerView.getAdapter().notifyItemInserted(last_idx);
                                        mRecyclerView.scrollToPosition(last_idx);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        };

        new Thread(runnable).start();


    }


}
