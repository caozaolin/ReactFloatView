package com.caozaolin.reactfloatview.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.caozaolin.reactfloatview.ZSDK;
import com.caozaolin.reactfloatview.bean.GameAssistContant;
import com.caozaolin.reactfloatview.interf.OnFloatWindowTouchEvent;
import com.caozaolin.reactfloatview.services.GamePlugIdleTimeService;
import com.caozaolin.reactfloatview.utils.PreferenceUtils;
import com.caozaolin.reactfloatview.utils.SDKUtils;


public class GameAssistWindowManager implements OnFloatWindowTouchEvent {
	
	private static final String TAG = "FloatView";
	
	private FloatWindowSmallView mFloatWindowSmallView;
	private WindowManager.LayoutParams smallWindowParams;
	private WindowManager.LayoutParams mBrandWindowParams;
	private WindowManager.LayoutParams bigWindowParams;
	private WindowManager mWindowManager;
	private GamePlugIdleTimeService gamePlugIdleTimeService;
	private Activity context;
	private volatile Handler uiHanlder;
	private FloatWindowBigView mFloatWindowBigView;
	private FloatWindowBrandView mFloatWindowBrandView;
	
	public GameAssistWindowManager(Activity mActivity){
		this.context = mActivity;
		this.mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	    Point mPoint = SDKUtils.getScreenHeightWidth(this.mWindowManager);
	    GameAssistContant.screenWidth = mPoint.x;
	    GameAssistContant.screenHeight = mPoint.y;
	    
		this.uiHanlder = new Handler(Looper.getMainLooper());

	}
	
	public void createSmallWindow() {
		if (this.mFloatWindowSmallView == null) {
			this.mFloatWindowSmallView = new FloatWindowSmallView(context, this);
			setSmallWindowParams();
			this.mFloatWindowSmallView.setParams(this.smallWindowParams);
			this.mWindowManager.addView(this.mFloatWindowSmallView, this.smallWindowParams);
			this.mFloatWindowSmallView.setOnTouchEventSmall(this);
			this.mFloatWindowSmallView.refeshSmallFloat();
			
		}

		cancelIdleTimeService();
		// 这句代码删除了smallview，展示brandview
		startIdleTimeService();
	}

	public void updateSmallWindow() {
		long now = System.currentTimeMillis();
		if ((this.mFloatWindowSmallView != null) && (now - this.mFloatWindowSmallView.lastClickTime >= 2000L) && (!isBigWindowShowing())){
			this.mFloatWindowSmallView.showFloat();
		}
	}
	  
	public void removeSmallWindow() {
		if (this.mFloatWindowSmallView != null) {
			this.mWindowManager.removeView(this.mFloatWindowSmallView);
			this.mFloatWindowSmallView = null;
			this.smallWindowParams = null;
		}
	}
	  
	public void cancelIdleTimeService() {
	    if (this.gamePlugIdleTimeService != null) {
	        this.gamePlugIdleTimeService.cancelIdleTimeService();
	        this.gamePlugIdleTimeService = null;
	    }
	}
	
	public void startIdleTimeService() {
	    if (this.gamePlugIdleTimeService == null) {
	        this.gamePlugIdleTimeService = new GamePlugIdleTimeService(this);
	    }
	    this.gamePlugIdleTimeService.executeIdleTimeService(this.uiHanlder);
	}

	private void setSmallWindowParams() {
	    if (this.smallWindowParams == null) {
	    	this.smallWindowParams = new WindowManager.LayoutParams();
	        
	    	this.smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
	    	// this.smallWindowParams.flags = 40;
	    	this.smallWindowParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	    	this.smallWindowParams.format = PixelFormat.RGBA_8888;
	    	this.smallWindowParams.width = this.mFloatWindowSmallView.viewWidth;
	    	this.smallWindowParams.height = this.mFloatWindowSmallView.viewHeight;
	    	this.smallWindowParams.gravity = Gravity.LEFT | Gravity. TOP;
	    	this.smallWindowParams.x = PreferenceUtils.getInstance(this.context).getInt("xInScreen", GameAssistContant.screenWidth);
	    	this.smallWindowParams.y = PreferenceUtils.getInstance(this.context).getInt("yInScreen", GameAssistContant.screenHeight / 2);
	    }
	}

	public void createBrandWindow() {
		removeSmallWindow();
		if (this.mFloatWindowBrandView == null) {
			this.mFloatWindowBrandView = new FloatWindowBrandView(this.context, this);
			setBrandWindowParams();
			this.mFloatWindowBrandView.setParams(this.mBrandWindowParams);
			this.mWindowManager.addView(this.mFloatWindowBrandView, this.mBrandWindowParams);
			this.mFloatWindowBrandView.invalidate();
		}
	}
	
	public void removeBrandWindow() {
		if (this.mFloatWindowBrandView != null) {
			this.mWindowManager.removeView(this.mFloatWindowBrandView);
			this.mFloatWindowBrandView = null;
			this.mBrandWindowParams = null;
		}

		if (this.gamePlugIdleTimeService != null)
			this.gamePlugIdleTimeService.cancelIdleTimeService();
	}
	
