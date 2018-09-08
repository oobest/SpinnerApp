package com.albertou.study.keyboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

public class CustomInputEditView extends AppCompatTextView {

    private String mDigits; //可输入的内容

    private int mNextViewResId;

    private DropdownPopup mPopup;

    public CustomInputEditView(Context context) {
        this(context, null);
    }

    public CustomInputEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomInputEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            mDigits = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "digits");
            Log.d("digits", "CustomInputEditView: mDigits=" + mDigits);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInputEditView);
            mNextViewResId = typedArray.getResourceId(R.styleable.CustomInputEditView_next, 0);
            typedArray.recycle();

        }
        this.mPopup = new DropdownPopup(context);
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    @Override
    public boolean performClick() {
        if (!isShowing()) {
            Context context = getContext();
            if (context instanceof Activity) {
                hideInputMethod((Activity) context);
            }
            mPopup.show();
        }
        return super.performClick();
    }

    /**
     * 强制隐藏键盘
     */
    public void hideInputMethod(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void next() {
        mPopup.dismiss();
        View v = getRootView().findViewById(mNextViewResId);
        if (v != null) {
            if (v instanceof CustomInputEditView) {
                v.performClick();
            } else if (v instanceof EditText) {
                boolean bool = v.requestFocus();
                if (bool) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, 0);
                }

            }
        }
    }

    private CustomKeyboardView.OnItemClickListener mOnItemClickListener = new CustomKeyboardView.OnItemClickListener() {
        @Override
        public void onClick(String value) {
            if ("undo".equals(value)) {
                CharSequence charSequence = getText();
                if (!TextUtils.isEmpty(charSequence)) {
                    setText(charSequence.subSequence(0, charSequence.length() - 1));
                }
            } else if ("next".equals(value)) {
                next();
            } else {
                append(value);
            }
        }
    };

    private class DropdownPopup extends PopupWindow {

        private CustomKeyboardView mCustomKeyboardView;

        private DropdownPopup(Context context) {
            super(context);
            mCustomKeyboardView = new CustomKeyboardView(context);
            mCustomKeyboardView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            if (!TextUtils.isEmpty(mDigits)) {
                mCustomKeyboardView.setDigits(mDigits);
            }
            mCustomKeyboardView.setOnItemClickListener(mOnItemClickListener);
            setContentView(mCustomKeyboardView);
            setOutsideTouchable(true);
            setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        private void show() {
            this.setInputMethodMode(2);
            showAsDropDown(CustomInputEditView.this);
        }
    }
}
