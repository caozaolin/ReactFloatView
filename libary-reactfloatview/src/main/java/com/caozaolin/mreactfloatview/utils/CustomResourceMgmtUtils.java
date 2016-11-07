package com.caozaolin.mreactfloatview.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Desction:该工具类提供了从assert文件夹里面读取悬浮窗所需的资源文件
 * Author:caozaolin
 * Date:16/09/29
 */
public class CustomResourceMgmtUtils {
	 public static boolean ISENGLISH = false;
	  protected static volatile CustomResourceMgmtUtils myResouceMgmt;
	  private Context mContext;
	  private AssetsUtil mAssetsUtil;
	  private Resources mResources;
	  private String packageName;
	  private LayoutInflater mLayoutInflater;

	  private CustomResourceMgmtUtils(Context context)
	  {
	    this.mContext = context.getApplicationContext();
	    this.mAssetsUtil = AssetsUtil.getInstance(this.mContext);
	    this.mResources = this.mContext.getResources();
	    this.packageName = this.mContext.getApplicationInfo().packageName;
	    this.mLayoutInflater = LayoutInflater.from(this.mContext);
	  }

	  public static CustomResourceMgmtUtils get(Context context) {
	    if (null == myResouceMgmt) {
	      synchronized (CustomResourceMgmtUtils.class) {
	        if (null == myResouceMgmt) {
	          myResouceMgmt = new CustomResourceMgmtUtils(context);
	        }
	      }
	    }
	    return myResouceMgmt;
	  }

	  public String getString(String resouceKey) {
	    return this.mAssetsUtil.getString(resouceKey);
	  }

	  public View getLayout(String resouceKey, boolean island)
	  {
	    return (island) && (havelandlayout(resouceKey)) ? getLayoutLand(resouceKey) : getLayoutPort(resouceKey);
	  }

	  private boolean havelandlayout(String resouceKey)
	  {
	    try
	    {
	      this.mContext.getAssets().open("layout-land/" + resouceKey + ".xml");
	    } catch (IOException e) {
	      e.printStackTrace();
	      return false;
	    }
	    return true;
	  }

	  private View getLayoutPort(String resouceKey)
	  {
	    int resouceId = this.mResources.getIdentifier(resouceKey, "layout", this.packageName);
	    if (resouceId == 0) {
	      return this.mAssetsUtil.getLayout("layout/" + resouceKey + ".xml", null);
	    }
	    return this.mLayoutInflater.inflate(resouceId, null);
	  }

	  private View getLayoutLand(String resouceKey)
	  {
	    int resouceId = this.mResources.getIdentifier(resouceKey, "layout", this.packageName);
	    if (resouceId == 0) {
	      return this.mAssetsUtil.getLayout("layout-land/" + resouceKey + ".xml", null);
	    }
	    return this.mLayoutInflater.inflate(resouceId, null);
	  }

	  public Drawable getDrawable(String resouceKey) {
	    int resouceId = this.mResources.getIdentifier(resouceKey, "drawable", this.packageName);
	    if (resouceId == 0) {
	      return this.mAssetsUtil.getDrawableFromXml("assets/drawable/" + resouceKey + ".xml");
	    }
	    return this.mResources.getDrawable(resouceId);
	  }

	  public Drawable getDrawable(String resouceKey, boolean is9Png) {
	    return getDrawable(resouceKey, is9Png, 480);
	  }

	  public Drawable getDrawable(String resouceKey, boolean is9Png, int density) {
	    int resouceId = this.mResources.getIdentifier(resouceKey, "drawable", this.packageName);
	    if (resouceId == 0) {
	      String suffix = (is9Png ? ".9" : "") + ".png";
	      return this.mAssetsUtil.getDrawable("drawable/" + resouceKey + suffix, density);
	    }
	    return this.mResources.getDrawable(resouceId);
	  }

