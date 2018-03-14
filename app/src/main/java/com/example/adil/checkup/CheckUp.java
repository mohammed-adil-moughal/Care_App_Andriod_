package com.example.adil.checkup;

import android.app.Application;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.adil.checkup.models.Models;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

/**
 * Created by adil on 5/6/17.
 */
public class CheckUp extends Application {
    private static EntityDataStore<Persistable> mDataStore;
    private static final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    public Ringtone getRingtone() {
        if(ringtone==null)
            ringtone = RingtoneManager.getRingtone(this, uri);
        return ringtone;
    }

    private static Ringtone ringtone;

    public CheckUp() {
        getDataStore();
    }

    public EntityDataStore<Persistable> getDataStore() {
        if (mDataStore == null) {
            // override onUpgrade to handle migrating to a new version
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT , 31);
            if (BuildConfig.DEBUG) {
                // use this in development mode to drop and recreate the tables on eery upgrade
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            }
            Configuration configuration = source.getConfiguration();
            mDataStore =  new EntityDataStore<>(configuration);
        }
        return mDataStore;
    }
    private static FaceServiceClient sFaceServiceClient;

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sFaceServiceClient = new FaceServiceRestClient(getString(R.string.endpoint), getString(R.string.subscription_key));

    }

}
