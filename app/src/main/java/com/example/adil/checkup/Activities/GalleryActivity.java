package com.example.adil.checkup.Activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Fragment.InfoFragment;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class GalleryActivity extends AppCompatActivity {
    Uri imageUri;
    Uri uriImagePath;
    public String sUsername;
    public ImageView imageView;
    public String string;
    public EditText editText;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    CheckUp app;
     EntityDataStore<Persistable> mdataStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.take_a_photo);
         app = ((CheckUp) getApplicationContext());
         mdataStore = app.getDataStore();
        editText = (EditText) findViewById(R.id.edit);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnCapture);
        imageView= (ImageView) findViewById(R.id.imageView);

        sUsername = editText.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup", String.valueOf(System.currentTimeMillis()) +
//                            ".jpg"));
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
//                    File folder = new File(Environment.getExternalStorageDirectory() + "/Checkup");
//                    folder.mkdirs();
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


                }
            }
        });

    }
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            Uri pickedImage =data.getData();
            String [] filePath={MediaStore.Images.Media.DATA};
            Cursor cursor =getContentResolver().query(pickedImage,filePath,null,null,null);
            cursor.moveToFirst();

            String imagePath=cursor.getString(cursor.getColumnIndex(filePath[0]));
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            cursor.close();
            String time=String.valueOf(System.currentTimeMillis());

            Image image=new Image();
            image.setImage_name(editText.getText().toString());
            image.setImage_location(time);
            mdataStore.insert(image);


            File destFile = new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+time+".jpg");

            Toast.makeText(getApplicationContext(), destFile.toString(),Toast.LENGTH_LONG).show();
//            destFile.createNewFile();
            if(!destFile.exists())
            {

                try {

                    destFile.createNewFile();
                copyFile(new File(imagePath),destFile);

            }
                catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("error",e.toString());
            }

            }



           finish();
        }

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