	  public Drawable getStatusDrawable(String origin, String press) {
	    String prefix = "assets/drawable/";
	    String suffix = ".xml";
	    int statePressed = 16842919;
	    int stateSelected = 16842910;
	    int originId = this.mResources.getIdentifier(origin, "drawable", this.packageName);
	    int pressId = this.mResources.getIdentifier(press, "drawable", this.packageName);
	    if ((originId == 0) && (pressId == 0)) {
	      return new AssetsUtil.StateListDrawableBuilder(this.mContext, prefix + origin + suffix, prefix + press + suffix).create();
	    }

	    StateListDrawable mStateListDrawable = new StateListDrawable();
	    mStateListDrawable.addState(new int[] { statePressed, stateSelected }, this.mResources.getDrawable(pressId));

	    mStateListDrawable.addState(new int[0], this.mResources.getDrawable(originId));
	    return mStateListDrawable;
	  }

	  public Drawable getCheckStatusDrawable(String origin, String press, String checked, String checkpressed) {
	    String prefix = "assets/drawable/";
	    String suffix = ".xml";
	    int statePressed = 16842919;
	    int stateChecked = 16842912;
	    int stateEnabled = 16842910;
	    int originId = this.mResources.getIdentifier(origin, "drawable", this.packageName);
	    int pressId = this.mResources.getIdentifier(press, "drawable", this.packageName);
	    int checkedId = this.mResources.getIdentifier(checked, "drawable", this.packageName);
	    int checkPressedId = this.mResources.getIdentifier(checkpressed, "drawable", this.packageName);
	    if ((originId == 0) && (pressId == 0) && (checkedId == 0) && (checkPressedId == 0)) {
	      return new AssetsUtil.StateListDrawableBuilder(this.mContext, prefix + origin + suffix, prefix + press + suffix, prefix + checked + suffix, prefix + checkpressed + suffix).create();
	    }

	    StateListDrawable mStateListDrawable = new StateListDrawable();
	    mStateListDrawable.addState(new int[0], this.mResources.getDrawable(originId));
	    mStateListDrawable.addState(new int[] { stateChecked, statePressed }, this.mResources.getDrawable(checkPressedId));
	    mStateListDrawable.addState(new int[] { stateChecked, stateEnabled }, this.mResources.getDrawable(checkedId));
	    mStateListDrawable.addState(new int[] { statePressed, stateEnabled }, this.mResources.getDrawable(pressId));
	    return mStateListDrawable;
	  }

	  public Drawable getStatusDrawable(String origin, String press, boolean is9Png) {
	    int statePressed = 16842919;
	    int stateSelected = 16842910;
	    int originId = this.mResources.getIdentifier(origin, "drawable", this.packageName);
	    int pressId = this.mResources.getIdentifier(press, "drawable", this.packageName);
	    if ((originId == 0) && (pressId == 0)) {
	      String suffix = (is9Png ? ".9" : "") + ".png";
	      return new AssetsUtil.StateListDrawableBuilder(this.mContext, "drawable/" + origin + suffix).setPressDrawable("drawable/" + press + suffix).create();
	    }
	    StateListDrawable mStateListDrawable = new StateListDrawable();
	    mStateListDrawable.addState(new int[] { statePressed, stateSelected }, this.mResources.getDrawable(pressId));

	    mStateListDrawable.addState(new int[0], this.mResources.getDrawable(originId));
	    return mStateListDrawable;
	  }

	  public Drawable getCheckStatusDrawable(String origin, String checked, String pressed, String checkpressed, boolean is9Png)
	  {
	    int stateChecked = 16842912;
	    int statePressed = 16842919;

	    int originId = this.mResources.getIdentifier(origin, "drawable", this.packageName);
	    int selectId = this.mResources.getIdentifier(checked, "drawable", this.packageName);
	    int pressedId = this.mResources.getIdentifier(pressed, "drawable", this.packageName);
	    int selectpressId = this.mResources.getIdentifier(checkpressed, "drawable", this.packageName);

	    if ((originId == 0) && (selectId == 0) && (pressedId == 0) && (selectpressId == 0)) {
	      String suffix = (is9Png ? ".9" : "") + ".png";
	      return new AssetsUtil.StateListDrawableBuilder(this.mContext, "drawable/" + origin + suffix).setPressDrawable("drawable/" + pressed + suffix).setCheckedDrawable("drawable/" + checked + suffix).setPressCheckedDrawable("drawable/" + checkpressed + suffix).create();
	    }

	    StateListDrawable mStateListDrawable = new StateListDrawable();
	    mStateListDrawable.addState(new int[] { stateChecked }, this.mResources.getDrawable(selectId));

	    mStateListDrawable.addState(new int[] { statePressed }, this.mResources.getDrawable(pressedId));

	    mStateListDrawable.addState(new int[] { statePressed, stateChecked }, this.mResources.getDrawable(selectpressId));

	    mStateListDrawable.addState(new int[0], this.mResources.getDrawable(originId));
	    return mStateListDrawable;
	  }

