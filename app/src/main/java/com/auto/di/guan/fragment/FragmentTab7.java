package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.PumpLeftAdapter;
import com.auto.di.guan.dialog.DialogUtil;
import com.auto.di.guan.dialog.OnDialogClick;
import com.auto.di.guan.entity.BengOptionEvent;
import com.auto.di.guan.rtm.MessageEntiy;
import com.auto.di.guan.rtm.MessageSend;
import com.auto.di.guan.socket.SocketEntiy;
import com.auto.di.guan.socket.SocketResult;
import com.auto.di.guan.socket.UdpReceiveThread;
import com.auto.di.guan.socket.UdpSendThread;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
/**
 *
 *
 *            //                     项目ID 泵名称
 * //      关        byte[] buf = "gb 20000   0               ".getBytes();
 *   //    开        byte[] buf = "kb 20000   0               ".getBytes();
 * //            byte[] buf = "cb 20000 0               ".getBytes();
 *
 *  *                   0    不查询
 *  *                   1 3  1分钟
 *  *                   2    10分钟
 *
 */
public class FragmentTab7 extends BaseFragment {
	private final String TAG = "FragmentTab6";

	private View view;
	private PumpLeftAdapter adapter;
	private ArrayList<SocketResult> infos = new ArrayList<>();
	private RecyclerView list;

	private Button fragment_7_del;
	private Button fragment_7_add;


	private boolean isStart = false;

