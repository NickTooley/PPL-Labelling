package com.ppl.nickj.pplqr.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"code"},
        unique = true)})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String code;

    public String title;
    public String imageURL;

}
