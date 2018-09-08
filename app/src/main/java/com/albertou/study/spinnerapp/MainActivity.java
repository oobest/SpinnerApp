package com.albertou.study.spinnerapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomSpinner spinner;
    private static final String[] cities = {"北京", "上海", "重庆", "广州", "深圳",
            "北京1", "上海1", "重庆1", "广州1", "深圳1",
            "北京2", "上海2", "重庆2", "广州2", "深圳2",
            "北京3", "上海3", "重庆3", "广州3", "深圳3",
            "北京4", "上海4", "重庆4", "广州4", "深圳4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> cityList = new ArrayList<String>();
        for (int i = 0; i < cities.length; i++) {
            cityList.add(cities[i]);
        }

        spinner = findViewById(R.id.spinner1);

        final CustomSpinner.SpinnerController<String> controller = new CustomSpinner.SpinnerController<String>(spinner) {
            @Override
            public String getItemName(int position, String object) {
                return object;
            }

            @Override
            public void onItemSelected(int position, String object) {
                Log.d("MainActivity", "onClick: " + object);
            }

            @Override
            public boolean compare(String object1, String object2) {
                return object1.equals(object2);
            }
        };
        controller.setData(cityList,-1);

        TextView textView = findViewById(R.id.textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.performClick();
            }
        });

    }
}
