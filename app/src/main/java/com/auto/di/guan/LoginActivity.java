package com.auto.di.guan;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.presenter.LoginPresenter;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.Task;
import com.auto.di.guan.view.XEditText;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class LoginActivity extends IBaseActivity<LoginPresenter> implements ILoginView, View.OnClickListener {

    @BindView(R.id.user_login_name)
    XEditText userLoginName;
    @BindView(R.id.user_login_pwd)
    XEditText userLoginPwd;
    @BindView(R.id.user_login)
    Button userLogin;
    String pwd;
    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
//        String id = userLoginName.getText().toString().trim();
////        id = "swlzc1";
//        if (TextUtils.isEmpty(id)) {
//            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
//            return;
//        }
//       pwd = userLoginPwd.getText().toString().trim();
////        pwd = "123456";
//        if (TextUtils.isEmpty(pwd)) {
//            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
//            return;
//        }
//        mPresenter.doLogin(id, pwd);
    }

    @Override
    protected void setListener() {
        userLogin.setOnClickListener(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {
        String id = userLoginName.getText().toString().trim();
//        id = "swlzc1";
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }
        pwd = userLoginPwd.getText().toString().trim();
//        pwd = "swlzc123";
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.doLogin(id, pwd);
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void loginSuccess(BaseRespone respone) {
//        LogUtils.e("---------", "" + (new Gson().toJson(respone)));
        if (respone.getData() != null) {
            User user = (User) respone.getData();
            user.setPassword(pwd);
            UserSql.updateUser(user);
            BaseApp.setUser(user);

            Entiy.GUN_COLUMN = user.getTrunkPipeMaxNum();
//            Entiy.GUN_COLUMN = 16;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void loginFail(Throwable error, Integer code, String msg) {
        LogUtils.e("msg", "doHttpTaskWithDialog==onError===" +msg);

//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void activationSuccess(BaseRespone respone) {

    }

    @Override
    public void activationFail(Throwable error, Integer code, String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onElecEvent(Task event) {

    }
}
