package com.example.adil.checkup.models;

import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 6/18/17.
 */
@Entity
public abstract class AbstractGlucose  {
    @Key
    @Generated
    public int glucose_id;
    @Nullable
    public int glucose_level;
    @Nullable
    public Date glucose_date;


}
