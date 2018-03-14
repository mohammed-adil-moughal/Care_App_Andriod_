package com.example.adil.checkup.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Image;

import java.io.File;
import java.util.ArrayList;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class ImageViewActivity extends AppCompatActivity {
    public  int value;
    File[] listFile;
    ArrayList<String> f = new ArrayList<String>();
    public ImageView imageView;
    public Image image;
    public EntityDataStore<Persistable> mdataStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getInt("Image_id");
            //The key argument here must match that used in the other activity
        }

        CheckUp app = ((CheckUp) getApplicationContext());
        mdataStore = app.getDataStore();
        image= mdataStore.select(Image.class).where(Image.IMAGE_ID.eq(value)).get().firstOrNull();


        File imgFile =    new File(Environment.getExternalStorageDirectory() + "/" + "Checkup"+"/"+image.getImage_location()+".jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);

            myImage.setImageBitmap(myBitmap);

        }
        //TextView textView= (TextView) findViewById(R.id.textView);
//        textView.setText(String.valueOf(image.getImage_name()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.valueOf(image.getImage_name()));

    }
    public void getFromSdcard()
    {
        File file= new File(android.os.Environment.getExternalStorageDirectory(),"MapleBear");

        if (file.isDirectory())
        {
            listFile = file.listFiles();


        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_menu, menu);
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
        if(id ==R.id.action_edit) {
            final EditText taskEditText = new EditText(this);
            taskEditText.setText(String.valueOf(image.getImage_name()));
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Edit Name")
                    .setView(taskEditText)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            image.setImage_name(taskEditText.getText().toString());
                            mdataStore.update(image);
                            Toast.makeText(ImageViewActivity.this,"Updated",Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
