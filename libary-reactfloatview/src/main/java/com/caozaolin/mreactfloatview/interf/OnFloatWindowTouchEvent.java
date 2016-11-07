package com.caozaolin.mreactfloatview.interf;

/**
 * Desction:悬浮窗的相关接口
 * Author:caozaolin
 * Date:16/09/29
 */
public abstract interface OnFloatWindowTouchEvent {
	/**
	 * Desction: 当悬浮窗处于big形态时候，拖动触发的接口
	 * @param x 悬浮窗在屏幕中的x坐标点
	 * @param y 悬浮窗在屏幕中的y坐标点
     */
	public abstract void onTouchEventBig(float x, float y);

	/**
	 * Desction: 当悬浮窗处于small形态时候，拖动触发的接口
	 * @param x 悬浮窗在屏幕中的x坐标点
	 * @param y 悬浮窗在屏幕中的y坐标点
	 */
	public abstract void onTouchEventSmall(float x, float y);

	/**
	 * Desction: 当悬浮窗处于small形态时候，拖动触发的接口
	 * @param x 悬浮窗在屏幕中的x坐标点
	 * @param y 悬浮窗在屏幕中的y坐标点
	 */
	public abstract void onTouchEventBrand(float x, float y);

	/**
	 * Desction: 预留接口
	 */
	public abstract void changeuserListen();
}
