package com.caozaolin.mreactfloatview;

import android.app.Activity;

public class ZSDK {

	private static ZSDK instance;
	private Activity context;

	private ZSDK() {

	}

	public static ZSDK getInstance() {
		if (instance == null) {
			instance = new ZSDK();
		}
		return instance;
	}

	public void init(Activity context) {
		this.context = context;
	}

	public Activity getContext() {
		return this.context;

	}

}
