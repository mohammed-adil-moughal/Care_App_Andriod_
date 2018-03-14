//package com.example.adil.checkup;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Request.Method;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.JsonRequest;
//import com.android.volley.toolbox.StringRequest;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//import com.android.volley.toolbox.Volley;
//import com.example.adil.checkup.AppConfig;
//import com.example.adil.checkup.AppController;
//import com.example.adil.checkup.SQLiteHandler;
//import com.example.adil.checkup.SessionManager;
//
//import com.example.adil.checkup.models.User;
//import com.google.android.gms.common.api.GoogleApiClient;
//
//import io.requery.Persistable;
//import io.requery.sql.EntityDataStore;
//
//public class LoginActivity extends Activity {
////    private static final String TAG = RegisterActivity.class.getSimpleName();
////    private Button btnLogin;
////    private Button btnLinkToRegister;
////    private EditText inputEmail;
////    private EditText inputPassword;
////    private ProgressDialog pDialog;
////    private SessionManager session;
////    private SQLiteHandler db;
////
////    private EntityDataStore<Persistable> dataStore;
////
////
////    /**
////     * ATTENTION: This was auto-generated to implement the App Indexing API.
////     * See https://g.co/AppIndexing/AndroidStudio for more information.
////     */
////    private GoogleApiClient client;
////
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_login);
////
////        inputEmail = (EditText) findViewById(R.id.email);
////        inputPassword = (EditText) findViewById(R.id.password);
////        btnLogin = (Button) findViewById(R.id.btnLogin);
////        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
////
////        // Progress dialog
////        pDialog = new ProgressDialog(this);
////        pDialog.setCancelable(false);
////
////        // SQLite database handler
////        db = new SQLiteHandler(getApplicationContext());
////
////        // Session manager
////        session = new SessionManager(getApplicationContext());
////
////        // Check if user is already logged in or not
////        if (!session.isLoggedIn()) {
//////            Toast.makeText(LoginActivity.this, "Your arent logged in", Toast.LENGTH_LONG).show();
////        } else {
////            Intent Login = new Intent(LoginActivity.this, MainActivity.class);
////            startActivity(Login);
////        }
////        // Login button Click Event
////        btnLogin.setOnClickListener(new View.OnClickListener() {
////
////            public void onClick(View view) {
////                String email = inputEmail.getText().toString().trim();
////                String password = inputPassword.getText().toString().trim();
////
////                // Check for empty data in the form
////                if (!email.isEmpty() && !password.isEmpty()) {
////                    // login user
////                    login2(email, password);
////
////                } else {
////                    // Prompt user to enter credentials
////                    Toast.makeText(getApplicationContext(),
////                            "Please enter the credentials!", Toast.LENGTH_LONG)
////                            .show();
////                }
////            }
////
////        });
////
////        // Link to Register Screen
////        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
////
////            public void onClick(View view) {
////                Intent i = new Intent(getApplicationContext(),
////                        RegisterActivity.class);
////                startActivity(i);
////                finish();
////            }
////        });
////
////        // ATTENTION: This was auto-generated to implement the App Indexing API.
////        // See https://g.co/AppIndexing/AndroidStudio for more information.
////    }
////
////    private void login2(final String email, final String password) {
////        pDialog.setMessage("Logging in ...");
////        showDialog();
////
////        StringRequest request = new StringRequest(Method.POST, AppConfig.LOGIN_URL, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                try {
////                    JSONObject jsonObject = new JSONObject(response);
////                    Log.d("check", jsonObject.getString("Error"));
////                    if (jsonObject.getString("Error").equals("False")) {
////                        Log.d("esponse", "loggin in");
////                        try {
////                            Log.d("Login_id", String.valueOf(jsonObject.get("id")));
////                            Log.d("Login_id_1", String.valueOf(jsonObject.get("name")));
////                            Log.d("Login_id_2", String.valueOf(jsonObject.get("password")));
////                            Log.d("Login_id_3", String.valueOf(jsonObject.get("email")));
////                          //  db.addUser(, , , );
////                            CheckUp app = ((CheckUp) getApplicationContext());
////                            final EntityDataStore<Persistable> mdataStore = app.getDataStore();
////
////                            User user=new User();
////                            user.setUser_email(String.valueOf(jsonObject.getString("email")));
////                            user.setUser_name(String.valueOf(jsonObject.getString("name")));
////                            user.setUser_password(String.valueOf(jsonObject.getString("password")));
////                            user.setUser_UID(Integer.parseInt(jsonObject.getString("id")));
////                            mdataStore.insert(user);
////                            Log.d("Login_id", "inserted");
////
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////
////                        session.setLogin(true);
////                        Intent login = new Intent(LoginActivity.this, MainActivity.class);
////                        startActivity(login);
////                    } else {
////                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
////                        hideDialog();
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                    hideDialog();
////                                Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
////                }
//////                Log.d("Response", response);
//////                hideDialog();
//////            Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
////
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                error.printStackTrace();
////
////            }
////        }) {
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put(AppConfig.KEY_EMAIL, email);
////                params.put(AppConfig.KEY_PASWORD2, password);
////
////                return params;
////            }
////        };
////        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
////        requestQueue.add(request);
////    }
////
////    private void login(final String email, final String password) {
////        pDialog.setMessage("Logging in ...");
////        showDialog();
////
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, AppConfig.LOGIN_URL,
////                new Response.Listener<JSONObject>() {
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        JSONObject jObj = response;
////                        Log.d("json response", response.toString());
////                        try {
////                            String name = jObj.getString("Name");
////                            Toast.makeText(LoginActivity.this, name, Toast.LENGTH_LONG).show();
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                            //Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_LONG).show();
////                        }
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                error.printStackTrace();
////                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
////            }
////        }
////
////        ) {
////            @Override
////            protected Map<String, String> getParams() {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put(AppConfig.KEY_EMAIL, email);
////                params.put(AppConfig.KEY_PASWORD2, password);
////
////                return params;
////            }
////
////        };
////
////        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
////        requestQueue.add(jsonObjectRequest);
////
////
////    }
////
////
////    private void showDialog() {
////        if (!pDialog.isShowing())
////            pDialog.show();
////    }
////
////    private void hideDialog() {
////        if (pDialog.isShowing())
////            pDialog.dismiss();
////    }
//
//
//}

