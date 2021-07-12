package com.auto.di.guan.adapter;

import android.text.TextUtils;

import com.auto.di.guan.R;
import com.auto.di.guan.api.ApiEntiy;
import com.auto.di.guan.basemodel.model.respone.EDepthRespone;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;


import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

public class Fragment8RightAdapter extends BaseMultiItemQuickAdapter<EDepthRespone, BaseViewHolder> {
    public Fragment8RightAdapter(List<EDepthRespone> data) {
        super(data);
        //设置当传入的itemType为某个常量显示不同的item
        addItemType(ApiEntiy.ITEM_TYPE_0, R.layout.fragment_8_right_list_item_0);
        addItemType(ApiEntiy.ITEM_TYPE_1, R.layout.fragment_8_right_list_item_1);
        addItemType(ApiEntiy.ITEM_TYPE_2, R.layout.fragment_8_right_list_item_1);
        addItemType(ApiEntiy.ITEM_TYPE_3, R.layout.fragment_8_right_list_item_2);
        addItemType(ApiEntiy.ITEM_TYPE_4, R.layout.fragment_8_right_list_item_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, EDepthRespone respone) {
        switch (holder.getItemViewType()){
            case 0:
                break;
            case 1:
                holder.setText(R.id.item_0, "采集时间");
                holder.setText(R.id.item_1, "空气温度(℃)");
                holder.setText(R.id.item_2, "相对湿度(%)");
                holder.setText(R.id.item_3, "大气压力(hPa)");
                holder.setText(R.id.item_4, "风速(m/s)");
                holder.setText(R.id.item_5, "风向(°)");
                holder.setText(R.id.item_6, "雨量(mm)");
                holder.setText(R.id.item_7, "当前太阳辐射强度(W/m²)");
                holder.setText(R.id.item_8, "累计太阳辐射量(MJ/m²)");
                break;
            case 2:
                holder.setText(R.id.item_0, respone.getDatetime()+"");
                String airTemperature = respone.getAirTemperature();
                if (TextUtils.isEmpty(airTemperature)) {
                    airTemperature = "-";
                }
                holder.setText(R.id.item_1, airTemperature);

                String relativeHumidity = respone.getRelativeHumidity();
                if (TextUtils.isEmpty(relativeHumidity)) {
                    relativeHumidity = "-";
                }
                holder.setText(R.id.item_2, relativeHumidity);

                String atmosphericPressure = respone.getAtmosphericPressure();
                if (TextUtils.isEmpty(atmosphericPressure)) {
                    atmosphericPressure = "-";
                }
                holder.setText(R.id.item_3, atmosphericPressure);

                String averageWindSpeed = respone.getAverageWindSpeed();
                if (TextUtils.isEmpty(averageWindSpeed)) {
                    averageWindSpeed = "0.00";
                }
                holder.setText(R.id.item_4, averageWindSpeed);

                String windDirection = respone.getWindDirection();
                if (TextUtils.isEmpty(windDirection)) {
                    windDirection = "0.00";
                }
                holder.setText(R.id.item_5, windDirection);

                String rainfall = respone.getRainfall();
                if (TextUtils.isEmpty(rainfall)) {
                    rainfall = "0.00";
                }
                holder.setText(R.id.item_6, rainfall);

                String solarRadiationIntensity = respone.getSolarRadiationIntensity();
                if (TextUtils.isEmpty(solarRadiationIntensity)) {
                    solarRadiationIntensity = "0.00";
                }
                holder.setText(R.id.item_7, solarRadiationIntensity);

                String solarRadiationAmount = respone.getSolarRadiationAmount();
                if (TextUtils.isEmpty(solarRadiationAmount)) {
                    solarRadiationAmount = "0.00";
                }
                holder.setText(R.id.item_8, solarRadiationAmount);

                break;
            case 3:
                holder.setText(R.id.item_0, "采集时间");
                holder.setText(R.id.item_1, "深度");
                holder.setText(R.id.item_2, "土壤温度(℃)");
                holder.setText(R.id.item_3, "水分含量(%)");
                break;
            case 4:
                holder.setText(R.id.item_0, respone.getDatetime()+"");
                holder.setText(R.id.item_1, ""+respone.getDapthName());
                String temperature = respone.getTemperature();
                if (TextUtils.isEmpty(temperature)) {
                    temperature = "-";
                }
                holder.setText(R.id.item_2, temperature);

                String moisture = respone.getMoisture();
                if (TextUtils.isEmpty(moisture)) {
                    moisture = "-";
                }
                holder.setText(R.id.item_3, moisture);
                break;
        }
    }
}
