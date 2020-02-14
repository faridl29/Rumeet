package com.mfa.rumeet.Rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://192.168.43.85/Rumeet/index.php/";
    public static final String IMAGE_URL = "http://192.168.43.85/WebAdmin_Rumeet/gambar/";
    public static final String PROFILE_URL = "http://192.168.43.85/WebAdmin_Rumeet/foto_profile/";


    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if (retrofit ==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
