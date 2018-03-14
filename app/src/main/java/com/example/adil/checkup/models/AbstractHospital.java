package com.example.adil.checkup.models;

import java.util.Date;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 7/8/17.
 */

@Entity
public abstract class AbstractHospital  {
    @Key
    @Generated
    public int hospital_visit_id;
    @Nullable
    public int hospital_visit_temperature;
    @Nullable
    public int hospital_visit_bloodPressure;
    @Nullable
    public int hospital_visit_weight;
    @Nullable
    public int hospital_visit_height_feet;
    @Nullable
    public int hospital_visit_height_inches;
    @Nullable
    public int hospital_visit_bmi;
    @Nullable
    public String hospital_visit_diagnosis;
    @Nullable
    public String hospital_visit_treatment;
    @Nullable
    public Date hospital_visit_date;


}