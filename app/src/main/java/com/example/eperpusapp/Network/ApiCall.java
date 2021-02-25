package com.example.eperpusapp.Network;

import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseCategory;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Model.ResponseUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCall {

    //http://192.168.1.30:5000/buku

    @GET("buku")
    Call<ResponseBuku> getCollectionList();

    @GET("login/{username}/{password}")
    Call<ResponseUser> getUserDetail(@Path("username") String username, @Path("password") String password);

    @GET("search/{keyword}")
    Call<ResponseBuku> getCollectionSearch(@Path("keyword") String keyword);

    @GET("listkategori")
    Call<ResponseCategory> getCategoryList();

    @GET("kategori/{category}")
    Call<ResponseBuku> getCollectionCategory(@Path("category") String category);

    @GET("sedangpinjam/{iduser}")
    Call<ResponseMyBook> getMyBookList(@Path("iduser") int id);

}
