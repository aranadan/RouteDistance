package com.fox.studio.routedistance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 500;
    private List<LatLng> pointsList;
    private Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pointsList = new ArrayList<>();
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Get context for requests
        final GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();

        //location permit
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(latLng -> {
            //reset marker if already 2
            if (pointsList.size() == 2) {
                pointsList.clear();
                mMap.clear();
            }

            //save first point
            pointsList.add(latLng);

            // create marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            if (pointsList.size() == 1) {
                //add first marker to the map
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Start");
            } else {
                //add second marker to the map
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Finish");

                addDirectionToDB();

                //request to gmaps api
                DirectionsResult result = null;
                try {
                    result = DirectionsApi.newRequest(geoApiContext)
                            .origin(new com.google.maps.model.LatLng(pointsList.get(0).latitude, pointsList.get(0).longitude))//Место старта
                            .destination(new com.google.maps.model.LatLng(pointsList.get(1).latitude, pointsList.get(1).longitude))//Пункт назначения
                            .alternatives(true)
                            .await();//Промежуточные точки.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (com.google.maps.errors.ApiException e) {
                    e.printStackTrace();
                }


                Log.d("onMapReady", "AddPolyline ");

                //draw multi way point
                for (int i = 0; i < result.routes.length; i++){

                    int red =  new Random().nextInt(256);
                    int green = new Random().nextInt(256);
                    int blue = new Random().nextInt(256);
                    int randomColor = Color.argb(200,red,green,blue);
                    mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(result.routes[i].overviewPolyline.getEncodedPath()))).setColor(randomColor);
                }

                //mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath())));
            }

            mMap.addMarker(markerOptions);
        });
    }

    //обработка результата запроса на определение местоположения
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    void addDirectionToDB(){
        RealmResults<RouteModel> results = mRealm.where(RouteModel.class).sort("date").findAll();

        if (results.size() < 10) {
            addItemToDB();

        } else {
            //delete
            mRealm.beginTransaction();
            results.deleteFirstFromRealm();
            mRealm.commitTransaction();

            addItemToDB();
        }
    }

   void addItemToDB(){
       //add points to RealmDB
       mRealm.beginTransaction();
       RouteModel model = mRealm.createObject(RouteModel.class);
       model.setFromPointLat(pointsList.get(0).latitude);
       model.setFromPointLon(pointsList.get(0).longitude);
       model.setToPointLat(pointsList.get(1).latitude);
       model.setToPointLon(pointsList.get(1).longitude);
       model.setDate(new Date());
       mRealm.commitTransaction();
   }
}
