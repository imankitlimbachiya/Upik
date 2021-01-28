package com.upik.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/3/2019.
 */

public class JobOnMapLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context mContext;
    private GoogleMap googleMap;
    private LinearLayout back;
    private ProgressBar Progressbar;
    SupportMapFragment mapFragment;
    private Double lat, longg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobonmaplocation_layout);

        Log.e("Activity", "JobOnMapLocationActivity");

        mContext = this;

        intentData();
    }

    public void intentData() {
        back = findViewById(R.id.back);
        Progressbar = findViewById(R.id.Progressbar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            lat = Double.parseDouble((String) bd.get("lat"));
            longg = Double.parseDouble((String) bd.get("long"));
        }
        initilizeMap();
    }

    public void initilizeMap() {
        try {
            Log.e("initilizeMap", "initilizeMap");
            mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment));
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.e("onMapReady", "onMapReady");
        googleMap = map;
        googleMap.setMapType(map.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMapToolbarEnabled(false);
        if (lat == null && longg == null) {
            LatLng latLng = new LatLng(45.50884, -73.58781);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 9.30F));
        } else {
            LatLng latLng = new LatLng(lat, longg);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .position(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(String.valueOf(lat)), Double.parseDouble(String.valueOf(longg))), 9.30F));
        }
    }
}
