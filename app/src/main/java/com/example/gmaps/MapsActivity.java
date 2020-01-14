package com.example.gmaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.gmaps.Interface.MapsApi;
import com.example.gmaps.Url.Url;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;
    private Location currentLocation;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        }catch(Resources.NotFoundException e){
            Log.e(TAG,"Can't find style. Error: ",e);
        }

        MapsApi apiMaps = Url.getInstance().create(MapsApi.class);
        Call<List<LatitudeLongitude>> listCall = apiMaps.getMapsdata();

        listCall.enqueue(new Callback<List<LatitudeLongitude>>() {
            @Override
            public void onResponse(Call<List<LatitudeLongitude>> call, Response<List<LatitudeLongitude>> response) {
                List<LatitudeLongitude> latLngs = response.body();
                for(LatitudeLongitude latlon : latLngs)
                {
                    double longid = latlon.getLon();
                    double latid = latlon.getLat();

                    center =  CameraUpdateFactory.newLatLng(new LatLng(27.706793,85.330050));
                    zoom = CameraUpdateFactory.zoomTo(13);
                    mMap.addMarker(new MarkerOptions().title(latlon.getMarker())
                            .position(new LatLng(latid,longid))
                    );

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    //Log.d("Goole Maps ", "onResponse: " + latlon.getMarker());
                }
                //Toast.makeText(MapsActivity.this, "Successfully called api", Toast.LENGTH_SHORT).show();
                //System.out.println("the response is: "+response);
            }

            @Override
            public void onFailure(Call<List<LatitudeLongitude>> call, Throwable t) {

            }
        });

       /* List<LatitudeLongitude> latLngs = new ArrayList<>();
        latLngs.add(new LatitudeLongitude(27.7052354,85.3294158,"Softwarica College of IT"));
        latLngs.add(new LatitudeLongitude(27.70482,85.3293997,"Gopal Dai ko Chatamari"));

        CameraUpdate center, zoom;

        for(int i=0;i<latLngs.size();i++){
            center = CameraUpdateFactory.newLatLng(new LatLng(latLngs.get(i).getLat(),latLngs.get(i).getLon()));

            zoom = CameraUpdateFactory.zoomTo(16);
            mMap.addMarker(new MarkerOptions().position(new LatLng(latLngs.get(i).getLat()
                    ,latLngs.get(i).getLon())).title(latLngs.get(i).getMarker()));

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            mMap.setMyLocationEnabled(true);*//*
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);*//*
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }*/

    }
}
