package com.caozaolin.mreactfloatview.services;

import android.os.Handler;


import com.caozaolin.mreactfloatview.task.BrandWindowShowTask;
import com.caozaolin.mreactfloatview.view.GameAssistWindowManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Desction:该类控制了small和brand两种形态的转变
 * Author:caozaolin
 * Date:16/09/29
 */
public class GamePlugIdleTimeService {
	public static final int DEAFAULT_IDLE_TIME = 10;
	private ScheduledThreadPoolExecutor mGameIdleTimeExec = null;
	private volatile GameAssistWindowManager mGameAssistWindowManager;
	private volatile BrandWindowShowTask brandWindowShowTask = null;
	private volatile Runnable brandWindowShowRunnable;

	public GamePlugIdleTimeService(GameAssistWindowManager gameAssistWindowManager) {
		this.mGameIdleTimeExec = new ScheduledThreadPoolExecutor(1);
		this.mGameAssistWindowManager = gameAssistWindowManager;
	}

	/**
	 * Desction:展示brand形态悬浮窗
	 */
	public void executeIdleTimeService(final Handler uiHandler) {
		this.brandWindowShowTask = new BrandWindowShowTask();
		this.brandWindowShowRunnable = new Runnable() {
			@Override
			public void run() {
				uiHandler.post(new Runnable()
		        {
		          public void run() {
		            GamePlugIdleTimeService.this.brandWindowShowTask.showBrandWindow(GamePlugIdleTimeService.this.mGameAssistWindowManager);
		          }
		        });
			}
		};
		this.mGameIdleTimeExec.schedule(this.brandWindowShowRunnable, 10L, TimeUnit.SECONDS);
	}

	/**
	 * Desction:关闭服务
	 */
	public void cancelIdleTimeService() {
		if (!this.mGameIdleTimeExec.isShutdown())
			this.mGameIdleTimeExec.shutdownNow();
	}
}
