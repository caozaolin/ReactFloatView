package com.caozaolin.reactfloatview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.caozaolin.mreactfloatview.ZSDK;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 实现相关初始化
        ZSDK.getInstance().init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZSDK.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ZSDK.getInstance().onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