	  public Drawable getCheckStatusDrawable(String origin, String checked, boolean is9Png)
	  {
	    int stateChecked = 16842912;

	    int originId = this.mResources.getIdentifier(origin, "drawable", this.packageName);
	    int selectId = this.mResources.getIdentifier(checked, "drawable", this.packageName);

	    if ((originId == 0) && (selectId == 0)) {
	      String suffix = (is9Png ? ".9" : "") + ".png";
	      return new AssetsUtil.StateListDrawableBuilder(this.mContext, "drawable/" + origin + suffix).setCheckedDrawable("drawable/" + checked + suffix).create();
	    }

	    StateListDrawable mStateListDrawable = new StateListDrawable();
	    mStateListDrawable.addState(new int[] { stateChecked }, this.mResources.getDrawable(selectId));

	    mStateListDrawable.addState(new int[0], this.mResources.getDrawable(originId));
	    return mStateListDrawable;
	  }

	  public ColorStateList createSelector(int checkedClr, int normalClr)
	  {
	    int stateChecked = 16842912;
	    int stateNormal = 0;
	    int[][] state = { { stateChecked }, { stateNormal } };
	    int[] color = { checkedClr, normalClr };
	    ColorStateList colorStateList = new ColorStateList(state, color);
	    return colorStateList; } 
	  static class AssetsUtil { private static final String LOG_TAG = CustomResourceMgmtUtils.class.getSimpleName();
	    private static AssetsUtil mAssetsUtil;
	    Context mContext;
	    Resources mResouces;
	    AssetManager mAssetManager;

	    private AssetsUtil(Context context) { this.mContext = context;
	      this.mResouces = this.mContext.getResources();
	      this.mAssetManager = this.mResouces.getAssets(); }

	    public static AssetsUtil getInstance(Context context)
	    {
	      if (mAssetsUtil != null) {
	        return mAssetsUtil;
	      }
	      synchronized (AssetsUtil.class) {
	        if (mAssetsUtil == null) {
	          mAssetsUtil = new AssetsUtil(context);
	        }
	      }
	      return mAssetsUtil;
	    }

	    public Drawable getDrawable(String fileName) {
	      return getDrawable(fileName, 480);
	    }

	    public Drawable getDrawableFromXml(String fileName) {
	      Drawable drawable = null;
	      try {
	        XmlPullParser parser = this.mAssetManager.openXmlResourceParser(fileName);
	        drawable = Drawable.createFromXml(this.mResouces, parser);
	      } catch (Exception e) {
	        Log.e(LOG_TAG, e.getMessage());
	      }
	      return drawable;
	    }

	    public String getString(String fileName) {
	      InputStream in = null;
	      String strResponse = "";
	      try {
	        in = this.mAssetManager.open(fileName);
	        strResponse = getStringFromInputStream(in);
	        String str1 = strResponse;
	        return str1;
	      }
	      catch (Exception localException3)
	      {
	    	  
	      } finally {
	        try {
	          if (in != null)
	            in.close();
	        } catch (Exception localException3) {
	        }
	      }
	      return null;
	    }

	    private String getStringFromInputStream(InputStream a_is) {
	      BufferedReader br = null;
	      StringBuilder sb = new StringBuilder();
	      try
	      {
	        br = new BufferedReader(new InputStreamReader(a_is, "UTF-8"));
	        String line;
	        while ((line = br.readLine()) != null)
	          sb.append(line);
	      }
	      catch (IOException e) {
	        Log.e(LOG_TAG, "" + e);
	      } finally {
	        if (br != null)
	          try {
	            br.close();
	          }
	          catch (IOException e) {
	          }
	      }
	      return sb.toString();
	    }

