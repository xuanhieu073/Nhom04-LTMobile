package com.example.gohotel;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.example.gohotel.api.HotelRequestInterceptor;
import com.example.gohotel.api.ServiceApi;
import com.example.gohotel.api.UrlParams;
import com.example.gohotel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoHotelApplication extends Application {
    public static String DEVICE_ID;
    public static String ID;
    public static Context context;
    public Retrofit retrofit;
    public static ServiceApi serviceApi;
    public static int limit = 50;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DEVICE_ID = Utils.md5(ID);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new HotelRequestInterceptor(this));
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        try {

            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlParams.MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            serviceApi = retrofit.create(ServiceApi.class);
        } catch (Exception e) {

        }

    }

    public static Context getContext() {
        return context;
    }

}
