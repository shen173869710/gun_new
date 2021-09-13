package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridOpenAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.Fragment4Event;
import com.auto.di.guan.utils.DiffCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *   单个操作
 */
public class FragmentTab4 extends BaseFragment {
    private RecyclerView recyclerView;
    private View view;
    private MyGridOpenAdapter adapter;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_4, null);
        EventBus.getDefault().register(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_4_gridview);
        deviceInfos.addAll(DeviceInfoSql.queryDeviceList());
        adapter = new MyGridOpenAdapter(deviceInfos);
        adapter.setDiffCallback(new DiffCallback());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Entiy.GUN_COLUMN));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(200);
        recyclerView.setDrawingCacheEnabled(true);//保存绘图，提高速度
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragment4Update(Fragment4Event event) {
        if (adapter != null) {
            adapter.setDiffNewData(DeviceInfoSql.queryDeviceList());
        }
    }

    @Override
    public void refreshData() {
        if (adapter != null) {
            adapter.setDiffNewData(DeviceInfoSql.queryDeviceList());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
