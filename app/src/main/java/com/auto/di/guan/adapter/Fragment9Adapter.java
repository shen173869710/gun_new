package com.auto.di.guan.adapter;

import androidx.annotation.Nullable;

import com.auto.di.guan.R;
import com.auto.di.guan.entity.OptionEntiy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class Fragment9Adapter extends BaseQuickAdapter<OptionEntiy, BaseViewHolder> {
    public Fragment9Adapter(@Nullable List<OptionEntiy> data) {
        super(R.layout.fragment_9_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final OptionEntiy info) {
        holder.setText(R.id.option_name, "第 " + info.getName() + " 行");
        if (info.isSel()) {
            holder.setBackgroundResource(R.id.option_switch, R.mipmap.select_bg_yellow);
        } else {
            holder.setBackgroundResource(R.id.option_switch, R.mipmap.select_bg_gray);
        }

    }

}
