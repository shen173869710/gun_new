package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.auto.di.guan.R;
import com.auto.di.guan.api.ApiUtil;
import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserActionSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.SyncData;
import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;

/**
 *
 */
public class FragmentTab12 extends BaseFragment {
	private Button login_out;
	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_10, null);
		login_out = (Button) view.findViewById(R.id.login_out);
		login_out.setText("同步数据");
		login_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				activity.finish();
				syncData();
			}
		});

		view.findViewById(R.id.login_1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Entiy.isPlay = true;
			}
		});
		view.findViewById(R.id.login_2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Entiy.isPlay = false;
			}
		});
		view.findViewById(R.id.login_3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Entiy.isSendCode = true;
			}
		});
		view.findViewById(R.id.login_4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Entiy.isSendCode = false;
			}
		});
		return view;
	}

	@Override
	public void refreshData() {

	}

	/**
	 *  数据同步
	 */
	public void syncData() {

		SyncData data = new SyncData();
		data.setDevices(DeviceInfoSql.queryDeviceList());
		data.setActions(UserActionSql.queryUserActionlList());
		data.setGroups(GroupInfoSql.queryGroupList());

		LogUtils.e("分组 -----",new Gson().toJson(GroupInfoSql.queryGroupList()));

		HttpManager.syncData(ApiUtil.createApiService().sync(data), new HttpManager.OnResultListener() {
			@Override
			public void onSuccess(BaseRespone t) {
				LogUtils.e("", "同步数据成功");
			}

			@Override
			public void onError(Throwable error, Integer code, String msg) {
				LogUtils.e("", "同步数据失败");
			}
		});
	}
}
