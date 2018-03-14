package com.example.adil.checkup.models;

import java.sql.Time;
import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 5/17/17.
 */

@Entity
public abstract class AbstractReminder {
    @Key
    @Generated
    public int Reminder_id;
    @Nullable
    public int Hospital_visit_id;
    @Nullable
    public Date Reminder_date;
    @Nullable
    public Time Reminder_timer;




}
