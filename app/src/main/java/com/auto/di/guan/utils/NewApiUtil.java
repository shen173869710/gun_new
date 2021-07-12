package com.auto.di.guan.utils;

import android.text.TextUtils;
import android.util.Log;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.api.ApiUtil;
import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.api.MGsonConverterFactory;
import com.auto.di.guan.api.MyGsonConverterFactory;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.google.gson.Gson;

public class NewApiUtil {
    private static String TAG = "NewApiUtil";
    public static void getToken(final String device, final HttpManager.OnResultListener onResultListener) {
        HttpManager.newApi(ApiUtil.createApiService("http://openapi.ecois.info", MyGsonConverterFactory.create()).
                getToken("vn8WKjFPgKhoyN0k", "iZ5QmduqPTG5CpTkvALizYlcP$TYIoQ*"), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                Log.e(TAG, "respone ==" +respone.getToken());
                if (respone != null && !TextUtils.isEmpty(respone.getToken())) {
                    ApiUtil.authorization = respone.getToken();
                    getDeviceInfo(device, onResultListener);
                }
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                Log.e("----", msg);

            }
        });
    }


    public static void initToken( final HttpManager.OnResultListener onResultListener) {
        HttpManager.newApi(ApiUtil.createApiService("http://openapi.ecois.info", MyGsonConverterFactory.create()).
                getToken("vn8WKjFPgKhoyN0k", "iZ5QmduqPTG5CpTkvALizYlcP$TYIoQ*"), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                Log.e(TAG, "respone ==" +respone.getToken());
                if (respone != null && !TextUtils.isEmpty(respone.getToken())) {
                    ApiUtil.authorization = respone.getToken();
                }
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                Log.e("----", msg);

            }
        });
    }


    public static void getDeviceList() {
        HttpManager.newApi(ApiUtil.createApiService("http://openapi.ecois.info",MyGsonConverterFactory.create()).
                getDeviceList(1, 200), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                Log.e("----", new Gson().toJson(respone));
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                Log.e("----", msg);
            }
        });

    }

    public static void getDeviceInfo(String device, HttpManager.OnResultListener onResultListener) {
        HttpManager.newApi(ApiUtil.createApiService("http://openapi.ecois.info", MGsonConverterFactory.create()).getDeviceInfo(device), onResultListener);
    }

    public static void getDeviceData(String device, HttpManager.OnResultListener onResultListener) {
        HttpManager.newApi(ApiUtil.createApiService("http://openapi.ecois.info",MyGsonConverterFactory.create()).getDeviceData(device), onResultListener);
    }


}
