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
        deviceInfos = DeviceInfoSql.queryDeviceList();
        adapter = new MyGridOpenAdapter(deviceInfos);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Entiy.GUN_COLUMN));
        recyclerView.setAdapter(adapter);;
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragment4Update(Fragment4Event event) {
        if (adapter != null) {
//            deviceInfos.clear();
//            deviceInfos.addAll(DeviceInfoSql.queryDeviceList());
//            adapter.notifyDataSetChanged();
            adapter.setNewData(DeviceInfoSql.queryDeviceList());
        }
    }

    ;

    @Override
    public void refreshData() {
        if (adapter != null) {
            adapter.setNewData(DeviceInfoSql.queryDeviceList());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
