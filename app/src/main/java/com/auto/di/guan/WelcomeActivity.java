package com.auto.di.guan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.auto.di.guan.entity.Entiy;

public class WelcomeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		// 如果数据表里面没有用户信息
		if (BaseApp.getUser() != null) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Entiy.GUN_COLUMN = 10;
					startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
					finish();
				}
			},2000);
		}else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(WelcomeActivity.this, ActivationActivity.class));
					finish();
				}
			},2000);
		}
	}


}
