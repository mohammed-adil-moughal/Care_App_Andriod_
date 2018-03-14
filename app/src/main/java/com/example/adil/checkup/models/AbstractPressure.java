package com.example.adil.checkup.models;

import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 6/21/17.
 */
@Entity
public class AbstractPressure {

    @Key
    @Generated
    public int pressure_id;
    @Nullable
    public int pressure_diastolic;
    @Nullable
    public int pressure_systolic;
    @Nullable
    public int pressure_pulse;
    @Nullable
    public Date pressure_date;
}
