package com.auto.di.guan.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.ChooseGroupEvent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
public class GroupEditListAdapter extends BaseQuickAdapter<ControlInfo, BaseViewHolder> {

    public GroupEditListAdapter(List<ControlInfo> data) {
        super(R.layout.group_edit_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ControlInfo info) {
        holder.setText(R.id.group_item_id,"阀门"+info.getValveName());
        holder.setText(R.id.group_item_name,"别名"+info.getValveAlias());
        String stutes = "";
        int valveStatus = info.getValveStatus();
        if (valveStatus == Entiy.CONTROL_STATUS＿RUN) {
            stutes = "工作当中";
        } else if (valveStatus == Entiy.CONTROL_STATUS＿ERROR) {
            stutes = "工作异常";
        } else {
            stutes = "可以编辑";
        }
        holder.setText(R.id.group_item_status,stutes);
        holder.getView(R.id.group_edit_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (valveStatus == Entiy.CONTROL_STATUS＿RUN || valveStatus == Entiy.CONTROL_STATUS＿ERROR) {
//                    Toast.makeText(getContext(), "阀控器处于不可以编辑状态", Toast.LENGTH_LONG).show();
//                } else {
                    MainShowDialog.ShowDialog((Activity) getContext(), "退出分组", "是退出当前分组", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getData().size() == 1) {
                                Toast.makeText(getContext(), "当前小组只有一个阀控器, 如果要退出, 请点击解散分组", Toast.LENGTH_LONG).show();
                                return;
                            }
                            info.setValveGroupId(0);
                            info.setSelect(false);
                            getData().remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            ControlInfoSql.updateControl(info);
                            EventBus.getDefault().post(new ChooseGroupEvent());
                        }
                    });

//                }
            }
        });
    }


}
