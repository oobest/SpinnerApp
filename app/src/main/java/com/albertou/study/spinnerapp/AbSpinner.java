package com.albertou.study.spinnerapp;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

/**
 * <p>
 * 文件名称：AbSpinner
 * </p>
 * <p>
 * 文件描述：下拉选项菜单视图，可监听弹出下拉框
 * </p>
 * <p>
 * 内容摘要：// 简要描述本文件的内容，包括主要模块、函数及其功能的说明
 * </p>
 * <p>
 * 其他说明：// 其它内容的说明
 * </p>
 * <p>
 * 修改记录1：//修改历史记录，包括修改日期、修改者及修改内容
 * </p>
 * <p>
 * <pre>
 * 修改日期：
 * 版 本 号：
 * 修 改 人：
 * 修改内容：
 * </pre>
 * <p>
 * 修改记录2：//修改历史记录，包括修改日期、修改者及修改内容
 * </p>
 *
 * @author oujf
 * @version 创建时间：2018/9/7 09:17
 */
public class AbSpinner extends AppCompatSpinner {

    public interface OnSpinnerPerformListener {
        void perform();
    }

    private OnSpinnerPerformListener mOnSpinnerPerformListener;

    public void setOnSpinnerPerformListener(OnSpinnerPerformListener onSpinnerPerformListener) {
        mOnSpinnerPerformListener = onSpinnerPerformListener;
    }

    public AbSpinner(Context context) {
        super(context);
    }

    public AbSpinner(Context context, int mode) {
        super(context, mode);
    }

    public AbSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public AbSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }


    @Override
    public boolean performClick() {
        boolean bool = super.performClick();
        if (mOnSpinnerPerformListener != null) {
            mOnSpinnerPerformListener.perform();
        }
        return bool;
    }
}
