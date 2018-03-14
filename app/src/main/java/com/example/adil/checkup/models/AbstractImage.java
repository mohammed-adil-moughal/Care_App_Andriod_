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
public abstract class AbstractImage {
    @Key
    @Generated
    public int image_id;
    @Nullable
    public String image_name;
    @Nullable
    public String image_location;


}
