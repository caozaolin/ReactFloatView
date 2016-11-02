package com.caozaolin.reactfloatview.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caozaolin.reactfloatview.interf.OnFloatWindowTouchEvent;
import com.caozaolin.reactfloatview.utils.CustomResourceMgmtUtils;
import com.caozaolin.reactfloatview.utils.SDKUtils;

/**
 * Desction:悬浮窗Big形态
 * Author:caozaolin
 * Date:16/09/29
 */
public class FloatWindowBigView extends LinearLayout implements OnClickListener {

	private static final String TAG = "FloatView";

	private Context mContext;
	private OnFloatWindowTouchEvent mOnFloatWindowTouchEvent;
	private CustomResourceMgmtUtils mCustomResourceMgmtUtils;
	private XmlResourceParser mXmlResourceParser = null;
	private AssetManager mAssetManager;
	private View rootView;
	private LinearLayout uac_float_big_ly_layout;
	private ImageView gift_iv;
	private ImageView message_iv;
	private ImageView forum_iv;
	private ImageView info_iv;
	private ImageView pay_iv;
	private RelativeLayout gift_ly;
	private RelativeLayout message_ly;
  	private RelativeLayout forum_ly;
  	private RelativeLayout info_ly;
  	private RelativeLayout pay_ly;
  	private TextView gift_tv;
  	private TextView message_tv;
  	private TextView forum_tv;
  	private TextView info_tv;
  	private TextView pay_tv;
    public int viewWidth;
    public int viewHeight;
    
	public FloatWindowBigView(Context context, OnFloatWindowTouchEvent mOnFloatWindowTouchEvent) {
		super(context);
		this.mContext = context;
		this.mOnFloatWindowTouchEvent = mOnFloatWindowTouchEvent;
		this.mCustomResourceMgmtUtils = CustomResourceMgmtUtils.get(this.mContext);
		initView();
	}

