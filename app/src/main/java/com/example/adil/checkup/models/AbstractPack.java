package com.example.adil.checkup.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Nullable;
import io.requery.Table;

/**
 * Created by adil on 5/5/17.
 */
@Entity
public abstract class AbstractPack {
    @Key
    @Generated
    public int pack_id;
    @Nullable
    public String name;
    @Nullable
    public int pack_size;
}
