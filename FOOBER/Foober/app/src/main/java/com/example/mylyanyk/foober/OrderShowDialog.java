package com.example.mylyanyk.foober;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class OrderShowDialog extends DialogFragment {

    TextView order_name;
    TextView wish_list;
    Button takeBtn;
    double lat;
    double lng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Integer order_id = getArguments().getInt("Id");
        View v = inflater.inflate(R.layout.order_show_dialog, container, false);
        order_name = (TextView) v.findViewById(R.id.orderer_name);
        wish_list = (TextView) v.findViewById(R.id.wish_list);
        takeBtn = (Button) v.findViewById(R.id.take_button);
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://franchukpetro.pythonanywhere.com/del_order/" + order_id;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppDataBase db = App.getInstance().getDatabase();
                        db.orderDao().insert(new Order(wish_list.getText().toString(), order_name.getText().toString(), lat, lng));
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
                dismiss();
            }
        });
        String url = "http://franchukpetro.pythonanywhere.com/get_order_full/" + order_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response = response.getJSONObject("order");
                    order_name.setText(response.getString("orderer"));
                    wish_list.setText(response.getString("wish_list"));
                    lat = response.getDouble("lat");
                    lng = response.getDouble("lng");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
        return v;
    }
}
