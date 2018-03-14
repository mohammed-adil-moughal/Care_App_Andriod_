package com.example.adil.checkup.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;

/**
 * Created by adil on 5/6/17.
 */
@Entity
public abstract class AbstractMedicine {

    @Key
    @Generated
    public int medicine_id;
    @Nullable
    public String medicine_name;
    @Nullable
    public String medicine_dosage;
    @Nullable
    public String medicine_description;
    @Nullable
    public String medicine_whatfor;
    @Nullable
    public String medicine_image;
    @Nullable
    public int medicine_hospital_visit;
}
