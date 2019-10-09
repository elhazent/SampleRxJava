package com.elhazent.education.rxjava.network;

import com.elhazent.education.rxjava.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Nu'man Nashif Annawwaf.
 * github : https://github.com/elhazent
 * linkedin : https://www.linkedin.com/in/elhazent/
 */
public class InitRetrofit {
    public static HttpLoggingInterceptor providerLogginInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return loggingInterceptor;
    }


    public static OkHttpClient providerOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(providerLogginInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

    }


    public static Retrofit providerRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(providerOkHttpClient())
                .build();
    }

    public static ApiService providerApiService() {
        return providerRetrofit("https://api.github.com/").create(ApiService.class);
    }
}
