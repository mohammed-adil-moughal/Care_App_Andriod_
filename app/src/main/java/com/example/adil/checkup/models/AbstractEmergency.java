package com.example.adil.checkup.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 6/18/17.
 */
//username    a@gmail
    //password  123
@Entity
public abstract class AbstractEmergency {
    @Key
    @Generated
    public int id;
    @Nullable
    public String doctor_id;
    @Nullable
    public String doctor_name;
    @Nullable
    public String doctor_contact;
    @Nullable
    public String chronic_illness;
    @Nullable
    public String allergies;
    @Nullable
    public String next_of_kin;
    @Nullable
    public String next_of_kin_contact;
    @Nullable
    public String created_at;


}