package com.example.adil.checkup;;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.adil.checkup.Activities.IntroActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;

import android.app.ProgressDialog;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import com.android.volley.toolbox.Volley;

import com.example.adil.checkup.models.User;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    final String SiteKey = "6LdjBDAUAAAAAPl8Oxc2lpDy7Jr4qHlYFlSi1B4C";
    final String SecretKey  = "6LdjBDAUAAAAAN-XTLgio7TElfsrfMLIpCXJh0GS";
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private EntityDataStore<Persistable> dataStore;
    Button btnRequest;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

/////////////
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {
                   // Toast.makeText(getApplicationContext(),"first",Toast.LENGTH_LONG).show();
                    //  Launch app intro
                    final Intent i = new Intent(LoginActivity.this, IntroActivity.class);
                    Log.d("not1","yes");
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
                else
                {
                   // Toast.makeText(getApplicationContext(),"nah",Toast.LENGTH_LONG).show();
                    Log.d("not1","not");
                }
            }
        });

        // Start the thread
        thread.start();
///////////////
////
        session = new SessionManager(getApplicationContext());

        // SQLite database handler


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        Button lang= (Button) findViewById(R.id.lang);
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Language");
                alertDialog.setMessage("Select Language");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "English",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateViews("en");
                                dialog.dismiss();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Swahili",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateViews("sw");
                                dialog.dismiss();

                            }
                        });


                alertDialog.show();
            }
        });
        tvResult = (EditText)findViewById(R.id.email);
        btnRequest = (Button)findViewById(R.id.btnLogin);
        btnRequest.setOnClickListener(RqsOnClickListener);
        session = new SessionManager(getApplicationContext());
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        TextView imagebuttoncam= (TextView) findViewById(R.id.imageButtonCam);
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);


            }
        });
        imagebuttoncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, VerificationMenuActivity.class);
                intent.putExtra("Type",1);
                startActivity(intent);


            }
        });
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(LoginActivity.this)
                .addOnConnectionFailedListener(LoginActivity.this)
                .build();

        mGoogleApiClient.connect();
    }

    View.OnClickListener RqsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    login2(email, password);
