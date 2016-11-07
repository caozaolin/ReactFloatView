package com.caozaolin.mreactfloatview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Desction:该类封装了读取SharedPreferences数据的方法
 * Author:caozaolin
 * Date:16/09/29
 */
public class PreferenceUtils {
	
	private static PreferenceUtils instance;
	private final static String TAG = "XYInScreen";
	private static SharedPreferences sp;
	
	public PreferenceUtils(){
		
	}
	public static PreferenceUtils getInstance(Context context) {
		if(instance == null){
			instance = new PreferenceUtils();
			sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
			
		}
		return instance;
	}
	
	
	public void putInt(String key, int value){
		if(instance != null){
			Editor editor = sp.edit();
			editor.putInt(key, value);
			editor.commit();
		}
	}
	
	public int getInt(String key, int deaful){
		if(instance != null){
			int num = sp.getInt(key, deaful);
			return num;
		}
		return deaful;
	}
	
	public void putFloat(String key, float value){
		if(instance != null){
			Editor editor = sp.edit();
			editor.putFloat(key, value);
			editor.commit();
		}
	}
	
	public float getFloat(String key, float deaful){
		if(instance != null){
			float num = sp.getFloat(key, deaful);
			return num;
		}
		return deaful;
	}
}
