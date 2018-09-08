package com.albertou.study.keyboardapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * <p>
 * 文件名称：CustomKeyboardView
 * </p>
 * <p>
 * 文件描述：自定义输入键盘
 * </p>
 * <p>
 * 内容摘要：将文本框与自定义键盘绑定，点击输入键盘，文本框发生变化
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
 * @version 创建时间：2018/9/8 03:17
 */
public class CustomKeyboardView extends LinearLayout implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(String value);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public CustomKeyboardView(Context context) {
        this(context, null);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_normal_key_board, this, true);
        initClick(R.id.button0);
        initClick(R.id.button1);
        initClick(R.id.button2);
        initClick(R.id.button3);
        initClick(R.id.button4);
        initClick(R.id.button5);
        initClick(R.id.button6);
        initClick(R.id.button7);
        initClick(R.id.button8);
        initClick(R.id.button9);
        initClick(R.id.button_undo);
        initClick(R.id.button_point);
        initClick(R.id.button_comma);
        initClick(R.id.button_line);
        initClick(R.id.button_next);
    }

    private void initClick(int resId) {
        View view = findViewById(resId);
        view.setOnClickListener(this);
    }


    private void callback(String value) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick(value);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.button_undo:
                    mOnItemClickListener.onClick("undo");
                    break;
                case R.id.button_next:
                    mOnItemClickListener.onClick("next");
                    break;
                default:
                    if (v instanceof Button) {
                        Button button = (Button) v;
                        mOnItemClickListener.onClick(button.getText().toString());
                    }

            }
        }
    }

    public void setDigits(@NonNull String digits) {
        if (!digits.contains(".") && digits.contains(",") && digits.contains("-")) {
            setSymbolVisible(false);
        } else {
            setSymbolVisible(true);
        }
    }

    /**
     * 是否显示符号
     *
     * @param visible
     */
    public void setSymbolVisible(boolean visible) {
        setViewVisible(R.id.button_point, visible);
        setViewVisible(R.id.button_comma, visible);
        setViewVisible(R.id.button_line, visible);
    }

    private void setViewVisible(int resId, boolean visible) {
        findViewById(resId).setVisibility(visible ? VISIBLE : GONE);
    }
}
