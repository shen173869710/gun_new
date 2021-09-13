package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.dialog.MainShowInputDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.DiffCallback;
import com.auto.di.guan.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FragmentTab0 extends BaseFragment {
    private RecyclerView recyclerView;
    private View view;
    private MyGridAdapter adapter;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_0, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_0_gridview);
        deviceInfos.addAll(DeviceInfoSql.queryDeviceList());
        adapter = new MyGridAdapter(deviceInfos);
        adapter.setDiffCallback(new DiffCallback());
        recyclerView.setLayoutManager(new GridLayoutManager(activity, Entiy.GUN_COLUMN));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(200);
        recyclerView.setDrawingCacheEnabled(true);//保存绘图，提高速度
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final DeviceInfo info = deviceInfos.get(position);
                if (info.getDeviceStatus() == Entiy.DEVEICE_UNBIND) {
                    MainShowDialog.ShowDialog(getActivity(), "添加阀控器", "添加阀控器到当前区域", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.bindDevice(info.getDeviceId());
                            DeviceInfoSql.updateDevice(info);
                            adapter.setDiffNewData(DeviceInfoSql.queryDeviceList());
                        }
                    });
                } else {
                    if (info.getValveDeviceSwitchList().get(0).getValveGroupId() > 0
                            || info.getValveDeviceSwitchList().get(1).getValveGroupId() > 0) {
                        showToast("该设备已经分组,不可以删除");
                        return;
                    }
                    MainShowInputDialog.ShowDialog(getActivity(), "删除阀控器", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.unBindDevice(info.getDeviceId());
                            DeviceInfoSql.updateDevice(info);
                            adapter.setDiffNewData(DeviceInfoSql.queryDeviceList());
                        }
                    });
                }

            }
        });
        return view;
    }


    @Override
    public void refreshData() {
        if (adapter != null) {
            adapter.setDiffNewData(DeviceInfoSql.queryDeviceList());
        }
    }

}