	private android.os.Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj == null) {
				return;
			}
			LogUtils.e(TAG, "handle what = "+msg.what);
			if (msg.what == SocketEntiy.STATUS_2_HANDLER) {
				SocketResult result = (SocketResult) msg.obj;
				new UdpSendThread(SocketEntiy.getSockenRead(result.getNameCode())).start();
			}else if (msg.what == SocketEntiy.STATUS_1_3_HANDLER) {
				SocketResult result = (SocketResult) msg.obj;
				new UdpSendThread(SocketEntiy.getSockenRead(result.getNameCode())).start();
			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_7, null);
		EventBus.getDefault().register(this);

		list = view.findViewById(R.id.fragment_7_list);
		list.setLayoutManager(new LinearLayoutManager(activity));

		fragment_7_del = view.findViewById(R.id.fragment_7_del);
		fragment_7_add = view.findViewById(R.id.fragment_7_add);

		adapter = new PumpLeftAdapter(infos);
		list.setAdapter(adapter);
		for (int i= 0; i < 2; i++) {
			initData(i+"");
		}
		setListen();
		return view;
	}

	private void initData(String name) {
		SocketResult result = new SocketResult();
		result.setName("水泵" + name);
		result.setNameCode(name);
		result.setVoltage("电压");
		result.setElectricity("电流");
		result.setStatus("状态");
		result.setErrorCode("错误");
		result.setVoltageValue("");
		result.setElectricityValue("");
		result.setStatusValue("");
		result.setErrorCodeValue("");
		infos.add(result);
		new UdpSendThread(SocketEntiy.getSockenRead(result.getNameCode())).start();

	}

	public void setListen() {
		adapter.addChildClickViewIds(R.id.item_open, R.id.item_close, R.id.item_1_value);
		adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				SocketResult result = infos.get(position);
				int id = view.getId();
				if (id == R.id.item_open) {
					DialogUtil.startSocket(getActivity(), result.getName(), new OnDialogClick() {
						@Override
						public void onDialogOkClick(String value) {
							new UdpSendThread(SocketEntiy.getSockenOpen(result.getNameCode())).start();
						}

						@Override
						public void onDialogCloseClick(String value) {

						}
					});

				}else if (id == R.id.item_close) {
					DialogUtil.closeSocket(getActivity(), result.getName(), new OnDialogClick() {
						@Override
						public void onDialogOkClick(String value) {
							new UdpSendThread(SocketEntiy.getSockenClose(result.getNameCode())).start();
						}

						@Override
						public void onDialogCloseClick(String value) {

						}
					});
				}else if (id == R.id.item_1_value) {
					//new UdpSendThread(SocketEntiy.getSockenRead(result.getNameCode())).start();
				}
			}
		});


		fragment_7_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int size = infos.size() - 1;
				if (size < 0) {
					ToastUtils.showLongToast("操作失败");
					return;
				}

				DialogUtil.delBeng(activity, String.valueOf(size), new OnDialogClick() {
					@Override
					public void onDialogOkClick(String value) {
						infos.remove(size);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onDialogCloseClick(String value) {

					}
				});
			}
		});

		fragment_7_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil.addBeng(activity, infos.size() + "", new OnDialogClick() {
					@Override
					public void onDialogOkClick(String value) {
						int size = infos.size();
						initData(String.valueOf(size));
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onDialogCloseClick(String value) {

					}
				});
			}
		});
		new UdpReceiveThread().start();
	}

	@Override
	public void refreshData() {

		LogUtils.e(TAG, "refreshData");
		if (handler != null) {
			handler.removeMessages(SocketEntiy.STATUS_1_3_HANDLER);
			handler.removeMessages(SocketEntiy.STATUS_2_HANDLER);
		}

		int size = infos.size();
		for (int i = 0; i < size; i++) {
			SocketResult result = infos.get(i);
			new UdpSendThread(SocketEntiy.getSockenRead(result.getNameCode())).start();
		}
	}


	/**
	 *        自动轮灌组状态更新
	 * @param event
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSocketEvent(SocketResult event) {
		if (event != null) {
			LogUtils.e(TAG, "收到数据" + new Gson().toJson(event));
		}

		int size = infos.size();
		for (int i = 0; i < size; i++) {
			SocketResult result = infos.get(i);
			if (result.getNameCode().equals(event.getNameCode())) {
				result.setStatusValue(event.getStatusValue());
				result.setVoltageValue(event.getVoltageValue());
				result.setElectricityValue(event.getElectricityValue());
				result.setErrorCodeValue(event.getErrorCodeValue());

				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}

				if ("1".equals(result.getStatusValue())
						|| "3".equals(result.getStatusValue())) {
					Message message = new Message();
					message.what = SocketEntiy.STATUS_1_3_HANDLER;
					message.obj = result;
					if (handler != null) {
						handler.sendMessageDelayed(message, SocketEntiy.STATUS_1_3);
					}
				}else if ("2".equals(result.getStatusValue())) {
					Message message = new Message();
					message.what = SocketEntiy.STATUS_2_HANDLER;
					message.obj = result;
					if (handler != null) {
						handler.sendMessageDelayed(message, SocketEntiy.STATUS_2);
					}
				}

			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onBengOptionEvent(BengOptionEvent event) {
		if (infos != null && infos.size() > event.getPostion()) {
			if (event.isOpen()) {
//				new UdpSendThread(SocketEntiy.getSockenOpen(infos.get(event.getPostion()).getNameCode())).start();
				LogUtils.e(TAG, "开泵"+infos.get(event.getPostion()).getNameCode());
				infos.get(event.getPostion()).setStatusValue("2");
				MessageSend.syncBengInfo(infos,MessageEntiy.TYPE_BENG_CLOSE);
			}else {
//				new UdpSendThread(SocketEntiy.getSockenClose(infos.get(event.getPostion()).getNameCode())).start();
				LogUtils.e(TAG, "关泵"+infos.get(event.getPostion()).getNameCode());
				infos.get(event.getPostion()).setStatusValue("3");
				MessageSend.syncBengInfo(infos, MessageEntiy.TYPE_BENG_CLOSE);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (handler != null) {
			handler.removeMessages(SocketEntiy.STATUS_1_3_HANDLER);
			handler.removeMessages(SocketEntiy.STATUS_2_HANDLER);
		}
		EventBus.getDefault().unregister(this);
	}
}
