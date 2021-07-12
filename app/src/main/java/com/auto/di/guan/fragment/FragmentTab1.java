package com.auto.di.guan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.ControlBindActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.BindIdEvent;
import com.auto.di.guan.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentTab1 extends BaseFragment {
    private RecyclerView recyclerView;
    private View view;
    private MyGridAdapter adapter;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, null);
        LogUtils.e("fragment","     39--"+System.currentTimeMillis());
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_1_gridview);
        deviceInfos.addAll(DeviceInfoSql.queryDeviceList());
        adapter = new MyGridAdapter(deviceInfos);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Entiy.GUN_COLUMN));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(200);
        recyclerView.setDrawingCacheEnabled(true);//保存绘图，提高速度
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("tab1","poation = "+position + " ="+new Gson().toJson(deviceInfos.get(position)));
                if ( deviceInfos.get(position).getDeviceStatus() == Entiy.DEVEICE_BIND) {
                        Intent intent = new Intent(getActivity(), ControlBindActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", deviceInfos.get(position));
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                }else {
                    showToast("无可用的设备");
                }
            }
        });
        EventBus.getDefault().register(this);
        LogUtils.e("fragment","     64--"+System.currentTimeMillis());
        return view;
    }

    @Override
    public void refreshData() {
        if (adapter != null) {
            LogUtils.e("fragment","     70--"+System.currentTimeMillis());
            deviceInfos.clear();
            deviceInfos.addAll(DeviceInfoSql.queryDeviceList());
            adapter.notifyDataSetChanged();
            LogUtils.e("fragment","     73--"+System.currentTimeMillis());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindIdEvent(BindIdEvent event) {
        LogUtils.e("FragmentTab1", "onBindIdEvent（）");
       refreshData();
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
