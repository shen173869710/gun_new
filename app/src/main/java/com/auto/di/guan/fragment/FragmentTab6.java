package com.auto.di.guan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.GroupStatusActivity;
import com.auto.di.guan.OptionSettingActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.GroupExpandableListViewaAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.GroupList;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.AutoTaskEvent;
import com.auto.di.guan.event.Fragment32Event;
import com.auto.di.guan.event.GroupStatusEvent;
import com.auto.di.guan.utils.NoFastClickUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FragmentTab6 extends BaseFragment {
	private View view;
	private ExpandableListView expandableListView;
	private List<GroupList> groupLists = new ArrayList<>();
	private  List<GroupInfo> groupInfos = new ArrayList<>();
	private GroupExpandableListViewaAdapter adapter;
	private Button fragment_6_start;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_6, null);
		EventBus.getDefault().register(this);
		expandableListView =(ExpandableListView)view.findViewById(R.id.fragment_6_expand);
		adapter = new GroupExpandableListViewaAdapter(getActivity(), groupLists);
		expandableListView.setGroupIndicator(null);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), OptionSettingActivity.class);
				intent.putExtra("id",groupLists.get(position).groupInfo.getGroupId());
				startActivity(intent);
			}
		});

		fragment_6_start = (Button) view.findViewById(R.id.fragment_6_start);
		fragment_6_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}

				BaseApp.setWebLogin(false);
				activity.startActivity(new Intent(activity, GroupStatusActivity.class));

			}
		});
		initData();
		return view;
	}


	private void initData() {
		groupInfos.clear();
		groupLists.clear();
		groupInfos = GroupInfoSql.queryGroupSettingList();
		int size = groupInfos.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				List<ControlInfo>clist = ControlInfoSql.queryControlList(groupInfos.get(i).getGroupId());
				if (clist.size() > 0) {
					GroupList list = new GroupList();
					list.groupInfo = groupInfos.get(i);
					list.controlInfos.addAll(clist);
					groupLists.add(list);
				}
			}
			if (adapter != null) {
				adapter.setData(groupLists);
			}
		}
	}

	@Override
	public void adapterUpdate() {
		super.adapterUpdate();
		adapter.notifyDataSetChanged();
	}


	@Override
	public void refreshData() {
		initData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFragment32Update(Fragment32Event event) {
		if (adapter != null) {
			initData();
		}
	}
	/**
	 *        自动轮灌组状态更新
	 * @param event
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onGroupStatusEvent(GroupStatusEvent event) {
		if (adapter != null) {
			initData();
		}
	}

	/**
	 *   接收自动轮灌相关操作
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onAutoTaskEvent(AutoTaskEvent event) {
		if (event != null) {
			if (event.getType() == Entiy.RUN_DO_FINISH) {
				if (adapter != null) {
					initData();
				}
			}
		}
	}
}
