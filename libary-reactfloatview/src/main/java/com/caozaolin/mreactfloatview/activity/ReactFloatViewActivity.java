package com.caozaolin.mreactfloatview.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.ZSDK;
import com.caozaolin.mreactfloatview.view.GameAssistApi;


/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class ReactFloatViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private GameAssistApi mGameAssistApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZSDK.getInstance().init(this);
        askforPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // 如果已经授权
                // 以下两句代码务必调用
                Log.i("ReactFloatViewActivity", "________________BasicActivity_________________ ");
                if(mGameAssistApi == null){
                    mGameAssistApi = new GameAssistApi(this);
                }
            }
        }
    }


    public void askforPermission(){
        // 进行权限申请
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 如果是api23以上版本，则进行权限申请
            if(!Settings.canDrawOverlays(this)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("需要申请系统悬浮窗权限！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_CODE);
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
                Toast.makeText(this, "已经申请过权限了", Toast.LENGTH_SHORT).show();
                if(mGameAssistApi == null){
                    mGameAssistApi = new GameAssistApi(this);
                }
            }

        } else {
            // 如果是api23以下版本，不需要申请权限
            Toast.makeText(this, "不需要申请权限", Toast.LENGTH_SHORT).show();
            ZSDK.getInstance().init(this);
            if(mGameAssistApi == null){
                mGameAssistApi = new GameAssistApi(this);
            }
        }
    }

    @Override
    protected void onResume() {
        if(mGameAssistApi != null){
            mGameAssistApi.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mGameAssistApi != null){
            mGameAssistApi.onPause();
        }
        super.onPause();
    }

}
