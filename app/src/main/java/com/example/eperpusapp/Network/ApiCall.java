package com.example.eperpusapp.Network;

import com.example.eperpusapp.Model.Response;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCall {

    //http://192.168.1.30:5000/buku

    @GET("buku")
    Call<Response> getCollectionList();

}
