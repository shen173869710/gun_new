package com.auto.di.guan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.auto.di.guan.BaseApp;

public class ShareUtil {
	public static final String TAG = "ShareUtil";

	public static final String COM_AUTO_DI_GUAN= "com.auto.di.guan";

	public static final String FRAGMENT_TAB_0_LIST = "fragment_tab_0_lit";


	public static void setFragmentTab0List(String vaule) {
		LogUtils.e(TAG, "set ="+vaule );
	 setStringLocalValue(BaseApp.getContext(), FRAGMENT_TAB_0_LIST, vaule);
	}

	public static String getFragmentTab0List() {
		LogUtils.e(TAG, "get ="+getStringLocalValue(BaseApp.getContext(), FRAGMENT_TAB_0_LIST) );
		return getStringLocalValue(BaseApp.getContext(), FRAGMENT_TAB_0_LIST);
	}

	private static SharedPreferences getSharedPreferences(Context mContext){
		return mContext.getSharedPreferences(COM_AUTO_DI_GUAN, 0);
	}
	
	public static int getIntLocalValue(Context mContext, String key) {
		SharedPreferences sp =getSharedPreferences(mContext);
		return sp.getInt(key, -1);
	}


	public static String getStrLocalValue(Context mContext, String key) {
		SharedPreferences sp =getSharedPreferences(mContext);
		return sp.getString(key, null);
	}

	public static void setIntStringValue(Context mContext,String key,String value){
		SharedPreferences sp =getSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}


	public static void setIntLocalValue(Context mContext,String key,int value){
		SharedPreferences sp =getSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static long getLongLocalValue(Context mContext, String key) {
		SharedPreferences sp =getSharedPreferences(mContext);
		return sp.getLong(key, -1);
	}
	
	public static void setLongLocalValue(Context mContext,String key,long value){
		SharedPreferences sp =getSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static String getStringLocalValue(Context mContext, String key) {
		SharedPreferences sp =getSharedPreferences(mContext);
		return sp.getString(key, null);
	}
	
	public static void setStringLocalValue(Context mContext, String key,String value){
		SharedPreferences sp = getSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static boolean getBooleanLocalValue(Context mContext, String key) {
		SharedPreferences sp =getSharedPreferences(mContext);
		return sp.getBoolean(key, false);
	}
	
	public static void setBooleanLocalValue(Context mContext,String key, boolean value){
		SharedPreferences sp =getSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

}
