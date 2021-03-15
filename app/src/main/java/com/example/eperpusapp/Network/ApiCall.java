package com.example.eperpusapp.Network;

import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseCategory;
import com.example.eperpusapp.Model.ResponseHistory;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Model.ResponseReturn;
import com.example.eperpusapp.Model.ResponseUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCall {

    //http://192.168.1.30:5000/buku

    @GET("buku/{page}")
    Call<ResponseBuku> getCollectionList(@Path("page") int page);

    @GET("detailbuku/{id}")
    Call<ResponseBuku> getCollectionDetail(@Path("id") int id);

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

    @GET("riwayat/{iduser}/{idbuku}/{idpinjam}/{timestamp}")
    Call<ResponseReturn> getResponseReturn(@Path("iduser") int iduser, @Path("idbuku") int idbuku, @Path("idpinjam") int idpinjam, @Path("timestamp") String timestamp);

    @GET("history/{iduser}")
    Call<ResponseHistory> getHistoryList(@Path("iduser") int iduser);

    @GET("pinjam/{iduser}/{idbuku}/{tanggalpinjam}/{tanggalkembali}")
    Call<ResponseReturn> getResponseBorrow(@Path("iduser") int iduser, @Path("idbuku") int idbuku, @Path("tanggalpinjam") String tglpinjam, @Path("tanggalkembali") String tglkembali);

    @GET("getwishlist/{iduser}")
    Call<ResponseBuku> getWishlist(@Path("iduser") int iduser);

    @GET("addwishlist/{iduser}/{idbuku}")
    Call<ResponseReturn> getResponseAddWishlist(@Path("iduser") int iduser, @Path("idbuku") int idbuku);

    @GET("removewishlist/{iduser}/{idbuku}")
    Call<ResponseReturn> getResponseRemoveWishlist(@Path("iduser") int iduser, @Path("idbuku") int idbuku);

    @GET("updatebaca/{idpinjam}/{progress}")
    Call<ResponseReturn> getResponseUpdateBaca(@Path("idpinjam") int idpinjam, @Path("progress") int progress);
}
