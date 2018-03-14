package com.example.adil.checkup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.AppController;
import com.example.adil.checkup.SQLiteHandler;
import com.example.adil.checkup.SessionManager;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Emergency;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Pressure;
import com.example.adil.checkup.models.Reminder;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.User;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputNationalId;
    private EditText inputPassword;
    private EditText inputCPassword;
    private EditText inputphone;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Spinner spinner1;
    public String gender;
    private EntityDataStore<Persistable> dataStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputNationalId= (EditText) findViewById(R.id.national_id);
        inputPassword = (EditText) findViewById(R.id.password);
        inputCPassword = (EditText) findViewById(R.id.Cpassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        inputphone= (EditText) findViewById(R.id.phone_number);
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {
     gender="male";

                }
                else if(position == 1)
                {

                    gender="female";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String Cpassword = inputCPassword.getText().toString().trim();
                String national_id=inputNationalId.getText().toString().trim();
                String phoneNumber=inputphone.getText().toString();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !national_id.isEmpty() && !phoneNumber.isEmpty()) {
                    pDialog.setMessage("Registering ...");
                    showDialog();


                registerUser(name, email, password,Cpassword,gender,national_id,phoneNumber);


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void registerUser(final String username, final String email,
                              final String password,final String Cpassword,final String gender,final String national_id,final String phone_number){
        CheckUp app =   ((CheckUp)getApplicationContext());
        EntityDataStore<Persistable> dataStore1 = app.getDataStore();
        dataStore1.delete(User.class).get().value();
        dataStore1.delete(Chat.class).get().value();
        dataStore1.delete(Emergency.class).get().value();
        dataStore1.delete(Medicine.class).get().value();
        dataStore1.delete(Timer.class).get().value();
        dataStore1.delete(Glucose.class).get().value();
        dataStore1.delete(Pressure.class).get().value();
        dataStore1.delete(Reminder.class).get().value();
        dataStore1.delete(Hospital.class).get().value();

        if( password.equals(Cpassword))
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                            Log.d("Response",response);

                            hideDialog();


                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("json",jsonObject.toString());
                                if(jsonObject.getString("error").equals("True"))
                                {
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }
                                else if(jsonObject.getString("error").equals("False"))
                                {
                                    CheckUp app = ((CheckUp) getApplicationContext());
                                    final EntityDataStore<Persistable> mdataStore = app.getDataStore();

                                    User user=new User();
                                    user.setUser_email(email);
                                    user.setUser_name(username);
                                    user.setUser_password(password);
                                    user.setNational_id(national_id);
                                    user.setGender(gender);
                                    user.setUser_UID(Integer.parseInt(jsonObject.getString("id")));
                                    mdataStore.insert(user);

                                    session.setLogin(true);
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    Intent login=new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(login);


                                }



                            }
                            catch (Exception e){
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(AppConfig.KEY_USERNAME, username);
                    params.put(AppConfig.KEY_PASSWORD, password);
                    params.put(AppConfig.KEY_PASWORD2, Cpassword);
                    params.put(AppConfig.KEY_EMAIL, email);
                    params.put(AppConfig.KEY_GENDER, gender);
                    params.put(AppConfig.KEY_NATIONALID, national_id);
                    params.put(AppConfig.KEY_PHONENUMBER,phone_number);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        else {

            Toast.makeText(this,"Passwords Dont Match",Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
