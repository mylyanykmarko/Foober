package com.example.mylyanyk.foober;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDialog fragment = new OrderDialog( );
                fragment.show(getFragmentManager(), "Order Dialog");
            }
        });
        fab.setImageResource(R.drawable.ic_add_black_24dp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class,
                "database").allowMainThreadQueries().build();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapFragment fragment = MapFragment.newInstance();
        fragmentTransaction.replace(R.id.container, fragment);
        fragment.getMapAsync(this);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_order_map) {
            Log.i("foober", "Opened map");
            MapFragment fragment = MapFragment.newInstance();
            fragmentTransaction.replace(R.id.container, fragment);
            fragment.getMapAsync(this);
        } else if (id == R.id.nav_my_orders) {
            Log.i("foober", "Opened your orders");
            OrderCurrentFragment fragment = new OrderCurrentFragment();
            fragmentTransaction.replace(R.id.container, fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng position = new LatLng(49.85, 24.0166666667);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));

        String url = "http://franchukpetro.pythonanywhere.com/get_orders";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("foober", response.toString());
                try {
                    JSONArray json_orders = response.getJSONArray("orders");
                    for(int i = 0; i < json_orders.length(); i++) {
                        JSONObject order = json_orders.optJSONObject(i);
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(order.getDouble("latitude"), order.getDouble("longitude"))));
                        Log.i("foober", "added marker");
                        marker.setTag(order.getInt("id"));

                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Integer order_id = (Integer) marker.getTag();
                                Bundle bundle = new Bundle();
                                bundle.putInt("Id", order_id);
                                OrderShowDialog orderDialog = new OrderShowDialog();
                                orderDialog.setArguments(bundle);
                                orderDialog.show(getFragmentManager(), "OrderDialog");
                                return true;
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
