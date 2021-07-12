package com.auto.di.guan;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.basemodel.presenter.LoginPresenter;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.entity.ElecEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.CopyObject;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.auto.di.guan.view.XEditText;
import com.google.gson.Gson;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
/**
 * 设备激活
 */
public class ActivationActivity extends IBaseActivity<LoginPresenter> implements ILoginView{

	@BindView(R.id.login_name)
	XEditText loginName;
	@BindView(R.id.login_pwd)
	XEditText loginPwd;
	@BindView(R.id.activiation)
	Button activiation;

	@Override
	protected int setLayout() {
		return R.layout.activity_activation;
	}

	@Override
	protected void init() {

	}

	@Override
	protected void setListener() {
		activiation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				String name = loginName.getText().toString().trim();
//				name = "13300000000";
				if (name == null && TextUtils.isEmpty(name)) {
					Toast.makeText(ActivationActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
					return;
				}
				String pwd = loginPwd.getText().toString().trim();
//				pwd = "123456";
				if (pwd == null && TextUtils.isEmpty(pwd)) {
					Toast.makeText(ActivationActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				mPresenter.doDeviceActivation(name,pwd);
			}
		});
	}

	@Override
	protected LoginPresenter createPresenter() {
		return new LoginPresenter();
	}



	@Override
	public Activity getActivity() {
		return this;
	}


	@Override
	public void showDialog() {

	}

	@Override
	public void dismissDialog() {

	}

	@Override
	public void loginSuccess(BaseRespone respone) {

	}

	@Override
	public void loginFail(Throwable error, Integer code, String msg) {

	}