	    public Drawable getDrawable(String fileName, int density) {
	      InputStream in = null;
	      try {
	        in = this.mAssetManager.open(fileName);
	        TypedValue value = new TypedValue();

	        value.density = density;
	        Drawable localDrawable = Drawable.createFromResourceStream(this.mResouces, value, in, null);

	        Drawable localDrawable1 = localDrawable;
	        return localDrawable1;
	      }
	      catch (Exception localException3)
	      {

	      } finally {
	        try {
	          if (in != null)
	            in.close();
	        } catch (Exception localException3) {
	        }
	      }
	      return null;
	    }

	    public View getLayout(String fileName, ViewGroup root) {
	      XmlResourceParser parser = null;
	      try {
	        parser = this.mAssetManager.openXmlResourceParser("assets/" + fileName);
	        View localView = LayoutInflater.from(this.mContext).inflate(parser, root);
	        return localView;
	      }
	      catch (Exception localException3)
	      {

	      } finally {
	        try {
	          if (parser != null)
	            parser.close();
	        } catch (Exception localException3) {
	        }
	      }
	      return null;
	    }
	    public static class StateListDrawableBuilder { AssetsUtil mAssetsUtil;
	      StateListDrawable mStateListDrawable = new StateListDrawable();
	      Drawable normalDrawable;

	      public StateListDrawableBuilder(Context context, String fileName) { this.mAssetsUtil = AssetsUtil.getInstance(context);
	        this.normalDrawable = this.mAssetsUtil.getDrawable(fileName, 360); }

	      public StateListDrawableBuilder(Context context, String normal, String pressed)
	      {
	        this.mAssetsUtil = AssetsUtil.getInstance(context);
	        this.normalDrawable = this.mAssetsUtil.getDrawableFromXml(normal);
	        this.mStateListDrawable.addState(new int[] { 16842919, 16842910 }, this.mAssetsUtil.getDrawableFromXml(pressed));
	      }

	      public StateListDrawableBuilder(Context context, String normal, String pressed, String checked, String checkPressed)
	      {
	        this.mAssetsUtil = AssetsUtil.getInstance(context);
	        this.normalDrawable = this.mAssetsUtil.getDrawableFromXml(normal);
	        this.mStateListDrawable.addState(new int[] { 16842912, 16842919 }, this.mAssetsUtil.getDrawableFromXml(checkPressed));

	        this.mStateListDrawable.addState(new int[] { 16842912, 16842910 }, this.mAssetsUtil.getDrawableFromXml(checked));

	        this.mStateListDrawable.addState(new int[] { 16842919, 16842910 }, this.mAssetsUtil.getDrawableFromXml(pressed));
	      }

	      public StateListDrawableBuilder setPressDrawable(String fileName)
	      {
	        this.mStateListDrawable.addState(new int[] { 16842919, 16842910 }, this.mAssetsUtil.getDrawable(fileName, 360));

	        return this;
	      }

	      public StateListDrawableBuilder setCheckedDrawable(String fileName) {
	        this.mStateListDrawable.addState(new int[] { 16842912 }, this.mAssetsUtil.getDrawable(fileName, 360));

	        return this;
	      }

	      public StateListDrawableBuilder setPressCheckedDrawable(String fileName) {
	        this.mStateListDrawable.addState(new int[] { 16842919, 16842910, 16842912 }, this.mAssetsUtil.getDrawable(fileName, 360));

	        return this;
	      }

	      public StateListDrawableBuilder setActivateDrawable(String fileName) {
	        this.mStateListDrawable.addState(new int[] { 16843518, 16842910 }, this.mAssetsUtil.getDrawable(fileName));

	        return this;
	      }

	      public StateListDrawableBuilder setSelectDrawable(String fileName) {
	        this.mStateListDrawable.addState(new int[] { 16842913, 16842910 }, this.mAssetsUtil.getDrawable(fileName));

	        return this;
	      }

	      public StateListDrawable create() {
	        this.mStateListDrawable.addState(new int[0], this.normalDrawable);

	        return this.mStateListDrawable;
	      }
	    }
	  }
}
