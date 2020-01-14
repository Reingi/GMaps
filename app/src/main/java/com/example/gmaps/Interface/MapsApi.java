package com.example.gmaps.Interface;

import com.example.gmaps.LatitudeLongitude;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapsApi {

    @GET("getmaps.php")
    Call<List<LatitudeLongitude>> getMapsdata();
}
