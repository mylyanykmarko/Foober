package com.example.mylyanyk.foober;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Order.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract OrderDao orderDao();
}
