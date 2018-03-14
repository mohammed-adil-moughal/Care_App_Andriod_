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
public abstract class AbstractDoctors {
    @Key
    @Generated
    public int entry_id;
    @Nullable
    public String doctor_name;
    @Nullable
    public int doctor_id;


}
