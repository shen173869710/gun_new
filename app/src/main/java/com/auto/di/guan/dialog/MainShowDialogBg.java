package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.utils.NoFastClickUtils;


public class MainShowDialogBg extends Dialog {

	public Button main_custom_cancle;
	public Button main_custom_sure;
	public TextView main_custom_msg;
	public TextView main_custom_title;


	public MainShowDialogBg(Context context) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	public MainShowDialogBg(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
		super(context, R.style.UpdateDialog);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		setCustomView();
	}

	public MainShowDialogBg(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(
				R.layout.main_custom_dialog, null);
		main_custom_cancle = (Button) mView
				.findViewById(R.id.main_custom_cancle);
		View viewById =  mView.findViewById(R.id.main_custom_bottom);
		viewById.setVisibility(View.GONE);

		main_custom_sure = (Button) mView.findViewById(R.id.main_custom_sure);
		main_custom_msg = (TextView) mView.findViewById(R.id.main_custom_msg);
		main_custom_title = (TextView) mView
				.findViewById(R.id.main_custom_title);
		mView.setBackgroundResource(R.drawable.bg_d_rect_border);
		super.setContentView(mView);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		super.setCanceledOnTouchOutside(cancel);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	public void setOnPositiveListener(View.OnClickListener listener) {
		main_custom_sure.setOnClickListener(listener);
	}

	public void setOnNegativeListener(View.OnClickListener listener) {
		main_custom_cancle.setOnClickListener(listener);
	}


	public static void ShowDialog(final Activity context, String title,
			String msg, final View.OnClickListener listener) {
		final MainShowDialogBg dialog = new MainShowDialogBg(context);
		dialog.main_custom_title.setText(title);
		dialog.main_custom_msg.setText(msg);
		dialog.setOnPositiveListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				if(listener != null)
					listener.onClick(v);
				dialog.dismiss();
			}
		});

		dialog.setOnNegativeListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();// 获取屏幕分辨率
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.width = dm.widthPixels * 5 / 10;
		dialog.show();
	}
}
