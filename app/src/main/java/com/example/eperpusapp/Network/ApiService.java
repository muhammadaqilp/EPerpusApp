package com.example.eperpusapp.Network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    //    private static String BASE_URL = "http://192.168.1.17:1298/"; //office
//    private static String BASE_URL = "http://172.24.144.1:5000/"; //localhost
//private static String BASE_URL = "http://192.168.100.22:5000/"; //localhost
    private static String BASE_URL = "http://192.168.1.29:5000/"; //localhost
    private static HttpLoggingInterceptor loggingInterceptor = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    public static ApiCall apiCall() {
        loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiCall.class);
    }

}
