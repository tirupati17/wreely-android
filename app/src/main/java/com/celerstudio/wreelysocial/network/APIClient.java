package com.celerstudio.wreelysocial.network;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.celerstudio.wreelysocial.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    private static APIService apiService;

    public static APIService getAPIService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        ./*addHeader("Authorization", getBasicAuth()).*/
                        addHeader("Accept", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        if (apiService == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.interceptors().add(interceptor());
//            if (BuildConfig.DEBUG)
                httpClient.interceptors().add(httpLoggingInterceptor);
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(BuildConfig.DOMAIN)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );
            Retrofit retrofit = builder.client(httpClient.build()).build();
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

    public static Interceptor interceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(interceptor());
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(httpLoggingInterceptor());
        return httpClient.build();
    }

    public static APIService getAdapterApiService() {
        Retrofit retrofit = new Retrofit.Builder().
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .baseUrl(BuildConfig.DOMAIN).build();
        return retrofit.create(APIService.class);
    }

    @NonNull
    private static Gson gson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private static String getBearer(String id) {
        return "Bearer " + id;
    }


}