package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.R;
import com.auto.di.guan.rtm.MessageSend;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.auto.di.guan.view.XEditText;

/**
 * 密码管理-输入确认密码
 */
public class InputPasswordDialog extends Dialog {

    private static InputPasswordDialog dialog;
    private Context context;
    private String inputPassword;
    private LinearLayout linearLayout;
    private ImageView handoverShow;

    public InputPasswordDialog(Context context) {
        super(context, R.style.MyDialog);
        init();
    }


    private void init() {
        setContentView(R.layout.dialog_input_password_manager);
        //弹出后会点击屏幕或物理返回键dialog消失
        setCancelable(false);
        linearLayout = findViewById(R.id.hondover_input_password_LinearLayout);
        XEditText xEditText = findViewById(R.id.handover_input_password_edittext);
        xEditText.clearFocus();
        TextView ok = findViewById(R.id.handover_input_password_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }

                inputPassword = xEditText.getTextTrimmed();
                if (TextUtils.isEmpty(inputPassword)) {
                    ToastUtils.showToast("登录密码不能为空");
                    return;
                }
                //隐藏软键盘
                if(!inputPassword.equals(BaseApp.getUser().getPassword())) {
                    LogUtils.e("", "input = "+inputPassword + " ---"+BaseApp.getUser().getPassword());
                    ToastUtils.showToast("登录密码错误");
                    return;
                }
                MessageSend.syncLogout();
                dismiss();
            }
        });

        handoverShow = findViewById(R.id.handover_show);
        findViewById(R.id.handover_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                handoverShow.setVisibility(View.VISIBLE);
            }
        });
        handoverShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                handoverShow.setVisibility(View.GONE);
            }
        });


    }


    public static void show(Context context) {
        if(null == context){
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                return;
            }
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new InputPasswordDialog(context);
        dialog.show();
    }



    public static void dismiss(Context context) {
        try {
            if(null ==context){
                return;
            }

            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    dialog = null;
                    return;
                }
            }
            if (dialog != null && dialog.isShowing()) {
                Context loadContext = dialog.getContext();
                if (loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing() || ((Activity) context).isDestroyed()) {
                        dialog = null;
                        return;
                    }
                }
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
        } finally {
            dialog = null;
        }
    }
}