//                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, SiteKey)
//                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
//                                @Override
//                                public void onResult(SafetyNetApi.RecaptchaTokenResult result) {
//                                    Status status = result.getStatus();
//
//                                    if ((status != null) && status.isSuccess()) {
//                                        Log.d("safetyemail",email.toString());
//                                        Log.d("safetypassword",password.toString());
//                                        login2(email, password);
////                                        if (!result.getTokenResult().isEmpty()) {
////                                            Log.d("safety","!result.getTokenResult().isEmpty()");
////
////                                            // User response token must be validated using the
////                                            // reCAPTCHA site verify API.
////                                        }else{
////                                            Log.d("safety","result.getTokenResult().isEmpty()");
////
////                                        }
//                                    } else {
//
//                                        Log.e("MY_APP_TAG", "Error occurred " +
//                                                "when communicating with the reCAPTCHA service.");
//                                        Log.d("safety",String.valueOf(status.getStatusCode())+"Error occurred " +
//                                                "when communicating with the reCAPTCHA service.");
//
//                                        // Use status.getStatusCode() to determine the exact
//                                        // error code. Use this code in conjunction with the
//                                        // information in the "Handling communication errors"
//                                        // section of this document to take appropriate action
//                                        // in your app.
//                                    }
//                                }
//                            });
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }


        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,
                "onConnectionSuspended: " + i,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "onConnectionFailed():\n" + connectionResult.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }

        private void login2(final String email, final String password) {
        pDialog.setMessage("Logging in ...");
            pDialog.show();

            Log.d("safetyesponse", "loggin in");
            Log.d("safetyesponse", email);
            Log.d("safetyesponse", password);
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("esponse", "loggin in1");
                Log.d("safetyesponse", email);
                Log.d("safetyesponse", password);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("check", jsonObject.getString("Error"));
                    if (jsonObject.getString("Error").equals("False")) {
                        Log.d("esponse", "loggin in");
                        try {
                            Log.d("Login_id", String.valueOf(jsonObject.get("id")));
                            Log.d("Login_id_1", String.valueOf(jsonObject.get("name")));
                            Log.d("Login_id_2", String.valueOf(jsonObject.get("password")));
                            Log.d("Login_id_3", String.valueOf(jsonObject.get("email")));
                          //  db.addUser(, , , );
                            CheckUp app = ((CheckUp) getApplicationContext());
                            final EntityDataStore<Persistable> mdataStore = app.getDataStore();

                            User user=new User();
                            user.setUser_email(String.valueOf(jsonObject.getString("email")));
                            user.setUser_name(String.valueOf(jsonObject.getString("name")));
                            user.setUser_password(String.valueOf(jsonObject.getString("password")));
                            user.setUser_UID(Integer.parseInt(jsonObject.getString("id")));
                            mdataStore.insert(user);
                            Log.d("Login_id", "inserted");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideDialog();
                        session.setLogin(true);
                        Intent login = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(login);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                                Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
                }
//                Log.d("Response", response);
//                hideDialog();
//            Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("safetyesponse", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.KEY_EMAIL, email);
                params.put(AppConfig.KEY_PASWORD2, password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_english) {
            updateViews("en");
        }
        if (id == R.id.action_swahili) {
            //return true;
            updateViews("sw");
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        btnLinkToRegister.setText(resources.getString(R.string.btn_link_to_register));
        btnLogin.setText(resources.getString(R.string.btn_login));
        inputEmail.setText(resources.getString(R.string.hint_email));
        inputPassword.setText(resources.getString(R.string.hint_password));

        setTitle(resources.getString(R.string.btn_login));
    }
}
