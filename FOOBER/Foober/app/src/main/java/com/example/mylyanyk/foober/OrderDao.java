package com.example.mylyanyk.foober;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM `order`")
    Flowable<List<Order>> getAll();

    @Query("SELECT * FROM `order` WHERE id = :id")
    Flowable<List<Order> > getById(long id);

    @Insert
    void insert(Order ord);

    @Update
    void update(Order ord);

    @Delete
    void delete(Order ord);
}
