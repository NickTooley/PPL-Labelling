package com.ppl.nickj.pplqr.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface ProductDAO {

    @Query("SELECT * FROM Product WHERE code = :code")
    Product find(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(Product product);
}
