package com.caozaolin.reactfloatview.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class GameAssistApi {

    public final String TAG = "GameAssistApi";
    private Handler handler;
    private GameAssistWindowManager mGameAssistWindowManager;
    private Activity mActivity;
    private boolean isShowAssist = true;
    // public static boolean isAllow = false; // 标志位，是否允许创建悬浮窗
    public GameAssistApi(Activity mActivity)
    {
        this.mActivity = mActivity;
        this.handler = new Handler(Looper.getMainLooper());
        // askforPermission();
        // Log.i(TAG, "________________在构造函数中1_________________ : isAllow : " + isAllow);
        if ((this.isShowAssist) && (this.mGameAssistWindowManager == null)) {
            // Log.i(TAG, "________________在构造函数中2_________________ : isAllow : " + isAllow);
            this.mGameAssistWindowManager = new GameAssistWindowManager(mActivity);
            this.mGameAssistWindowManager.createSmallWindow();
        }
    }

    public void askforPermission(){
        // 进行权限申请
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 如果是api23以上版本，则进行权限申请
            if(!Settings.canDrawOverlays(this.mActivity)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
                builder.setTitle("提示");
                builder.setMessage("需要申请系统悬浮窗权限！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        mActivity.startActivity(intent);
                        dialog.dismiss();
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
                Toast.makeText(this.mActivity, "已经申请过权限了", Toast.LENGTH_SHORT).show();
            }

        } else {
            // 如果是api23以下版本，不需要申请权限
            Toast.makeText(this.mActivity, "不需要申请权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void onPause()
    {

        if (this.mGameAssistWindowManager != null) {

            this.mGameAssistWindowManager.removeBigWindow();
            this.mGameAssistWindowManager.removeSmallWindow();

            this.mGameAssistWindowManager.removeBrandWindow();
            this.mGameAssistWindowManager = null;
        }
        System.gc();
    }

    public void onResume()
    {
        if (this.isShowAssist) {
            if (this.mGameAssistWindowManager == null) {
                this.mGameAssistWindowManager = new GameAssistWindowManager(this.mActivity);
            }
            if (!this.mGameAssistWindowManager.isWindowShowing()){
                this.mGameAssistWindowManager.createSmallWindow();
            }
        }
    }
}
