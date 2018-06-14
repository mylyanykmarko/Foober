package com.example.mylyanyk.foober;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Order {

    @PrimaryKey(autoGenerate = true)
    long id;

    String wish_list;
    String orderer;
    double lat;
    double lng;

    Order(String wish_list, String orderer, double lat, double lng) {
        this.wish_list = wish_list;
        this.orderer = orderer;
        this.lat = lat;
        this.lng = lng;
    }
}
