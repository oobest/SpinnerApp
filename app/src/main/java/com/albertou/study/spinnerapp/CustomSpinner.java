package com.albertou.study.spinnerapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文件名称：CustomSpinner
 * </p>
 * <p>
 * 文件描述：//自定义下拉选择框,带hint
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
 * @version 创建时间：2018/9/5 21:04
 */
public class CustomSpinner extends FrameLayout {

    public AbSpinner mAppCompatSpinner;

    public TextView mTextView;

    public CustomSpinner(@NonNull Context context) {
        this(context, null);
    }

    public CustomSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        CharSequence hintText = "";
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner, defStyleAttr, 0);
            hintText = a.getText(R.styleable.CustomSpinner_hint);
            a.recycle();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        mAppCompatSpinner = (AbSpinner) inflater.inflate(R.layout.spinner_item, this, false);
        FrameLayout.LayoutParams spinnerLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinnerLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        addView(mAppCompatSpinner, spinnerLayoutParams);


        mTextView = (TextView) inflater.inflate(R.layout.spinner_stytle, this, false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        //        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.spinner_hint_text_right_margin);
        addView(mTextView, layoutParams);

        if (!TextUtils.isEmpty(hintText)) {
            mTextView.setVisibility(VISIBLE);
            mTextView.setText("");
            setHint(hintText);
        } else {
            mTextView.setVisibility(GONE);
        }
    }

    public void setHint(CharSequence text) {
        mTextView.setHint(text);
    }


    public void setText(CharSequence text) {
        mTextView.setText(text);
    }

    /**
     * 下拉菜单控制器
     *
     * @param <T>
     */
    public abstract static class SpinnerController<T> {

        private List<T> mObjects;

        private CustomSpinner mCustomSpinner;

        public SpinnerController(CustomSpinner spinner) {
            mCustomSpinner = spinner;
            initClick();
        }

        private void initClick() {
            mCustomSpinner.mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 < mObjects.size()) {
                        T object = mObjects.get(arg2);
                        mCustomSpinner.mTextView.setVisibility(GONE);
                        SpinnerController.this.onItemSelected(arg2, object);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            mCustomSpinner.mTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mObjects.size() > 0) {
                        performClick();
                    }
                }
            });

            mCustomSpinner.mAppCompatSpinner.setOnSpinnerPerformListener(new AbSpinner.OnSpinnerPerformListener() {
                @Override
                public void perform() {
                    if (mCustomSpinner.mAppCompatSpinner.getSelectedItemPosition() == mObjects.size()) {
                        // 如果显示hint text 则让弹出的list滚动到
                        scrollToPosition(0);
                    }
                }
            });
        }

        private void scrollToPosition(int position) {
            try {
                Field field = AppCompatSpinner.class.getDeclaredField("mPopup");
                field.setAccessible(true);
                Object popupWindow = field.get(mCustomSpinner.mAppCompatSpinner);
                if (popupWindow != null) {
                    ListView listView = ((ListPopupWindow) popupWindow).getListView();
                    if (listView != null) {
                        listView.setSelection(position);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public abstract String getItemName(int position, T object);

        public abstract void onItemSelected(int position, T object);

        public abstract boolean compare(T object1, T object2);

        public void performClick() {
            mCustomSpinner.mAppCompatSpinner.performClick();
        }


        public void setData(@NonNull List<T> objects, T selectedObject) {
            int selectedPosition = -1;
            if (selectedObject != null) {
                for (int i = 0; i < objects.size(); i++) {
                    if (compare(selectedObject, objects.get(i))) {
                        selectedPosition = i;
                        break;
                    }
                }
            }
            setData(objects, selectedPosition);
        }

        public void setData(@NonNull List<T> objects, int selectedPosition) {
            mObjects = objects;
            if (objects.size() == 0) {
                mCustomSpinner.mTextView.setVisibility(VISIBLE);
                mCustomSpinner.mTextView.setText("");
                mCustomSpinner.mAppCompatSpinner.setVisibility(GONE);
            } else {
                mCustomSpinner.mTextView.setVisibility(GONE);
                mCustomSpinner.mAppCompatSpinner.setVisibility(VISIBLE);
                initAdapter();
                if (objects.size() == 1) {
                    selectedPosition = 0;
                } else if (selectedPosition == -1) {
                    selectedPosition = mObjects.size();
                }
                mCustomSpinner.mAppCompatSpinner.setSelection(selectedPosition, true);
            }
        }

        /**
         * 初始化adapter
         */
        private void initAdapter() {
            ArrayAdapter adapter = new MyArrayAdapter(
                    mCustomSpinner.getContext(), R.layout.spinner_stytle) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    if (position == getCount()) {
                        final TextView textView;
                        if (convertView == null) {
                            LayoutInflater layoutInflater = LayoutInflater.from(mCustomSpinner.getContext());
                            textView = (TextView) layoutInflater.inflate(R.layout.spinner_stytle, null);
                        } else {
                            textView = (TextView) convertView;
                        }
                        CharSequence hintText = mCustomSpinner.mTextView.getHint();
                        textView.setHint(hintText);
                        textView.setText("");
                        return textView;
                    } else {
                        return super.getView(position, convertView, parent);
                    }
                }
            };
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
            mCustomSpinner.mAppCompatSpinner.setAdapter(adapter);
        }

        public void clear() {
            mObjects = new ArrayList<>();
            initAdapter();
            mCustomSpinner.mTextView.setVisibility(VISIBLE);
        }

        private class MyArrayAdapter extends ArrayAdapter<String> {

            public MyArrayAdapter(@NonNull Context context, int resource) {
                super(context, resource);
            }

            @Nullable
            @Override
            public String getItem(int position) {
                T object = mObjects.get(position);
                return getItemName(position, object);
            }

            @Override
            public int getCount() {
                return mObjects.size();
            }

        }

    }
}
