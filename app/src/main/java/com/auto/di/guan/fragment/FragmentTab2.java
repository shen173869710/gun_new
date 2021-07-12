package com.auto.di.guan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.auto.di.guan.ChooseGroupctivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.GroupExpandableListViewaAdapter2;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.GroupList;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.dialog.MainShowInputDialog;
import com.auto.di.guan.event.ChooseGroupEvent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

/**
 *   控制阀分组
 */
public class FragmentTab2 extends BaseFragment {
	private Button addBtn;
	private View view;
	private  List<GroupList> groupLists = new ArrayList<>();
	private  List<GroupInfo> groupInfos = new ArrayList<>();
	private  List<DeviceInfo> deviceInfos = new ArrayList<>();
	private  List<ControlInfo> controlInfos = new ArrayList<>();

	private GroupExpandableListViewaAdapter2 adapter;
	private ExpandableListView expandableListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_2, null);
		addBtn = (Button) view.findViewById(R.id.addBtn);

		expandableListView =(ExpandableListView)view.findViewById(R.id.expandableListView);
		adapter = new GroupExpandableListViewaAdapter2(getActivity(), groupLists);
		expandableListView.setAdapter(adapter);
		expandableListView.setGroupIndicator(null);
		addBtn = (Button) view.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				getActivity().startActivity(new Intent(getActivity(), ChooseGroupctivity.class));
			}
		});


		Button button = (Button) view.findViewById(R.id.delBtn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				MainShowInputDialog.ShowDialog(getActivity(), "删所有分组除",  new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						List<DeviceInfo> deviceInfos = DeviceInfoSql.queryDeviceList();
						int size = deviceInfos.size();
						for (int i = 0; i < size; i++) {
							DeviceInfo deviceInfo = deviceInfos.get(i);
							deviceInfo.getValveDeviceSwitchList().get(0).setValveGroupId(0);
							deviceInfo.getValveDeviceSwitchList().get(0).setSelect(false);
							deviceInfo.getValveDeviceSwitchList().get(1).setValveGroupId(0);
							deviceInfo.getValveDeviceSwitchList().get(1).setSelect(false);
						}
						DeviceInfoSql.updateDeviceList(deviceInfos);
						groupInfos = GroupInfoSql.queryGroupList();
						int count = groupInfos.size();
						for (int i = 0; i < count; i++) {
							GroupInfoSql.deleteGroup(groupInfos.get(i));
						}
						LevelInfoSql.delLevelInfoList();
						if (LevelInfoSql.queryLevelInfoList().size() == 0) {
							List<LevelInfo> levelInfos = new ArrayList<>();
							for (int i = 1; i < 200; i++) {
								LevelInfo info = new LevelInfo();
								info.setLevelId(i);
								info.setIsGroupUse(false);
								info.setIsLevelUse(false);
								levelInfos.add(info);
							}
							LevelInfoSql.insertLevelInfoList(levelInfos);
						}
						groupInfos.clear();
						groupLists.clear();
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
		initData();
		EventBus.getDefault().register(this);
		return view;
	}

	private void initData() {
		groupLists.clear();
		groupInfos = GroupInfoSql.queryGroupList();
		deviceInfos = DeviceInfoSql.queryDeviceList();
		int size = deviceInfos.size();
		controlInfos.clear();
		for (int i = 0; i < size; i++) {
			controlInfos.addAll(deviceInfos.get(i).getValveDeviceSwitchList());
		}
		int gSize = groupInfos.size();
		if (gSize > 0) {
			for (int i = 0; i < gSize; i++) {
				GroupInfo groupInfo = groupInfos.get(i);
				int controlSize = controlInfos.size();
				GroupList groupList = new GroupList();
				groupList.controlInfos = new ArrayList<>();
				groupList.groupInfo = groupInfo;
				for (int j = 0; j < controlSize; j++) {
					if (controlInfos.get(j).getValveGroupId() == groupInfo.getGroupId()) {
						groupList.controlInfos.add(controlInfos.get(j));
					}

				}
				groupLists.add(groupList);
			}
		}
		if (adapter != null)
		adapter.setData(groupLists);
	}


	@Override
	public void refreshData() {
		initData();

	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onChooseGroupEvent(ChooseGroupEvent event) {
		LogUtils.e("FragmentTab2", "ChooseGroupEvent（）");
		refreshData();
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}


}