	private void initView() {
		try {
			this.mAssetManager = getResources().getAssets();
			this.mXmlResourceParser = this.mAssetManager.openXmlResourceParser("res/layout/uac_gameassist_float_big.xml");
			this.rootView = LayoutInflater.from(this.mContext).inflate(this.mXmlResourceParser, this);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (this.mXmlResourceParser != null) {
				this.mXmlResourceParser.close();
				this.mXmlResourceParser = null;
			}
		}
		
	    this.uac_float_big_ly_layout = ((LinearLayout)this.rootView.findViewWithTag("uac_float_big_ly"));
	    this.forum_ly = ((RelativeLayout)this.rootView.findViewWithTag("uac_float_big_forum_ly"));
	    this.forum_iv = ((ImageView)this.rootView.findViewWithTag("uac_float_big_forum_iv"));
	    this.forum_tv = ((TextView)this.rootView.findViewWithTag("uac_float_big_forum_tv"));
	    SDKUtils.setBackground(this.forum_iv, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_forum", false, 320));
	    this.forum_tv.setText("论坛");  // 论坛

	    this.info_ly = ((RelativeLayout)this.rootView.findViewWithTag("uac_float_big_info_ly"));
	    this.info_iv = ((ImageView)this.rootView.findViewWithTag("uac_float_big_info_iv"));
	    this.info_tv = ((TextView)this.rootView.findViewWithTag("uac_float_big_info_tv"));
	    SDKUtils.setBackground(this.info_iv, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_info", false, 320));
	    this.info_tv.setText("个人信息"); // 个人信息

	    this.message_ly = ((RelativeLayout)this.rootView.findViewWithTag("uac_float_big_message_ly"));
	    this.message_iv = ((ImageView)this.rootView.findViewWithTag("uac_float_big_message_iv"));
	    this.message_tv = ((TextView)this.rootView.findViewWithTag("uac_float_big_message_tv"));
	    SDKUtils.setBackground(this.message_iv, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_message", false, 320));
	    this.message_tv.setText("消息");  // 消息

	    this.pay_ly = ((RelativeLayout)this.rootView.findViewWithTag("uac_float_big_pay_ly"));
	    this.pay_iv = ((ImageView)this.rootView.findViewWithTag("uac_float_big_pay_iv"));
	    this.pay_tv = ((TextView)this.rootView.findViewWithTag("uac_float_big_pay_tv"));
	    SDKUtils.setBackground(this.pay_iv, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_pay", false, 320));
	    this.pay_tv.setText("充值"); // 充值

	    this.gift_ly = ((RelativeLayout)this.rootView.findViewWithTag("uac_float_big_gift_ly"));
	    this.gift_iv = ((ImageView)this.rootView.findViewWithTag("uac_float_big_gift_iv"));
	    this.gift_tv = ((TextView)this.rootView.findViewWithTag("uac_float_big_gift_tv"));
	    SDKUtils.setBackground(this.gift_iv, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_gift", false, 320));
	    this.gift_tv.setText("礼包"); // 礼包

	    this.forum_ly.setOnClickListener(this);
	    this.info_ly.setOnClickListener(this);
	    this.message_ly.setOnClickListener(this);
	    this.gift_ly.setOnClickListener(this);
	    this.pay_ly.setOnClickListener(this);
	    
	    if (!checkQiKuPay()) {
	        this.pay_ly.setVisibility(View.GONE);
	    }

	    invalidate();
	    this.rootView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED));
	    this.viewWidth = this.rootView.getMeasuredWidth();
	    this.viewHeight = this.rootView.getMeasuredHeight();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (inRangeOfView(this.rootView, event)) {
		    return false;
		}
		if (event.getAction() == 1) {
		    this.mOnFloatWindowTouchEvent.onTouchEventBig(-1.0F, -1.0F);
		}
		return true;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public void onClick(View v) {
		String tag = String.valueOf(v.getTag());
		if (TextUtils.equals(tag, "uac_float_big_message_ly")) {
			Toast.makeText(this.mContext, "进入信息页面", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.equals(tag, "uac_float_big_forum_ly")){
			Toast.makeText(this.mContext, "进入论坛页面", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.equals(tag, "uac_float_big_info_ly")){
			Toast.makeText(this.mContext, "进入个人信息页面", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.equals(tag, "uac_float_big_gift_ly")) {
			Toast.makeText(this.mContext, "进入礼包页面", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.equals(tag, "uac_float_big_pay_ly")) {
			Toast.makeText(this.mContext, "进入支付页面", Toast.LENGTH_SHORT).show();
		}
		this.mOnFloatWindowTouchEvent.onTouchEventBig(-1.0F, -1.0F);
	}
	
	public boolean checkQiKuPay() {
		boolean isSupportQikuPay = false;
		try {
			PackageInfo pInfo = this.mContext.getPackageManager().getPackageInfo("com.android.qikupay.apk", 0);
			if ((pInfo != null) && (pInfo.versionCode > 1000))
				isSupportQikuPay = true;
		} catch (Throwable t) {
			isSupportQikuPay = false;
			t.printStackTrace();
		}
		return isSupportQikuPay;
	}
	
	private boolean inRangeOfView(View view, MotionEvent ev) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		return (ev.getX() >= x) && (ev.getX() <= x + view.getWidth()) && (ev.getY() >= y) && (ev.getY() <= y + view.getHeight());
	}

	public void setOnTouchEventBig(OnFloatWindowTouchEvent mOnFloatWindowTouchEvent) {
		this.mOnFloatWindowTouchEvent = mOnFloatWindowTouchEvent;
	}

	public void changeBg(boolean isLeft) {
	    LayoutParams lp = (LayoutParams)this.uac_float_big_ly_layout.getLayoutParams();
	    if (isLeft) {
	       lp.setMargins(20, 0, 0, 0);
	       this.uac_float_big_ly_layout.setLayoutParams(lp);
	       this.uac_float_big_ly_layout.invalidate();
	       SDKUtils.setBackground(this.rootView, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_left_bg", true, 320));
	    } else {
	       lp.setMargins(30, 0, 2, 0);
	       this.uac_float_big_ly_layout.setLayoutParams(lp);
	       this.uac_float_big_ly_layout.invalidate();
	       SDKUtils.setBackground(this.rootView, this.mCustomResourceMgmtUtils.getDrawable("uac_gameassist_right_bg", true, 320));
	    }
		
	}
}
