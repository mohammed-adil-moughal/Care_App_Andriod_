package com.example.adil.checkup.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adil.checkup.Adapters.ReminderAdapter;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CamTestActivity";
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    Uri imageUri;
    String sUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        CheckUp app = ((CheckUp) getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        final EditText editText= (EditText) findViewById(R.id.edit);
        FloatingActionButton button= (FloatingActionButton) findViewById(R.id.btnCapture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.take_a_photo);
        sUsername = editText.getText().toString();

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    {
                        Image image=new Image();
                        image.setImage_location(editText.getText().toString());
                        image.setImage_name(editText.getText().toString());
                        mdataStore.insert(image);

//                        Toast.makeText(getApplication(),editText.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup",editText.getText().toString() +
//                                String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup",editText.getText().toString() +
                                 ".jpg"));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                        File folder = new File(Environment.getExternalStorageDirectory() + "/Checkup");
                        folder.mkdirs();
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                }
            });




    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
//            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//                //use imageUri here to access the image
//
//                Bundle extras = data.getExtras();
//
//                Log.e("URI",imageUri.toString());
//
//                Bitmap bmp = (Bitmap) extras.get("data");
//
//                // here you will get the image as bitmap
//
//
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
//            }

        }


      finish();
    }
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