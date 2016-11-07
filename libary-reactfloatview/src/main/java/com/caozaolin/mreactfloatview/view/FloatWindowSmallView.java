package com.caozaolin.mreactfloatview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.caozaolin.mreactfloatview.bean.GameAssistContant;
import com.caozaolin.mreactfloatview.interf.OnFloatWindowTouchEvent;
import com.caozaolin.mreactfloatview.utils.CustomResourceMgmtUtils;
import com.caozaolin.mreactfloatview.utils.PreferenceUtils;
import com.caozaolin.mreactfloatview.utils.SDKUtils;

import java.lang.reflect.Field;

/**
 * Desction:悬浮窗small形态
 * Author:caozaolin
 * Date:16/09/29
 */
public class FloatWindowSmallView extends LinearLayout{
	private static final String TAG = "FloatView";
	private Activity context;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams params;
	private AssetManager mAssetManager;
	private XmlResourceParser mXmlResourceParser = null;
	private CustomResourceMgmtUtils mCustomResourceMgmtUtils;
	private OnFloatWindowTouchEvent mOnFloatWindowTouchEvent;
	private View uac_gameassist_small_photo_ly;
	private ImageView uac_gameassist_small_photo;
	public int smallWidth = 0;
	private float xInScreen;
	private float yInScreen;
	private float xDownInScreen;
	private float yDownInScreen;
	private float xInView;
	private float yInView;
	private View rootview;
	private int mBarHeight;
	public int viewWidth;
	public int viewHeight;
	private int statusBarHeight;
	public long lastClickTime;
	
	
	public FloatWindowSmallView(Activity context, OnFloatWindowTouchEvent onFloatWindowTouchEvent){
		super(context);
		try{
			this.context = context;
			this.mCustomResourceMgmtUtils = CustomResourceMgmtUtils.get(this.context);
			this.mOnFloatWindowTouchEvent = onFloatWindowTouchEvent;
			init();
		} catch(Exception e){
			e.printStackTrace();
		}

	}
	

	private void init() {
		mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		try {
		    this.mAssetManager = this.context.getResources().getAssets();
		    this.mXmlResourceParser = this.mAssetManager.openXmlResourceParser("res/layout/uac_gameassist_float_small.xml");
			Log.i(TAG, "");
			this.rootview = LayoutInflater.from(this.context).inflate(this.mXmlResourceParser, this);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		  if (this.mXmlResourceParser != null) {
		      this.mXmlResourceParser.close();
		      this.mXmlResourceParser = null;
		  }
		}
		
	    int flags = context.getWindow().getAttributes().flags;
	    if ((flags & 0x400) == 1024)
	      this.mBarHeight = 0;
	    else {
	      this.mBarHeight = getStatusBarHeight();
	    }
	    this.xInScreen = PreferenceUtils.getInstance(this.context).getInt("xInScreen", GameAssistContant.screenWidth / 2);
	    this.yInScreen = PreferenceUtils.getInstance(this.context).getInt("yInScreen", GameAssistContant.screenHeight / 2);
	    initView();
	}

	// 设置view的资源
	private void initView() {
	    this.uac_gameassist_small_photo_ly = this.rootview.findViewWithTag("uac_gameassist_small_float_center_ly");
	    this.uac_gameassist_small_photo = ((ImageView)this.rootview.findViewWithTag("uac_gameassist_small_float_center_iv"));

	    SDKUtils.setBackground(this.uac_gameassist_small_photo, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_default", false, 320));
	    // uac_gameassist_small_photo.setImageDrawable(getResources().getDrawable(R.drawable.uac_gameassist_photo_default));
	    
	    SDKUtils.setBackground(this.uac_gameassist_small_photo_ly, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_bg", false, 320));
	    
	    refeshSmallFloat();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.lastClickTime = System.currentTimeMillis();
		
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
				updateViewPosition();
			break;
			
			case MotionEvent.ACTION_UP:
				if ((Math.abs(this.xDownInScreen - this.xInScreen) <= 
					ViewConfiguration.get(this.context).getScaledTouchSlop()) && (Math.abs(this.yDownInScreen - this.yInScreen) <= ViewConfiguration.get(this.context).getScaledTouchSlop())){
				    this.mOnFloatWindowTouchEvent.onTouchEventSmall(mTempX - this.xInView, mTempY - this.yInView);
				} else {
					hideHalf();
				}
			break;
		}
		return true;
	}
	
	private void updateViewPosition() {
	    this.params.x = (int)(this.xInScreen - this.xInView);
	    this.params.y = (int)(this.yInScreen - this.yInView);
	    refeshSmallFloat();  // 这句代码有其他作用
	    this.mWindowManager.updateViewLayout(this, this.params);
	}
	
	public void setOnTouchEventSmall(OnFloatWindowTouchEvent mOnFloatWindowTouchEvent) {
	    this.mOnFloatWindowTouchEvent = mOnFloatWindowTouchEvent;
	}
	
	public void setParams(WindowManager.LayoutParams smallWindowParams) {
		this.params = smallWindowParams;
	}
	
	public void refeshSmallFloat() {
	    invalidate();
	    this.rootview.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED));
	    this.viewWidth = this.rootview.getMeasuredWidth();
	    this.viewHeight = this.rootview.getMeasuredHeight();
	}
	
    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
	public void show(){
		if(getVisibility() != View.VISIBLE){
			this.setVisibility(View.VISIBLE);
		}
	}
	
	public void hide(){
		if(getVisibility() == View.VISIBLE){
			this.setVisibility(View.GONE);
		}
	}
	
	public void destroy() {
		try {
			hide();
			removeFloatView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showFloat() {
	    if (this.params.x > GameAssistContant.screenWidth / 2)
	        SDKUtils.setBackground(this.uac_gameassist_small_photo_ly, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_left_bg", false, 320));
	      else {
	        SDKUtils.setBackground(this.uac_gameassist_small_photo_ly, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_right_bg", false, 320));
	      }
	    refeshSmallFloat();
	}
	
	// 抬起手的一瞬间，记录悬浮窗在屏幕中的位置
	public void hideHalf() {
	    SDKUtils.setBackground(this.uac_gameassist_small_photo_ly, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_photo_bg", false, 320));
	    refeshSmallFloat();
	    this.params.width = this.viewWidth;
	    this.params.height = this.viewHeight;
	    this.params.x = (this.xInScreen > GameAssistContant.screenWidth / 2 ? GameAssistContant.screenWidth - this.viewWidth / 2 : 0);
	    this.mWindowManager.updateViewLayout(this, this.params);
		PreferenceUtils.getInstance(context).putInt("xInScreen", this.params.x);
		PreferenceUtils.getInstance(context).putInt("yInScreen", this.params.y);
		
	}
	
	// 获取状态栏高度
	private int getStatusBarHeight() {
	    if (this.statusBarHeight == 0) {
	        try {
	          Class c = Class.forName("com.android.internal.R$dimen");
	          Object o = c.newInstance();
	          Field field = c.getField("status_bar_height");
	          int x = ((Integer)field.get(o)).intValue();
	          this.statusBarHeight = getResources().getDimensionPixelSize(x);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	      return this.statusBarHeight;
	}
	
}
