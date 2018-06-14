package com.example.mylyanyk.foober;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OrderCurrentFragment extends Fragment {

    ListView lvMain;
    ArrayAdapter<String> adapter;
    List<String> orders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.current_orders, container, false);
        lvMain = (ListView) v.findViewById(R.id.ordersLV);
        orders = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, orders);

        AppDataBase db = App.getInstance().getDatabase();
        db.orderDao().getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<List<Order>>() {
                    @Override
                    public void accept(List<Order> orders1) throws Exception {
                        orders.clear();
                        for (Order order : orders1) {
                            orders.add(order.orderer + " - " + order.wish_list);
                        }
                        adapter.notifyDataSetChanged();
//                                   adapter.notifyItemInserted(bookCards.size() - 1);
                    }
                });

        lvMain.setAdapter(adapter);
        return v;
    }
}
