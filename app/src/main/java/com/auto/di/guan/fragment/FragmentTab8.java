package com.auto.di.guan.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.Fragment8LeftAdapter;
import com.auto.di.guan.adapter.Fragment8RightAdapter;
import com.auto.di.guan.api.ApiEntiy;
import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.model.respone.EDepthRespone;
import com.auto.di.guan.basemodel.model.respone.EDeviceDataRespone;
import com.auto.di.guan.basemodel.model.respone.EDeviceRespone;
import com.auto.di.guan.basemodel.model.respone.ERespone;
import com.auto.di.guan.basemodel.model.respone.MeteoRespone;
import com.auto.di.guan.dialog.DialogContent;
import com.auto.di.guan.dialog.InputDialog;
import com.auto.di.guan.dialog.InputPasswordDialog;
import com.auto.di.guan.dialog.LoadingDialog;
import com.auto.di.guan.dialog.OnDialogClick;
import com.auto.di.guan.event.ActivityItemEvent;
import com.auto.di.guan.event.LoginEvent;
import com.auto.di.guan.event.TabClickEvent;
import com.auto.di.guan.rtm.MessageSend;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NewApiUtil;
import com.auto.di.guan.utils.ShareUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentTab8 extends BaseFragment implements View.OnClickListener{


	private LoadingDialog mLoadingDailog;

	private View view;

	LinearLayout addBtn_layout;
	ImageButton add_memto;
	ImageButton add_poi;
	ImageView fragment_8_image;

	RecyclerView recyclerViewLeft;
	Fragment8LeftAdapter leftAdapter;

	RecyclerView recyclerViewRight;
	Fragment8RightAdapter rightAdapter;

	List<MeteoRespone> meteoRespones = new ArrayList<>();
	List<EDepthRespone> eDepthRespones = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_8, null);

		EventBus.getDefault().register(this);
		fragment_8_image = view.findViewById(R.id.fragment_8_image);
		fragment_8_image.setVisibility(View.VISIBLE);
		addBtn_layout = view.findViewById(R.id.addBtn_layout);
		addBtn_layout.setVisibility(View.VISIBLE);
		recyclerViewLeft = view.findViewById(R.id.fragment_8_left);
		recyclerViewLeft.setLayoutManager(new LinearLayoutManager(getContext()));

		String local = ShareUtil.getFragmentTab0List();
		if (TextUtils.isEmpty(local)) {
			final MeteoRespone meteoRespone = new MeteoRespone();
			meteoRespone.setSn("殇情信息");
			meteoRespone.setSle(true);
			meteoRespones.add(meteoRespone);
			EDepthRespone eDepthRespone = new EDepthRespone();
			eDepthRespone.setType(ApiEntiy.ITEM_TYPE_0);
			eDepthRespones.add(eDepthRespone);
		}else {
			eDepthRespones.clear();
			LogUtils.e("fragment8", local);
			List<MeteoRespone> list = new Gson().fromJson(local, new TypeToken<List<MeteoRespone>>() {}.getType());
			if (list != null) {
				meteoRespones.addAll(list);
				int size= meteoRespones.size();
				for (int i = 0; i < size; i++) {
					if (i == 0) {
						meteoRespones.get(0).setSle(true);
					}else {
						meteoRespones.get(0).setSle(false);
					}
				}
			}
			NewApiUtil.initToken(new HttpManager.OnResultListener() {
				@Override
				public void onSuccess(BaseRespone t) {

				}

				@Override
				public void onError(Throwable error, Integer code, String msg) {

				}
			});

		}

		leftAdapter = new Fragment8LeftAdapter(meteoRespones);
		recyclerViewLeft.setAdapter(leftAdapter);

		recyclerViewLeft.addItemDecoration(new SpacesItemDecoration(30));

		recyclerViewRight = view.findViewById(R.id.fragment_8_right);
		recyclerViewRight.setLayoutManager(new LinearLayoutManager(getContext()));
		rightAdapter = new Fragment8RightAdapter(eDepthRespones);
		recyclerViewRight.setAdapter(rightAdapter);
		leftAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				if (position == 0) {
					fragment_8_image.setVisibility(View.VISIBLE);
					addBtn_layout.setVisibility(View.VISIBLE);
				}else {
					fragment_8_image.setVisibility(View.GONE);
					addBtn_layout.setVisibility(View.GONE);
				}
				onItemClickListen(position);
			}
		});

		add_memto = view.findViewById(R.id.add_memto);
		add_poi = view.findViewById(R.id.add_poi);
		add_memto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				addDevice("添加气象信息", "19101600107861");
			}
		});


		add_poi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addDevice("添加墒情信息", "18121700094125");
			}
		});
		/**
		 *   首次进来同步列表信息
		 */
		MessageSend.syncListItem(meteoRespones, eDepthRespones);
		return view;
	}

	@Override
	public void refreshData() {

	}

	@Override
	public void onClick(View v) {
//		startActivity(new Intent(getContext(), WebviewActivity.class));
	}


	public void addDevice(String title, String device) {
		DialogContent dialogContent = new DialogContent();
		dialogContent.setDesc(title);
		dialogContent.setCancle(device);
		InputDialog.show(getActivity(), dialogContent, new OnDialogClick() {
			@Override
			public void onDialogOkClick(final String value) {
				NewApiUtil.getToken(value, new HttpManager.OnResultListener() {
					@Override
					public void onSuccess(BaseRespone t) {
						if (t != null) {
							MeteoRespone meteoRespone = (MeteoRespone) t;
							String sn = meteoRespone.getSn();
							int size = meteoRespones.size();
							if (size > 1) {
								Iterator<MeteoRespone> iterator = meteoRespones.iterator();
								while (iterator.hasNext()) {
									MeteoRespone mRespone = iterator.next();
									if (sn.equals(mRespone.getSn())) {
										iterator.remove();
									}
								}
							}
							meteoRespones.add(meteoRespone);
							leftAdapter.notifyDataSetChanged();
							/**
							 * 添加列表同步
							 */
							MessageSend.syncListItem(meteoRespones, eDepthRespones);
						}
					}

					@Override
					public void onError(Throwable error, Integer code, String msg) {

					}
				});
			}

			@Override
			public void onDialogCloseClick(String value) {

			}
		});
	}

	public void getDeviceInfo(String device, final boolean isWather) {
		showDialog();
		NewApiUtil.getDeviceData(device, new HttpManager.OnResultListener() {
			@Override
			public void onSuccess(BaseRespone t) {
				dismissDialog();
				ERespone eRespone = (ERespone) t;
				if (eRespone != null) {
					if (eRespone.getList() != null) {
						int size = eRespone.getList().size();
						eDepthRespones.clear();

						EDepthRespone eDepthRespone = new EDepthRespone();
						if (isWather) {
							eDepthRespone.setType(ApiEntiy.ITEM_TYPE_1);
						}else {
							eDepthRespone.setType(ApiEntiy.ITEM_TYPE_3);
						}
						eDepthRespones.add(eDepthRespone);

						for (int i = 0; i < size; i++) {
							EDeviceRespone eDeviceRespone = eRespone.getList().get(i);
							initDepth(eDeviceRespone, isWather);
						}
						rightAdapter.notifyDataSetChanged();
						/**
						 *   添加详情同步
						 */
						MessageSend.syncListItem(meteoRespones, eDepthRespones);
					}
				}
			}

			@Override
			public void onError(Throwable error, Integer code, String msg) {
				dismissDialog();
			}
		});
	}


	public void initDepth(EDeviceRespone eDeviceRespone, boolean isWather) {
		EDeviceDataRespone eDeviceDataRespone = eDeviceRespone.getValues();
		EDepthRespone depth0 = eDeviceDataRespone.getDepth0();
		if (depth0 != null) {
			depth0.setDatetime(eDeviceRespone.getDatetime());
			depth0.setDapthName(ApiEntiy.DEPTH_0[0]);
			if (isWather) {
				depth0.setType(ApiEntiy.ITEM_TYPE_2);
			} else {
				depth0.setType(ApiEntiy.ITEM_TYPE_4);
			}
			eDepthRespones.add(depth0);
		}

		if (isWather) {
			return;
		}

		EDepthRespone depth10 = eDeviceDataRespone.getDepth10();
		if (depth10 != null) {
			depth10.setDatetime(eDeviceRespone.getDatetime());
			depth10.setDapthName(ApiEntiy.DEPTH_10[0]);
			depth10.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth10);
		}

		EDepthRespone depth20 = eDeviceDataRespone.getDepth20();
		if (depth20 != null) {
			depth20.setDatetime(eDeviceRespone.getDatetime());
			depth20.setDapthName(ApiEntiy.DEPTH_20[0]);
			depth20.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth20);
		}

		EDepthRespone depth30 = eDeviceDataRespone.getDepth30();
		if (depth30 != null) {
			depth30.setDatetime(eDeviceRespone.getDatetime());
			depth30.setDapthName(ApiEntiy.DEPTH_30[0]);
			depth30.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth30);
		}
		EDepthRespone depth40 = eDeviceDataRespone.getDepth40();
		if (depth40 != null) {
			depth40.setDatetime(eDeviceRespone.getDatetime());
			depth40.setDapthName(ApiEntiy.DEPTH_40[0]);
			depth40.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth40);
		}
		EDepthRespone depth50 = eDeviceDataRespone.getDepth50();
		if (depth50 != null) {
			depth50.setDatetime(eDeviceRespone.getDatetime());
			depth50.setDapthName(ApiEntiy.DEPTH_50[0]);
			depth50.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth50);
		}
		EDepthRespone depth60 = eDeviceDataRespone.getDepth60();
		if (depth60 != null) {
			depth60.setDatetime(eDeviceRespone.getDatetime());
			depth60.setDapthName(ApiEntiy.DEPTH_60[0]);
			depth60.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth60);
		}
		EDepthRespone depth70 = eDeviceDataRespone.getDepth70();
		if (depth70 != null) {
			depth70.setDatetime(eDeviceRespone.getDatetime());
			depth70.setDapthName(ApiEntiy.DEPTH_70[0]);
			depth70.setType(ApiEntiy.ITEM_TYPE_4);
			eDepthRespones.add(depth70);
		}
	}

	public void webAddDevice(String device) {
		showDialog();
		NewApiUtil.getToken(device, new HttpManager.OnResultListener() {
			@Override
			public void onSuccess(BaseRespone t) {
				dismissDialog();
				if (t != null) {
					MeteoRespone meteoRespone = (MeteoRespone) t;
					String sn = meteoRespone.getSn();
					int size = meteoRespones.size();
					if (size > 1) {
						Iterator<MeteoRespone> iterator = meteoRespones.iterator();
						while (iterator.hasNext()) {
							MeteoRespone mRespone = iterator.next();
							if (sn.equals(mRespone.getSn())) {
								iterator.remove();
							}
						}
					}
					meteoRespones.add(meteoRespone);
					leftAdapter.notifyDataSetChanged();
					/**
					 *   同步详情信息
					 */
					MessageSend.syncListItem(meteoRespones, eDepthRespones);
				}
			}

			@Override
			public void onError(Throwable error, Integer code, String msg) {
				dismissDialog();
			}
		});
	}

	/**
	 *         处理web端相关操作
	 * @param event
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onActivityItemEvent(ActivityItemEvent event) {
		LogUtils.e("FragmentTab8", "sn ="+event.getSn());
		if (event == null) {
			return;
		}

		/**
		 *  item 点击事件
		 */
		if (TextUtils.isEmpty(event.getSn())) {
			/**
			 *   来自web端的点击事件
			 */
			onItemClickListen(event.getIndex());
		}else {
			webAddDevice(event.getSn());
		}
	};


	public void onItemClickListen(int position) {
		int size = meteoRespones.size();
		if (position > size) {
			return;
		}
		eDepthRespones.clear();
		if (position == 0) {
			EDepthRespone eDepthRespone = new EDepthRespone();
			eDepthRespone.setType(ApiEntiy.ITEM_TYPE_0);
			eDepthRespones.add(eDepthRespone);
			rightAdapter.notifyDataSetChanged();

		} else {
			MeteoRespone mRespone = meteoRespones.get(position);
			boolean isWather = false;
			if (ApiEntiy.DEVICE_TYPE.equals(mRespone.getType())) {
				isWather = true;
			}
			getDeviceInfo(mRespone.getSn(), isWather);
		}

		for(int i = 0; i < size; i++) {
			if (i == position) {
				meteoRespones.get(i).setSle(true);
			}else {
				meteoRespones.get(i).setSle(false);
			}
		}

		LogUtils.e("--", ""+new Gson().toJson(meteoRespones));
		leftAdapter.notifyDataSetChanged();
	}




	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			// Add top margin only for the first item to avoid double space between items
			if (parent.getChildPosition(view) != 0)
				outRect.top = space;
		}
	}


	public void showDialog() {
		if (null == mLoadingDailog) {
			showWaitingDialog("");
		}
		if (!mLoadingDailog.isShowing()) {
			mLoadingDailog.show();
		}
	}

	/**
	 * 显示等待提示框
	 */
	public Dialog showWaitingDialog(String tip) {
		mLoadingDailog = new LoadingDialog(activity, R.style.CustomDialog);
		return mLoadingDailog;
	}

	public void dismissDialog() {
		hideWaitingDialog();
	}

	/**
	 * 隐藏等待提示框
	 */
	public void hideWaitingDialog() {
		if (mLoadingDailog != null && mLoadingDailog.isShowing()) {
			mLoadingDailog.dismiss();
			mLoadingDailog = null;
		}
	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onTabClickEvent(TabClickEvent event) {
		if (event == null) {
			return;
		}

		if (event.getIndex() == 0) {
			MessageSend.syncListItem(meteoRespones, eDepthRespones);
			fragment_8_image.setVisibility(View.VISIBLE);
			addBtn_layout.setVisibility(View.VISIBLE);
		}else {
			fragment_8_image.setVisibility(View.GONE);
			addBtn_layout.setVisibility(View.GONE);
		}
	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onLoginEvent(LoginEvent event) {
		if (event != null && event.isLogin()) {
			MessageSend.syncListItem(meteoRespones, eDepthRespones);
		}
	}

}