	private void setBrandWindowParams() {
		if (this.mBrandWindowParams == null) {
			this.mBrandWindowParams = new WindowManager.LayoutParams();
		    this.mBrandWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		    this.mBrandWindowParams.format = PixelFormat.RGBA_8888;
		    // this.mBrandWindowParams.flags = 40;
		    this.mBrandWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		    this.mBrandWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
		    
			this.mBrandWindowParams.width = this.mFloatWindowBrandView.viewWidth;
			this.mBrandWindowParams.height = this.mFloatWindowBrandView.viewHeight;
			this.mBrandWindowParams.x = PreferenceUtils.getInstance(this.context).getInt("xInScreen", GameAssistContant.screenWidth);
			this.mBrandWindowParams.y = PreferenceUtils.getInstance(this.context).getInt("yInScreen", GameAssistContant.screenHeight / 2);
		}
	}
	
	private void createBigWindow(float x, float y) {
	    if (this.mFloatWindowBigView == null) {
	        this.mFloatWindowBigView = new FloatWindowBigView(this.context, this);
	        setBigWindowParams(x, y);
	        this.mWindowManager.addView(this.mFloatWindowBigView, this.bigWindowParams);
	        this.mFloatWindowBigView.invalidate();
	        this.mFloatWindowBigView.setOnTouchEventBig(this);
	    }
	}
	
	public void removeBigWindow() {
	    if (this.mFloatWindowBigView != null) {
	        this.mWindowManager.removeView(this.mFloatWindowBigView);
	        this.mFloatWindowBigView = null;
	        this.bigWindowParams = null;
	    }
	    if (this.mFloatWindowSmallView != null){
	    	this.mFloatWindowSmallView.hideHalf();
	    }
	}
	
	private void setBigWindowParams(float smallX, float smallY) {
		if (this.bigWindowParams == null) {
			this.bigWindowParams = new WindowManager.LayoutParams();
			this.bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
			this.bigWindowParams.format = PixelFormat.RGBA_8888;
			// this.smallWindowParams.flags = 40;
			this.smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			this.bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
			this.bigWindowParams.width = this.mFloatWindowBigView.viewWidth;
			this.bigWindowParams.height = this.mFloatWindowBigView.viewHeight;
		}

		if ((GameAssistContant.screenWidth - smallX - this.mFloatWindowSmallView.viewWidth > this.bigWindowParams.width) || (smallX > this.bigWindowParams.width)) {
			if (GameAssistContant.screenWidth - smallX > this.bigWindowParams.width) {
				this.bigWindowParams.x = (int) (smallX + this.mFloatWindowSmallView.viewWidth - this.mFloatWindowSmallView.smallWidth);
				if (this.bigWindowParams.x < this.mFloatWindowSmallView.viewWidth) {
					this.bigWindowParams.x = (this.mFloatWindowSmallView.viewWidth - this.mFloatWindowSmallView.smallWidth);
				}
				this.mFloatWindowBigView.changeBg(true);
			} else {
				if (smallX > GameAssistContant.screenWidth - this.mFloatWindowSmallView.viewWidth) {
					smallX = GameAssistContant.screenWidth - this.mFloatWindowSmallView.viewWidth + this.mFloatWindowSmallView.smallWidth;
				}
				this.mFloatWindowBigView.changeBg(false);
				this.bigWindowParams.x = (int) (smallX - this.mFloatWindowBigView.viewWidth + this.mFloatWindowSmallView.smallWidth);
			}
			this.bigWindowParams.y = (int) (smallY + this.smallWindowParams.height / 2 - this.bigWindowParams.height / 2);
		} else if (smallY + this.mFloatWindowSmallView.viewHeight / 2 < GameAssistContant.screenHeight / 2) {
			if (GameAssistContant.screenWidth - smallX - this.mFloatWindowSmallView.viewWidth / 2 > GameAssistContant.screenWidth / 2)
				this.bigWindowParams.x = (int) smallX;
			else {
				this.bigWindowParams.x = (int) (smallX - this.mFloatWindowBigView.viewWidth + this.mFloatWindowSmallView.viewWidth);
			}
			this.bigWindowParams.y = (int) (smallY + this.mFloatWindowSmallView.viewHeight);
		} else {
			if (GameAssistContant.screenWidth - smallX - this.mFloatWindowSmallView.viewWidth / 2 > GameAssistContant.screenWidth / 2)
				this.bigWindowParams.x = (int) smallX;
			else {
				this.bigWindowParams.x = (int) (smallX - this.mFloatWindowBigView.viewWidth + this.mFloatWindowSmallView.viewWidth);
			}
			this.bigWindowParams.y = (int) (smallY - this.mFloatWindowBigView.viewHeight);
		}
	}

	@Override
	public void onTouchEventBig(float paramFloat1, float paramFloat2) {
	    removeBigWindow();

	    cancelIdleTimeService();
	    if (this.mFloatWindowBigView == null){
	    	startIdleTimeService();
	    }
	}

	@Override
	public void onTouchEventSmall(float x, float y) {
	    createBigWindow(x, y);
	    if (this.mFloatWindowSmallView != null) {
	      this.mFloatWindowSmallView.showFloat();
	    }

	    cancelIdleTimeService();
	    if (this.mFloatWindowBigView == null){
	    	startIdleTimeService();
	    }
	}

	@Override
	public void onTouchEventBrand(float paramFloat1, float paramFloat2) {
	    removeBrandWindow();
	    if (this.mFloatWindowSmallView == null){
	    	createSmallWindow();
	    }
	}
	
	@Override
	public void changeuserListen() {
		if (this.mFloatWindowSmallView != null);
	}

	public boolean isBigWindowShowing() {
		return this.mFloatWindowBigView != null;
	}

	public boolean isWindowShowing() {
		return (this.mFloatWindowSmallView != null) || (this.mFloatWindowBigView != null) || (this.mFloatWindowBrandView != null);
	}
}
