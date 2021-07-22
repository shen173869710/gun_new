package com.auto.di.guan.api;


import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.model.respone.ERespone;
import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.basemodel.model.respone.MeteoRespone;
import com.auto.di.guan.db.User;
import com.auto.di.guan.entity.SyncData;
import com.auto.di.guan.entity.TableDataInfo;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求的相关接口
 */
public interface ApiService {

    /**
     *  设备激活
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api//user/deviceActivation")
    Observable<BaseRespone<LoginRespone>> deviceActivation(@FieldMap Map<String, Object> map);

    /**
     *  用户登录接口
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/user/login")
    Observable<BaseRespone<User>> login(@FieldMap Map<String, Object> map);

    /**
     *  用户数据同步
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("/api/syncData")
    Observable<BaseRespone> sync(@Body SyncData data);

    /**
     *  获取用户token
     */
    @GET("/v3/token")
    Observable<BaseRespone> getToken(@Query("appid") String appid, @Query("secret") String secret);
    @GET("/v3/devices")
    Observable<BaseRespone> getDeviceList(@Query("page") int page, @Query("limit") int limit);
    @GET("/v3/device/{sn}")
    Observable<MeteoRespone> getDeviceInfo(@Path("sn") String sn);
    @GET("/v3/device/{sn}/data")
    Observable<ERespone> getDeviceData(@Path("sn") String sn);


    /**
     *  发送预计信息
     * @return
     *
     *
     * @NotBlank(message = "手机号码不能为空")
     *  private String mobile;
     *
     *  @NotBlank(message = "短信内容不能为空")
     *  private String content;
     */
    @FormUrlEncoded
    @POST("/api/send/smsMsg")
    Observable<BaseRespone> sendSmsMsg(@FieldMap Map<String, Object> map);

//    TreeMap<String, Object> treeMap = new TreeMap<>();
//        treeMap.put("mobile","18675570791");
//        treeMap.put("content","1111111111");
//    //        treeMap.put("pageNum",1);
////        treeMap.put("pageSize",2);
//    doHttpTask(getApiService().sendSmsMsg(BaseRequest.toMerchantTreeMap(treeMap)), new HttpManager.OnResultListener() {
//        @Override
//        public void onSuccess(BaseRespone respone) {
//            getBaseView().success(respone);
//        }
//        @Override
//        public void onError(Throwable error, Integer code,String msg) {
//            getBaseView().fail(error,code,msg);
//        }
//    });

}
