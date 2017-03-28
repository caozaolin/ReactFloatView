package com.caozaolin.mreactfloatview;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.view.GameAssistApi;

public class ZSDK {

	private static final int REQUEST_CODE = 1;
	private GameAssistApi mGameAssistApi;

	private static ZSDK instance;
	private Activity context;

	private ZSDK() {}

	public static ZSDK getInstance() {
		if (instance == null) {
			instance = new ZSDK();
		}
		return instance;
	}

	/**
	 * 初始化ZSDK类，悬浮窗权限申请也在这一步进行
	 * @param context
     */
	public void init(Activity context) {
		this.context = context;
		askforPermission();
	}

	public Activity getContext() {
		return this.context;
	}

	/**
	 * 显示悬浮窗
	 */
	public void onResume() {
		if(mGameAssistApi != null){
			mGameAssistApi.onResume();
		}
	}

	/**
	 * 隐藏悬浮窗
	 */
	public void onPause() {
		if(mGameAssistApi != null){
			mGameAssistApi.onPause();
		}
	}

	/**
	 * 处理返回Activity状态码的情况
	 * @param requestCode
	 * @param resultCode
	 * @param data
     */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (Settings.canDrawOverlays(context)) {
				// 如果已经授权
				// 以下两句代码务必调用
				if(mGameAssistApi == null){
					mGameAssistApi = new GameAssistApi(context);
				}
			}
		}
	}

	/**
	 * 权限申请
	 */
	public void askforPermission(){
		// 进行权限申请
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			// 如果是api23以上版本，则进行权限申请
			if(!Settings.canDrawOverlays(context)){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("提示");
				builder.setMessage("需要申请系统悬浮窗权限！");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
						intent.setData(Uri.parse("package:" + context.getPackageName()));
						context.startActivityForResult(intent, REQUEST_CODE);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			} else {
				Toast.makeText(context, "已经申请过权限了", Toast.LENGTH_SHORT).show();
				if(mGameAssistApi == null){
					mGameAssistApi = new GameAssistApi(context);
				}
			}

		} else {
			// 如果是api23以下版本，不需要申请权限
			Toast.makeText(context, "不需要申请权限", Toast.LENGTH_SHORT).show();
			// ZSDK.getInstance().init(context);
			if(mGameAssistApi == null){
				mGameAssistApi = new GameAssistApi(context);
			}
		}
	}


}
