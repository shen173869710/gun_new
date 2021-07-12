package com.auto.di.guan.basemodel.presenter;


import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.request.BaseRequest;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserActionSql;
import com.auto.di.guan.entity.SyncData;
import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;

import java.util.TreeMap;

/**
 * Created by czl on 2019/7/9.
 * 用户登录相关逻辑业务
 */

public class LoginPresenter extends BasePresenter<ILoginView>{
    private String TAG = "LoginPresenter";

    /**
     *  设备激
     * **/
    public void doDeviceActivation(String loginName,String pwd) {
//        String mac = MacInfo.getMacAddress();
        String mac = "EC:89:14:3A:00:00";
       TreeMap<String, Object> treeMap = new TreeMap<>();
       treeMap.put("loginName",loginName);
       treeMap.put("password", pwd);
       treeMap.put("mac", mac);
       doHttpTask(getApiService().deviceActivation(BaseRequest.toMerchantDeviceTreeMap(treeMap)),
               new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                if (respone != null && respone.isOk() && null !=respone.getData()) {
                    getBaseView().activationSuccess(respone);
                }else{
                    getBaseView().activationFail(null,-1, "设备码不存在,请重新输入!");
                }
            }

            @Override
            public void onError(Throwable error, Integer code,String msg) {
                getBaseView().activationFail(error,code, msg);
            }
       });
    }

    /**
     *
     * 登录请求
     * **/
    public void doLogin(String userName, final String pwd) {
//        String password = Md5Util.md5(pwd);
//        String mac = MacInfo.getMacAddress();

        String mac = "EC:89:14:3A:00:00";
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("loginName",userName);
        treeMap.put("password",pwd);
        treeMap.put("mac", mac);
        doHttpTask(getApiService().login(BaseRequest.toMerchantTreeMap(treeMap)), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                getBaseView().loginSuccess(respone);
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                getBaseView().loginFail(error,code,msg);
            }
        });
    }


    public void doSyncData() {
        SyncData data = new SyncData();
        data.setDevices(DeviceInfoSql.queryDeviceList());

        data.setActions(UserActionSql.queryUserActionlList());
        data.setGroups(GroupInfoSql.queryGroupList());

        doHttpTask(getApiService().sync(data), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                LogUtils.e("----", new Gson().toJson(respone));
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                LogUtils.e("----", msg);
            }
        });

    }


//    public void getToken() {
//        doNewHttpTask(ApiUtil.createApiService("http://openapi.ecois.info").
//                getToken("vn8WKjFPgKhoyN0k", "iZ5QmduqPTG5CpTkvALizYlcP$TYIoQ*"), new HttpManager.OnResultListener() {
//            @Override
//            public void onSuccess(BaseRespone respone) {
//                LogUtils.e(TAG, "respone ==" +respone.getToken());
//
//                if (respone != null && !TextUtils.isEmpty(respone.getToken())) {
//                    ApiUtil.authorization = respone.getToken();
//                    getDeviceInfo();
//
//                }
//            }
//            @Override
//            public void onError(Throwable error, Integer code,String msg) {
//                LogUtils.e("----", msg);
//            }
//        });
//
//    }
//
//
//    public void getDeviceList() {
//        doNewHttpTask(ApiUtil.createApiService("http://openapi.ecois.info").
//                getDeviceList(1, 200), new HttpManager.OnResultListener() {
//            @Override
//            public void onSuccess(BaseRespone respone) {
//                LogUtils.e("----", new Gson().toJson(respone));
//            }
//            @Override
//            public void onError(Throwable error, Integer code,String msg) {
//                LogUtils.e("----", msg);
//            }
//        });
//
//    }
//
//
//    public void getDeviceInfo() {
//        doNewHttpTask(ApiUtil.createApiService("http://openapi.ecois.info").
//                getDeviceInfo("18092100088696"), new HttpManager.OnResultListener() {
//            @Override
//            public void onSuccess(BaseRespone respone) {
//                LogUtils.e("----", new Gson().toJson(respone));
//            }
//            @Override
//            public void onError(Throwable error, Integer code,String msg) {
//                LogUtils.e("----", msg);
//            }
//        });
//    }

}
