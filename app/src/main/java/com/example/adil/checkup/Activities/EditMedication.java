package com.example.adil.checkup.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.ReminderAdapter;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.DatabaseHelper;
import com.example.adil.checkup.Fragment.Add_medication;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.meds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class EditMedication extends AppCompatActivity {
    public int value;
    private DatabaseHelper db;
    public Toolbar toolbar;
    public Button button;
    public int med_id;
    public Button addReminder;
    Uri imageUri;
    public ImageView imageView;
    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    public Medicine med;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    public EntityDataStore<Persistable> dataStore;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        CheckUp app = ((CheckUp) getApplicationContext());
         dataStore = app.getDataStore();


//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Medication");
        button = (Button) findViewById(R.id.buttonupdate);
        addReminder = (Button) findViewById(R.id.textviewaddReminder);
        imageView= (ImageView) findViewById(R.id.imageView);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }
        Log.d("Edity", String.valueOf(value));

        med = dataStore.select(Medicine.class).where(Medicine.MEDICINE_ID.eq(value)).get().first();

//        db = new DatabaseHelper(getApplicationContext());
        TextView editname = (TextView) findViewById(R.id.textname);
        final EditText editdescription = (EditText) findViewById(R.id.textdescription);
        final EditText editdosage = (EditText) findViewById(R.id.textdosage);
        final EditText editwhatfor = (EditText) findViewById(R.id.textwhatfor);


        try {
            Log.d("id", String.valueOf(value));

//    HashMap<String, String> medication = db.getMedDetails(value);

            String name = med.getMedicine_name();
            String description = med.getMedicine_description();
            String whatfor = med.getMedicine_whatfor();
            String dosage = med.getMedicine_dosage();
            String imagename=med.getMedicine_image();
            med_id = med.getMedicine_id();


            editname.setText(name);
            editdescription.setText(description);
            editdosage.setText(dosage);
            editwhatfor.setText(whatfor);

            Log.d("name of drug", med.getMedicine_name());

            File imgFile =    new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+imagename+".jpg");

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.imageView);

                myImage.setImageBitmap(myBitmap);

            }

        } catch (Exception e) {
            Log.d("error", e.toString());
        }

        Log.d("INTENT STRING", String.valueOf(value));
//        TextView MedId= (TextView) findViewById(R.id.textViewmedicationID);
//        MedId.setText(s);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                med.setMedicine_description(editdescription.getText().toString());
                med.setMedicine_dosage(editdosage.getText().toString());
                med.setMedicine_whatfor(editwhatfor.getText().toString());

                dataStore.update(med);
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
            }
        });

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer tim = new Timer();
                tim.setMedicine_id(med.getMedicine_id());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 17);
                cal.set(Calendar.MINUTE, 30);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date d = cal.getTime();
                tim.setTimer_time(d);

                Date d1=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d1);
                Log.d("date", currentDateTimeString);
                dataStore.insert(tim);
                ReminderAdapter adapter = ((ReminderAdapter)mRecyclerView.getAdapter());
                adapter.getMeds().add(tim);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        // Set CustomAdapter as the adapter for RecyclerView.
        List<Timer> all = dataStore.select(Timer.class).where(Timer.MEDICINE_ID.eq(med.getMedicine_id())).get().toList();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);

        mAdapter = new ReminderAdapter(getApplicationContext(), new ArrayList(all));

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_med_menu, menu);
        return true;
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
//                return true;      final EditText taskEditText = new EditText(this);


            case R.id.action_camera:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Take Image")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(getApplication(),editText.getText().toString(),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                String time=String.valueOf(System.currentTimeMillis());
//                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup",editText.getText().toString() +
//                                String.valueOf(System.currentTimeMillis()) + ".jpg"));
                                med.setMedicine_image(time);
                                dataStore.update(med);
                                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup",  time+
                                        ".jpg"));
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                File folder = new File(Environment.getExternalStorageDirectory() + "/Checkup");
                                folder.mkdirs();
                                startActivityForResult(intent,1);
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "Checkup", String.valueOf(System.currentTimeMillis()) +
//                            ".jpg"));
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
//                    File folder = new File(Environment.getExternalStorageDirectory() + "/Checkup");
//                    folder.mkdirs();
                                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                            }
                        })
                        .create();
                dialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

            med.setMedicine_image(time);
            dataStore.update(med);
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
        else if(requestCode == 1)
        {

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

}

