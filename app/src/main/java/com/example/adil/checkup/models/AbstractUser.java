package com.example.adil.checkup.models;

import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 7/3/17.
 */

@Entity
public abstract class AbstractUser  {
    @Key
    @Generated
    public int user_id;
    @Nullable
    public String user_name;
    @Nullable
    public String user_email;
    @Nullable
    public int user_UID;
    @Nullable
    public int doctor_id;
    @Nullable
    public String user_password;
    @Nullable
    public String doctor_name;

    @Nullable
    public String gender;
    @Nullable
    public String national_id;

}
