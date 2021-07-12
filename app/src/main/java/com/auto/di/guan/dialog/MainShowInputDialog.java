package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.R;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.auto.di.guan.view.XEditText;


public class MainShowInputDialog extends Dialog {

    public Button main_custom_cancle;
    public Button main_custom_sure;
    public TextView main_custom_title;
    public XEditText main_custom_edit_phone;


    public MainShowInputDialog(Context context) {
        super(context, R.style.UpdateDialog);
        setCustomView();
    }

    public MainShowInputDialog(Context context, boolean cancelable,
                               OnCancelListener cancelListener) {
        super(context, R.style.UpdateDialog);
        this.setCancelable(cancelable);
        this.setOnCancelListener(cancelListener);
        setCustomView();
    }

    public MainShowInputDialog(Context context, int theme) {
        super(context, R.style.UpdateDialog);
        setCustomView();
    }

    private void setCustomView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.main_custom_input_dialog, null);
        main_custom_cancle = (Button) mView.findViewById(R.id.cancle);
        main_custom_sure = (Button) mView.findViewById(R.id.ok);
        main_custom_title = (TextView) mView.findViewById(R.id.title);
        main_custom_edit_phone = (XEditText) mView.findViewById(R.id.input);
        super.setContentView(mView);
    }


    public void setOnPositiveListener(View.OnClickListener listener) {
        main_custom_sure.setOnClickListener(listener);
    }

    public void setOnNegativeListener(View.OnClickListener listener) {
        main_custom_cancle.setOnClickListener(listener);
    }


    public static void ShowDialog(final Activity context, String title ,final View.OnClickListener listener) {
        final MainShowInputDialog dialog = new MainShowInputDialog(context);
        dialog.main_custom_title.setText(title);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }
                String inputPassword = dialog.main_custom_edit_phone.getTextTrimmed();
                if (TextUtils.isEmpty(inputPassword)) {
                    ToastUtils.showToast("登录密码不能为空");
                    return;
                }
                //隐藏软键盘
                if(!inputPassword.equals(BaseApp.getUser().getPassword())) {
                    LogUtils.e("-------", "input = "+inputPassword + " ---"+BaseApp.getUser().getPassword());
                    ToastUtils.showToast("登录密码错误");
                    return;
                }

                if (listener != null) {
                    listener.onClick(v);
                }
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
        lay.width = dm.widthPixels * 3 / 10;
        dialog.show();
    }
}
