package com.auto.di.guan.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.auto.di.guan.R;
import com.auto.di.guan.entity.CmdStatus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import java.util.List;

public class DialogListViewAdapter extends BaseQuickAdapter<CmdStatus, BaseViewHolder> {
    public DialogListViewAdapter(List<CmdStatus> data) {
        super(R.layout.dialoglistviewitem, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CmdStatus cmdStatus) {
        holder.setText(R.id.cmd_name, "");
        holder.setText(R.id.cmd_alias, "");
        holder.setText(R.id.cmd_start, "");
        holder.setText(R.id.cmd_end, "");
        holder.setText(R.id.cmd_read_start, "");
        holder.setText(R.id.cmd_read_middle, "");
        holder.setText(R.id.cmd_read_end, "");

        if (!TextUtils.isEmpty(cmdStatus.getControlName())) {
            holder.setText(R.id.cmd_name, "阀"+ cmdStatus.getControlName());
        }

        if (!TextUtils.isEmpty(cmdStatus.getControlAlias())) {
            holder.setText(R.id.cmd_alias, ""+ cmdStatus.getControlAlias());
        }

        if (cmdStatus.getCmd_start() != null) {
            holder.setText(R.id.cmd_start, Html.fromHtml(cmdStatus.getCmd_start()+""));
        }

        if (cmdStatus.getCmd_end() != null) {
            holder.setText(R.id.cmd_end, Html.fromHtml(cmdStatus.getCmd_end()+""));
        }

        if (cmdStatus.getCmd_read_start() != null) {
            holder.setText(R.id.cmd_read_start, Html.fromHtml(cmdStatus.getCmd_read_start()+""));
        }

        if (cmdStatus.getCmd_read_middle() != null) {
            holder.setText(R.id.cmd_read_middle, Html.fromHtml(cmdStatus.getCmd_read_middle()+""));
        }

        if (cmdStatus.getCmd_read_end() != null) {
            holder.setText(R.id.cmd_read_end, Html.fromHtml(cmdStatus.getCmd_read_end()+""));
        }
    }
}
