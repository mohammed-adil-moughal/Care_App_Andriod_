package com.example.adil.checkup.models;

import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 5/17/17.
 */

@Entity
public abstract class AbstractTimer {
    @Key
    @Generated
    public int timer_id;
    @Nullable
    public int medicine_id;
    @Nullable
    public Date timer_time;

    @Nullable
    public Boolean Monday = false;
    @Nullable
    public Boolean Tuesday = false;
    @Nullable
    public Boolean Wednesday = false;
    @Nullable
    public Boolean Thursday = false;
    @Nullable
    public Boolean Friday = false;
    @Nullable
    public Boolean Saturday = false;
    @Nullable
    public Boolean Sunday = false;

}
