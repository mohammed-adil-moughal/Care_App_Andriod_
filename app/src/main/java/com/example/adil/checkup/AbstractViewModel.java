package com.example.adil.checkup;

import android.content.Context;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import android.content.Context;
import android.widget.TextView;

import com.example.adil.checkup.CheckUp;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by savatia on 14/02/17.
 */

public class AbstractViewModel {
    protected Context mContext;
    protected EntityDataStore<Persistable> mDataStore;
    protected CheckUp mApplication;
    public AbstractViewModel(Context context)
    {
        mContext = context;
        mApplication = (CheckUp) context.getApplicationContext();
        mDataStore = mApplication.getDataStore();
    }

    public void addNew(){

    }
}
