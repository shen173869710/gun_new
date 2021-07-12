package com.auto.di.guan.api;

import com.auto.di.guan.BuildConfig;
import com.auto.di.guan.utils.LogUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by czl on 2019-7-9.
 */
public class ApiUtil {
    public static String TAG = "RetrofitClient";

    public static String authorization = "";
    private static int CONNECT_TIME = 15;
    private static int RADE_TIMEOUR = 15;

    public static ApiService createApiService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e(TAG, "----request   " + message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(RADE_TIMEOUR , TimeUnit.SECONDS);
        okHttpClientBuilder.retryOnConnectionFailure(true);
        OkHttpClient client = okHttpClientBuilder.addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.http_post_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createApiService(String url, Converter.Factory factory) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e(TAG, "----request   " + message);
            }


        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(RADE_TIMEOUR , TimeUnit.SECONDS);
        okHttpClientBuilder.retryOnConnectionFailure(true);
        OkHttpClient client = okHttpClientBuilder
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new TokenHeaderInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }


    public static class TokenHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request updateRequest = originalRequest.newBuilder()
                    .header("Authorization", authorization)
                    .build();
            return chain.proceed(updateRequest);
        }
    }

}
