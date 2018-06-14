package com.example.mylyanyk.foober;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class OrderDialog extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    Button btn;
    GoogleMap map = null;
    LatLng cur_lat_lng = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_dialog, container, false);
        btn = (Button) v.findViewById(R.id.add_button);
        final EditText orderer = (EditText) v.findViewById(R.id.orderer_name);
        final EditText wish_list_ = (EditText) v.findViewById(R.id.wish_list);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_name = orderer.getText().toString();
                String wish_list = wish_list_.getText().toString();
                if(order_name.length() == 0) {
                    Toast.makeText(getContext(), "You should write orderer's name", Toast.LENGTH_SHORT).show();
                } else if(wish_list.length() == 0) {
                    Toast.makeText(getContext(), "You should write what you want to buy", Toast.LENGTH_SHORT).show();
                } else if(cur_lat_lng == null) {
                    Toast.makeText(getContext(), "You should choose location to deliver", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://franchukpetro.pythonanywhere.com/insert_order";
                    JSONObject jsonBody = null;
                    try {
                        jsonBody = new JSONObject("{\"wish_list\": \"" + wish_list + "\",\"orderer\": \"" + order_name + "\", \"lat\": \"" + cur_lat_lng.latitude + "\", \"lng\": \"" + cur_lat_lng.longitude + "\"}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(jsonRequest);
                    dismiss();
                }
            }
        });
        FragmentManager fragmentManager = getChildFragmentManager();
        MapFragment map = MapFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.map_frame, map).commit();
        map.getMapAsync(this);


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.i("foober", "map is ready");
        LatLng position = new LatLng(49.85, 24.0166666667);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("foober", "set some markers");
        map.clear();
        Marker marker = map.addMarker(new MarkerOptions().position(latLng));
        cur_lat_lng = latLng;
    }
}
