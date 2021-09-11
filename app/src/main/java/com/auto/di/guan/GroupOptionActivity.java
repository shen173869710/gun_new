package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.RecyclerListAdapter;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.event.Fragment32Event;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.SPUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  设置轮灌相关参数
 */
public class GroupOptionActivity extends Activity  {

	private static final String TAG = "GroupOptionActivity";
	private View view;
	private TextView textView;
	private TextView title_bar_status;
	private RecyclerView recyclerView;
	private List<GroupInfo> groupInfos = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_option_layout);
		view = findViewById(R.id.title_bar);

		groupInfos = GroupInfoSql.queryGroupList();
		String local = SPUtils.getInstance().getString(SPUtils.DEVICE_OPTION);
		LogUtils.e(TAG, "local  = "+ local);
//		if (!TextUtils.isEmpty(local)) {
//			List<GroupInfo> localGroup = new Gson().fromJson(local, new TypeToken<List<GroupInfo>>(){}.getType());
//			if (localGroup != null) {
//				int size = localGroup.size();
//				for (int i = 0; i < size; i++) {
//					int length = groupInfos.size();
//					GroupInfo localInfo = localGroup.get(i);
//					for (int j = 0; j < length; j++) {
//						GroupInfo info = groupInfos.get(j);
//						if (localInfo.getGroupId() == info.getGroupId()) {
//							info.setGroupIsJoin(localInfo.getGroupIsJoin());
//							info.setGroupRunTime(0);
//							info.setGroupTime(localInfo.getGroupTime());
//							info.setGroupLevel(localInfo.getGroupLevel());
//						}
//					}
//				}
//			}
//		}

		textView = (TextView)view.findViewById(R.id.title_bar_title);
		textView.setText("自动轮灌设置");
		title_bar_status  = (TextView)view.findViewById(R.id.title_bar_status);
		title_bar_status.setVisibility(View.VISIBLE);
		title_bar_status.setText("保存设置");
		title_bar_status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				int size = groupInfos.size();
				HashMap<Integer, Integer> lv = new HashMap<>();
				for (int i = 0; i < size; i++) {
					GroupInfo groupInfo = groupInfos.get(i);
					if (groupInfo.getGroupIsJoin() == 1) {
						if (groupInfo.getGroupTime() == 0 || groupInfo.getGroupLevel() == 0) {
							ToastUtils.showLongToast("轮灌优先级或者轮灌时长不能为0");
							return;
						}
					}else {
						groupInfo.setGroupRunTime(0);
						groupInfo.setGroupLevel(0);
						groupInfo.setGroupTime(0);
						groupInfo.setGroupEndTime(0);
					}

					int level = groupInfo.getGroupLevel();
					if (level > 0) {
						if (lv.containsKey(level)) {
							ToastUtils.showLongToast("不能设置相同的轮灌优先级,或者优先级不能为空");
							return;
						}
						lv.put(level,level);
					}
				}
				GroupInfoSql.updateGroupList(groupInfos);
				EventBus.getDefault().post(new Fragment32Event());
//				SPUtils.getInstance().putString(SPUtils.DEVICE_OPTION, new Gson().toJson(groupInfos));
				GroupOptionActivity.this.finish();
			}
		});
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupOptionActivity.this.finish();
			}
		});
		recyclerView = (RecyclerView) findViewById(R.id.group_option_view);
		RecyclerListAdapter adapter = new RecyclerListAdapter(groupInfos);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

}
