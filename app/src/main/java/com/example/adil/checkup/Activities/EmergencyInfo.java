package com.example.adil.checkup.Activities;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Doctors;
import com.example.adil.checkup.models.Emergency;
import com.example.adil.checkup.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class EmergencyInfo extends AppCompatActivity {
    public static  String doctorname;
    public static  String doctorcontact;
    public  Emergency emergency;
    @Override
    protected void onStart()
    {
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();

        final User user=dataStore.select(User.class).get().first();
        StringRequest dr = new StringRequest(Request.Method.GET, AppConfig.DOC_URL+user.getDoctor_id(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("emergency123",response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            Log.d("emergency123", obj.getString("name"));
                            doctorname=obj.getString("name");
                            doctorcontact=obj.getString("phone_number");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(dr);
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);

        getSupportActionBar().setTitle(R.string.emergency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> dataStore = app.getDataStore();

        final User user=dataStore.select(User.class).get().first();
        if(user.getDoctor_id() == 0)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(EmergencyInfo.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Select a Doctor To Use this Section of the Application");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i=new Intent(EmergencyInfo.this,Profile.class);
                            startActivity(i);
                        }
                    });
            alertDialog.show();
        }

        final Emergency emergency1=dataStore.select(Emergency.class).get().firstOrNull();
        if(emergency1 == null)
        {
            emergency=new Emergency();
        }
        else
        {
            emergency=dataStore.select(Emergency.class).get().firstOrNull();
        }
        final int id=user.getDoctor_id();
        Log.d("emergency123",  AppConfig.DOC_URL+id);


        Button save= (Button) findViewById(R.id.buttonsave);
        final EditText chronic_illness= (EditText) findViewById(R.id.editTextChronic);
        final EditText  allergie= (EditText) findViewById(R.id.editTextAllergies);
        final EditText next_of_kin= (EditText) findViewById(R.id.editTextNextofkin);
        final EditText next_of_kin_contact= (EditText) findViewById(R.id.editTextNextofKinContact);
        if(emergency1!=null) {
            chronic_illness.setText(emergency.getChronic_illness());
            allergie.setText(emergency.getAllergies());
            next_of_kin.setText(emergency.getNext_of_kin());
            next_of_kin_contact.setText(emergency.getNext_of_kin_contact());
        }
        TextView name= (TextView) findViewById(R.id.textViewName);
        TextView doc= (TextView) findViewById(R.id.textViewDoc);
        TextView doccontact= (TextView) findViewById(R.id.textViewDocContact);
        name.setText(user.getUser_name());
        doc.setText(doctorname);
        doccontact.setText(doctorcontact);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emergency.setAllergies(allergie.getText().toString());
                emergency.setChronic_illness(chronic_illness.getText().toString());
                emergency.setNext_of_kin(next_of_kin.getText().toString());
                emergency.setNext_of_kin_contact(next_of_kin_contact.getText().toString());
                emergency.setDoctor_name(doctorname);
                emergency.setDoctor_contact(doctorcontact);

                if(emergency1 == null)
                {
                    dataStore.insert(emergency);
                }
                else {
                    dataStore.update(emergency);
                }
                sendemergencyinfo(String.valueOf(user.getUser_UID()),chronic_illness.getText().toString(),next_of_kin.getText().toString(),next_of_kin_contact.getText().toString(),allergie.getText().toString());
                Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.ux); // the original file yourimage.jpg i added in resources
                Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

                String yourText = user.getUser_name();
                String doctorname=emergency.getDoctor_name();
                String doctorcontact=emergency.getDoctor_contact();
                String chronicillness=chronic_illness.getText().toString();
                String allergies=allergie.getText().toString();
                String nextofkin=next_of_kin.getText().toString();
                String numbernextofkin=next_of_kin_contact.getText().toString();

                Canvas cs = new Canvas(dest);
                Paint tPaint = new Paint();
                Paint sPaint=new Paint();
                sPaint.setTextSize(45);
                sPaint.setColor(Color.BLACK);
                sPaint.setStyle(Paint.Style.FILL);
                Paint cPaint=new Paint();
                cPaint.setColor(Color.WHITE);

                tPaint.setTextSize(55);
                tPaint.setColor(Color.BLACK);
                tPaint.setStyle(Paint.Style.FILL);

                cs.drawBitmap(src, 0f, 0f, sPaint);
                final RectF rect = new RectF();
                float height = (src.getHeight()-1300f);
                float width = tPaint.measureText(yourText);
                float x_coord = (src.getWidth()-900f)/2;

                cs.drawText("Name:", x_coord, height+25f, tPaint);
                cs.drawText(yourText, x_coord+170f, height+25f, sPaint);
                cs.drawText("Doctor Name:", x_coord, height+135f, tPaint);// 15f is to put space between top edge and the text, if you want to change it, you can
                cs.drawText(doctorname, x_coord+400f, height+135f, sPaint);
                cs.drawText("Doctor Contact:", x_coord, height+245f, tPaint);
                cs.drawText(doctorcontact, x_coord+400f, height+245f, sPaint);
                cs.drawText("Chronic Illness:", x_coord, height+355f, tPaint);
                cs.drawText(chronicillness, x_coord+400f, height+355f, sPaint);
                cs.drawText("Allergies:", x_coord, height+465f, tPaint);
                cs.drawText(allergies, x_coord+250f, height+465f, sPaint);
                cs.drawText("Next of Kin:", x_coord, height+585f, tPaint);
                cs.drawText(nextofkin, x_coord+300f, height+585f, sPaint);
                cs.drawText("Contact:", x_coord, height+695f, tPaint);
                cs.drawText(numbernextofkin, x_coord+250f, height+695f, sPaint);
                try {String time=String.valueOf(System.currentTimeMillis());
                    dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+"screensaver"+".jpg")));
                    // dest is Bitmap, if you want to preview the final image, you can display it on screen also before saving
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                WallpaperManager myWallpaperManager= WallpaperManager.getInstance(getApplicationContext());
                Bitmap o = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+"screensaver"+".jpg");

                try {
                    myWallpaperManager.setBitmap(o);
                    Toast.makeText(getApplicationContext(),"Information Saved",Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(),"ScreenSaver Updated",Toast.LENGTH_LONG).show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void sendemergencyinfo(final String patient_id,final String chronic_illness, final String next_of_kin, final String next_of_kin_contact,final String allergies){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.POST_EMERGENCY_INFO,
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
                        Toast.makeText(EmergencyInfo.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_PATIENTID, patient_id);
                params.put(AppConfig.KEY_CHRONIC_ILLNESS, chronic_illness);
                params.put(AppConfig.KEY_NEXT_0F_KIN, next_of_kin);
                params.put(AppConfig.KEY_NEXT_OF_KIN_CONTACT, next_of_kin_contact);
                params.put(AppConfig.KEY_ALLERGIES, allergies);

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
            Window window = EmergencyInfo.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            LayoutInflater info = LayoutInflater.from(EmergencyInfo.this);

            View promptsinfo = info.inflate(R.layout.emergencyinfo, null);
//            promptsinfo.setMinimumWidth((int)(displayRectangle.width() ));
//            promptsinfo.setMinimumHeight((int)(displayRectangle.height()));
            android.app.AlertDialog.Builder alertDialogBuilderinfo = new android.app.AlertDialog.Builder(
                    EmergencyInfo.this);

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
            android.app.AlertDialog alertDialoginfo = alertDialogBuilderinfo.create();

            // show it
            alertDialoginfo.show();


            //return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
