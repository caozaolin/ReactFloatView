package com.caozaolin.mreactfloatview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.bean.GameAssistContant;
import com.caozaolin.mreactfloatview.interf.OnFloatWindowTouchEvent;
import com.caozaolin.mreactfloatview.utils.CustomResourceMgmtUtils;
import com.caozaolin.mreactfloatview.utils.PreferenceUtils;
import com.caozaolin.mreactfloatview.utils.SDKUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Desction:悬浮窗Brand形态
 * Author:caozaolin
 * Date:16/09/29
 */
public class FloatWindowBrandView extends LinearLayout {
	
	private static final String TAG = "FloatView";
	
	private static final int TOUCH_INTERVAL_TIME = 3000;
	private static final int TOUCH_INTERVAL_MAX_VALUE = 2;
	private static final int TOUCH_INTERVAL_FIRST_VALUE = 1;
	private static final int TOUCH_INTERVAL_INT_VALUE = 0;
	  
	private Activity mActivity;
	private CustomResourceMgmtUtils mCustomResourceMgmtUtils;
	private OnFloatWindowTouchEvent mOnFloatWindowTouchEvent;
	private WindowManager windowManager;
	private WindowManager.LayoutParams mParams;
	private AssetManager mAssetManager;
	private XmlResourceParser mXmlResourceParser = null;
	private View rootview;
	private ImageView uacBrandImg;
	private View uacBrandBgView;
	private int statusBarHeight;
	private int mBarHeight;
	public int viewWidth;
	public int viewHeight;
	private int touchCount = 0;
	private float xInView;
	private float yInView;
	private float xDownInScreen;
	private float yDownInScreen;
	private float xInScreen;
	private float yInScreen;
	private Runnable intervalRunnable;
	
	public FloatWindowBrandView(Activity activity, OnFloatWindowTouchEvent onFloatWindowTouchEvent) {
		super(activity);
		this.mActivity = activity;
		this.mCustomResourceMgmtUtils = CustomResourceMgmtUtils.get(this.mActivity);
		this.mOnFloatWindowTouchEvent = onFloatWindowTouchEvent;
		init();
		
	}

	private void init() {
		try {
			windowManager = (WindowManager) mActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
			this.mAssetManager = this.mActivity.getResources().getAssets();
			this.mXmlResourceParser = this.mAssetManager.openXmlResourceParser("res/layout/uac_gameassist_float_brand.xml");
			rootview = LayoutInflater.from(this.mActivity).inflate(this.mXmlResourceParser, this);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		  if (this.mXmlResourceParser != null) {
		      this.mXmlResourceParser.close();
		      this.mXmlResourceParser = null;
		  }
		}
		int flags = this.mActivity.getWindow().getAttributes().flags;
		if ((flags & 0x400) == 1024)
			this.mBarHeight = 0;
		else {
			this.mBarHeight = getStatusBarHeight();
		}
		initView();
	}

	private void initView() {
		this.uacBrandBgView = this.rootview.findViewWithTag("uac_gameassist_small_float_brand_bg");
		this.uacBrandImg = ((ImageView) this.rootview.findViewWithTag("uac_gameassist_small_float_brand_img"));
		int xSmallInScreen = PreferenceUtils.getInstance(this.mActivity).getInt("xInScreen", GameAssistContant.screenWidth / 2);

		if (xSmallInScreen < GameAssistContant.screenWidth / 2) {
			SDKUtils.setBackground(this.uacBrandImg, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_brand_left", false, 320));
		} else {
			SDKUtils.setBackground(this.uacBrandImg, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_brand_right", false, 320));
		}	
		refeshBrandFloat();
	}

	private void refeshBrandFloat() {
		invalidate();
		this.rootview.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED));
		this.viewWidth = this.rootview.getMeasuredWidth();
		this.viewHeight = this.rootview.getMeasuredHeight();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float mTempX = event.getRawX();
		float mTempY = event.getRawY() - this.mBarHeight;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.xInView = event.getX();
			this.yInView = event.getY();
			this.xDownInScreen = mTempX;
			this.yDownInScreen = mTempY;
			this.xInScreen = mTempX;
			this.yInScreen = mTempY;
			break;
		case MotionEvent.ACTION_MOVE:
			this.xInScreen = mTempX;
			this.yInScreen = mTempY;
			
			break;
		case MotionEvent.ACTION_UP:
			if ((Math.abs(this.xDownInScreen - this.xInScreen) > ViewConfiguration.get(this.mActivity).getScaledTouchSlop())
					|| (Math.abs(this.yDownInScreen - this.yInScreen) > ViewConfiguration.get(this.mActivity).getScaledTouchSlop()))
				break;

			dealTab();
		}

		return true;
	}
	
	public void setParams(WindowManager.LayoutParams params) {
	    this.mParams = params;
	}
	
	// 获取状态栏高度
	private int getStatusBarHeight() {
		if (this.statusBarHeight == 0) {
			try {
				Class c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = ((Integer) field.get(o)).intValue();
				this.statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.statusBarHeight;
	}
	
	private void dealTab() {
		this.touchCount += 1;
		if (this.touchCount == 2) {
			this.mOnFloatWindowTouchEvent.onTouchEventBrand(-1.0F, -1.0F);
			if (this.intervalRunnable != null) {
				removeCallbacks(this.intervalRunnable);
			}
			this.touchCount = 0;
		} else if (this.touchCount == 1) {
			Toast.makeText(this.mActivity, "请再次点击,打开游戏助手", Toast.LENGTH_SHORT).show();

			this.intervalRunnable = new Runnable() {
				public void run() {
					touchCount = 0;
				}
			};
			postDelayed(this.intervalRunnable, 3000L);
		}
		
	}

}
