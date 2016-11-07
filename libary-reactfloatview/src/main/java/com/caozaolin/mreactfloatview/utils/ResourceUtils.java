package com.caozaolin.mreactfloatview.utils;

import android.content.Context;

/**
 * Desction:该类封装了读取资源文件的方法
 * Author:caozaolin
 * Date:16/09/29
 */
public class ResourceUtils {

    public static int getLayoutId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }

    public static int getStringId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    public static int getDrawableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    public static int getStyleId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    public static Object getStyleableId(Context context, String resName){
        return context.getResources().getIdentifier(resName, "styleable", context.getPackageName());
    }

    public static int getAnimId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "anim", context.getPackageName());
    }

    public static int getId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    public static int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }
}