	@Override
	public void activationSuccess(BaseRespone respone) {
        LogUtils.e("---------",""+(new Gson().toJson(respone)));
		if (respone.getData() != null) {
			LoginRespone lr = (LoginRespone) respone.getData();
			if (lr.getSysRes() != null) {
				User user = lr.getSysRes();
				UserSql.deleteAllUser();
				UserSql.insertUser(CopyObject.copyUser(user));
				Entiy.GUN_COLUMN = user.getTrunkPipeMaxNum();
				BaseApp.setUser(user);
			}
			if (lr.getValveDeviceInfos() != null) {
				DeviceInfoSql.delAllDeviceList();
				List<DeviceInfo> deviceInfos = lr.getValveDeviceInfos();
				int num = deviceInfos.size();
				for (int i = 0; i < num; i++) {
					DeviceInfo deviceInfo  = CopyObject.copyData(deviceInfos.get(i));
					deviceInfo.setProtocalId(Entiy.createProtocalId(deviceInfo.getDeviceSort()));
					ControlInfo controlInfo0 = deviceInfo.getValveDeviceSwitchList().get(0);
//					controlInfo0.setValveName(deviceInfo.getDeviceSort() + "_1");
//					controlInfo0.setValveAlias(deviceInfo.getDeviceSort() + "_" + controlInfo0.getValveName());
					controlInfo0.setValveId(deviceInfo.getDeviceSort() * 2 - 1);
					controlInfo0.setProtocalId("0");
					controlInfo0.setDeviceProtocalId(Entiy.createProtocalId(deviceInfo.getDeviceSort()));
					ControlInfo controlInfo1 = deviceInfo.getValveDeviceSwitchList().get(1);
//					controlInfo1.setValveName(deviceInfo.getDeviceSort() + "_2");
//					controlInfo1.setValveAlias(deviceInfo.getDeviceSort() + "_" + controlInfo1.getValveName());
					controlInfo1.setValveId(deviceInfo.getDeviceSort() * 2);
					controlInfo1.setProtocalId("1");
					controlInfo1.setDeviceProtocalId(Entiy.createProtocalId(deviceInfo.getDeviceSort()));
					DeviceInfoSql.insertDevice(deviceInfo);
				}
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
			if (lr.getDeviceGroupList() != null) {
				List<GroupInfo>groupInfos = lr.getDeviceGroupList();
				LogUtils.e("---------",""+(new Gson().toJson(lr.getDeviceGroupList())));
				int size = groupInfos.size();
				for (int i = 0; i < size; i++) {
					List<LevelInfo>levelInfos = LevelInfoSql.queryUserLevelInfoListByGroup(false);
					if (levelInfos != null && levelInfos.size() > 0) {
						levelInfos.get(0).setIsGroupUse(true);
						LevelInfoSql.updateLevelInfo(levelInfos.get(0));
						GroupInfoSql.insertGroup(CopyObject.copyGroup(groupInfos.get(i)));
					}
				}
			}
			startActivity(new Intent(ActivationActivity.this, LoginActivity.class));
			finish();
		}
	}

	@Override
	public void activationFail(Throwable error, Integer code, String msg) {
        LogUtils.e("---------",""+msg);
		ToastUtils.showLongToast(""+msg);

//		User user = new User();
//		user.setUserId(155l);
//		user.setAvatar("");
//		user.setLoginName("13300000000");
//		user.setParentId(123456l);
//		user.setPhonenumber("13300000000");
//		user.setProjectName(Entiy.GUN_NAME);
//		user.setProjectId("00006");
//		user.setProjectGroupId("00006");
//		user.setPileOutNum(Entiy.GUN_COLUMN);
//		user.setTrunkPipeNum(Entiy.GUN_COLUMN);
//		user.setMemberId(152l);
//		user.setPassword("123456");
//		UserSql.insertUser(user);
//		BaseApp.setUser(user);
//		int num = user.getPileOutNum()*user.getTrunkPipeNum();
//
//		Entiy.GUN_COLUMN = user.getPileOutNum();
//
//		List<DeviceInfo>deviceInfos = new ArrayList<>();
//		if(DeviceInfoSql.queryDeviceCount() <= 0) {
//			for (int i = 0 ; i < num; i++) {
//				DeviceInfo deviceInfo = new DeviceInfo();
//				deviceInfo.setDeviceName("设备_"+(i+1));
//				deviceInfo.setDeviceStatus(0);
//				deviceInfo.setDeviceSort(i+1);
//				deviceInfo.setDeviceId(i+1);
//				deviceInfo.setProtocalId(Entiy.createProtocalId(i+1));
//				ArrayList<ControlInfo>controlInfos = new ArrayList<>();
//				ControlInfo controlInfo0 = new ControlInfo(deviceInfo.getDeviceId(),deviceInfo.getDeviceSort()+"_1", 0);
//				controlInfo0.setValveId(deviceInfo.getDeviceSort()*2-1);
//				controlInfo0.setProtocalId("0");
//				controlInfo0.setDeviceProtocalId("10000");
//				controlInfo0.setValveAlias(deviceInfo.getDeviceSort()+"-"+controlInfo0.getValveName());
//				ControlInfo controlInfo1 = new ControlInfo(deviceInfo.getDeviceId(),deviceInfo.getDeviceSort()+"_2",0);
//				controlInfo1.setValveAlias(deviceInfo.getDeviceSort()+"_"+controlInfo1.getValveName());
//				controlInfo1.setValveId(deviceInfo.getDeviceSort()*2);
//				controlInfo1.setProtocalId("1");
//				controlInfo1.setDeviceProtocalId("10000");
//				controlInfos.add(controlInfo0);
//				controlInfos.add(controlInfo1);
//				deviceInfo.setValveDeviceSwitchList(controlInfos);
//				deviceInfos.add(deviceInfo);
////				if (i < 9) {
////					controlInfo0.setValveStatus(2);
////					controlInfo1.setValveStatus(2);
////				}
////
////				if(i == 1 || i == 3 || i == 5 || i == 11) {
////					deviceInfo.setElectricQuantity(i);
////					controlInfo1.setValveStatus(3);
////				}else {
////					deviceInfo.setElectricQuantity(88);
////				}
//
//			}
//			DeviceInfoSql.insertDeviceInfoList(deviceInfos);
//		}
//
//
//		if (LevelInfoSql.queryLevelInfoList().size() == 0) {
//			List<LevelInfo> levelInfos = new ArrayList<>();
//			for (int i = 1; i < 200; i++) {
//				LevelInfo info = new LevelInfo();
//				info.setLevelId(i);
//				info.setIsGroupUse(false);
//				info.setIsLevelUse(false);
//				levelInfos.add(info);
//			}
//			LevelInfoSql.insertLevelInfoList(levelInfos);
//		}
//
//		startActivity(new Intent(ActivationActivity.this, MainActivity.class));
//		finish();
//		ToastUtils.showLongToast(""+msg);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onElecEvent(ElecEvent elecEvent) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mqttAndroidClient.unregisterResources();
	}
}
