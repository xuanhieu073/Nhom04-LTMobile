package com.example.gohotel.api;

import com.example.gohotel.GoHotelApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HotelRequestInterceptor implements Interceptor{
    private final GoHotelApplication app;

    public HotelRequestInterceptor(GoHotelApplication app) {
        this.app = app;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();


//        builder.addHeader("Accept", "application/json").build();
//        builder.header("Content-Type", "application/json");

//        if (app.getUser() != null && app.getUser().getAccess_token() != null) {
//            builder.addHeader("Authorization",  app.getUser().getAccess_token());
//        }else{
//            if(app.getAccessToken() != null && !app.getAccessToken().isEmpty()){
//                builder.addHeader("Authorization", "Bearer " + app.getAccessToken());
//            }
//        }
        return chain.proceed(builder.build());


    }

}
