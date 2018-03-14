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
public abstract class AbstractChat {
    @Key
    @Generated
    public int entry_id;
    @Nullable
    public String server_id;
    @Nullable
    public String sender_id;
    @Nullable
    public String receiver_id;
    @Nullable
    public String messages;
    @Nullable
    public String synced;
    @Nullable
    public String created_at;


}
