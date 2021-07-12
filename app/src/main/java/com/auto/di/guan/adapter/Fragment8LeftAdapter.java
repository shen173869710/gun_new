package com.auto.di.guan.adapter;

import androidx.annotation.Nullable;

import com.auto.di.guan.R;
import com.auto.di.guan.basemodel.model.respone.MeteoLocation;
import com.auto.di.guan.basemodel.model.respone.MeteoRespone;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;


public class Fragment8LeftAdapter extends BaseQuickAdapter<MeteoRespone, BaseViewHolder> {
    public Fragment8LeftAdapter(@Nullable List<MeteoRespone> data) {
        super(R.layout.fragment_8_left_list_item, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MeteoRespone respone) {
        holder.setText(R.id.item_0, respone.getAlias());
        holder.setText(R.id.item_1, respone.getSn());

        MeteoLocation location = respone.getLocation();
        if (location != null) {
            String local = location.getProvince()+location.getCity()+location.getDistrict();
            holder.setText(R.id.item_2, local);
        }else {
            holder.setText(R.id.item_2, "");
        }

        if (respone.isSle()) {
            holder.setBackgroundResource(R.id.item_layout,R.drawable.fragment_tab_0_sel);
        }else {
            holder.setBackgroundResource(R.id.item_layout,R.color.fragment0_tab_n);
        }
    }
}

