package com.caozaolin.reactfloatview;

import android.os.Bundle;

import com.caozaolin.reactfloatview.view.GameAssistApi;

public class MainActivity extends ReactFloatViewActivity {

    private GameAssistApi mGameAssistApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
