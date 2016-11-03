package com.caozaolin.reactfloatview.task;


import com.caozaolin.reactfloatview.view.GameAssistWindowManager;

/**
 * Desction:该类实现了brand形态悬浮窗的实现
 * Author:caozaolin
 * Date:16/09/29
 */
public class BrandWindowShowTask {
	public void showBrandWindow(GameAssistWindowManager gameAssistWindowManager) {
		if (gameAssistWindowManager != null)
			gameAssistWindowManager.createBrandWindow();
	}
}
