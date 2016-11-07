package com.caozaolin.mreactfloatview.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StatFs;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.NumberPicker;

public class SDKUtils {

	private static final String TAG = "SDKUtils";

	public static void setAlpha(View view, float value) {
		try {
			if (Build.VERSION.SDK_INT >= 11) {
				view.setAlpha(value);
			} else {
				int ivalue = (int) (255.0F * value);
				view.getBackground().setAlpha(ivalue);
			}
		} catch (Throwable e) {

		}
	}

	@SuppressLint("NewApi")
	public static void setBackground(View view, Drawable drawable) {
		try {
			if (Build.VERSION.SDK_INT >= 16)
				view.setBackground(drawable);
			else
				view.setBackgroundDrawable(drawable);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void setCheckBoxBackground(CheckBox mChechBox,
			Drawable drawable) {
		mChechBox.setButtonDrawable(drawable);
	}

	public static int getChildCount(NumberPicker np) {
		try {
			if (Build.VERSION.SDK_INT >= 11) {
				return np.getChildCount();
			}
		} catch (Throwable e) {

		}

		return 0;
	}

	public static View getChildAt(NumberPicker np, int index) {
		try {
			if (Build.VERSION.SDK_INT >= 11) {
				return np.getChildAt(index);
			}
		} catch (Throwable e) {

		}
		return null;
	}

	public static AlertDialog.Builder buildAlertDialog(Context context) {
		AlertDialog.Builder builder = null;
		try {
			if (Build.VERSION.SDK_INT >= 11)
				builder = new AlertDialog.Builder(context, 5);
			else
				builder = new AlertDialog.Builder(context);
		} catch (Throwable e) {

		}

		return builder;
	}

	@SuppressLint("NewApi")
	public static Point getScreenHeightWidth(WindowManager mWindowManager) {
		Point point = new Point();
		try {
			Display display = mWindowManager.getDefaultDisplay();
			if (Build.VERSION.SDK_INT >= 17) {
				display.getRealSize(point);
			} else {
				point.y = display.getHeight();
				point.x = display.getWidth();
			}
		} catch (Throwable e) {

		}

		return point;
	}

	@SuppressLint("NewApi")
	public static Long getFreeSpaceSize(StatFs statFs) {
		long freeSpaceSize = 0L;
		try {
			if (Build.VERSION.SDK_INT >= 18)
				freeSpaceSize = statFs.getFreeBlocksLong()
						* statFs.getBlockSizeLong();
			else
				freeSpaceSize = statFs.getFreeBlocks() * statFs.getBlockSize();
		} catch (Throwable e) {

		}

		return Long.valueOf(freeSpaceSize);
	}

	public static void setFullscreen(Activity activity) {
		try {
			if (Build.VERSION.SDK_INT >= 8) {
				activity.getWindow().setFlags(-1, -1);
			} else
				activity.getWindow().setFlags(-1, -1);
		} catch (Throwable e) {

		}
	}
}
