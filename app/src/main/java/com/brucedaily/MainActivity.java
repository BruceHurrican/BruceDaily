/*
 * BruceHurrican
 * Copyright (c) 2016.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    This document is Bruce's individual learning the android demo, wherein the use of the code from the Internet, only to use as a learning exchanges.
 *    And where any person can download and use, but not for commercial purposes.
 *    Author does not assume the resulting corresponding disputes.
 *    If you have good suggestions for the code, you can contact BurrceHurrican@foxmail.com
 *    本文件为Bruce's个人学习android的作品, 其中所用到的代码来源于互联网，仅作为学习交流使用。
 *    任和何人可以下载并使用, 但是不能用于商业用途。
 *    作者不承担由此带来的相应纠纷。
 *    如果对本代码有好的建议，可以联系BurrceHurrican@foxmail.com
 */

package com.brucedaily;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.brucedaily.month.MonthDailyActivity;
import com.bruceutils.base.BaseActivity;
import com.bruceutils.utils.logdetails.LogDetails;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String SP_KEY_BDAILY_1 = "bdaily1";
    public static final String SP_KEY_BDAILY_2 = "bdaily2";
    public static final String SP_KEY_BDAILY_3 = "bdaily3";
    public static final String SP_KEY_BDAILY_4 = "bdaily4";
    public static final String SP_KEY_BDAILY_5 = "bdaily5";
    public static final String SP_KEY_BDAILY_6 = "bdaily6";
    public static final String SP_KEY_BDAILY_7 = "bdaily7";
    EditText etItem;
    Button btnAdd;
    Button btnClear;
    ListView lvData;
    RadioButton rbtn1;
    RadioButton rbtn2;
    RadioButton rbtn3;
    RadioButton rbtn4;
    RadioButton rbtn5;
    RadioButton rbtn6;
    RadioButton rbtn7;
    RadioGroup radioGroup;
    Button btnModify;

    private String customItem = "";
    private ArrayList<String> dataList;
    private int flag;
    private ArrayAdapter adapter;
    private String[] keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        customItem = SharedPreferencesUtil.getString(MainActivity.this, keys[flag], "");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_1:
                        flag = 0;
                        break;
                    case R.id.rbtn_2:
                        flag = 1;
                        break;
                    case R.id.rbtn_3:
                        flag = 2;
                        break;
                    case R.id.rbtn_4:
                        flag = 3;
                        break;
                    case R.id.rbtn_5:
                        flag = 4;
                        break;
                    case R.id.rbtn_6:
                        flag = 5;
                        break;
                    case R.id.rbtn_7:
                        flag = 6;
                        break;
                }
                customItem = SharedPreferencesUtil.getString(MainActivity.this, keys[flag], "");
            }
        });
    }

    private void initView() {
        etItem = (EditText) findViewById(R.id.et_item);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnModify = (Button) findViewById(R.id.btn_modify);
        btnClear = (Button) findViewById(R.id.btn_clear);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        rbtn1 = (RadioButton) findViewById(R.id.rbtn_1);
        rbtn2 = (RadioButton) findViewById(R.id.rbtn_2);
        rbtn3 = (RadioButton) findViewById(R.id.rbtn_3);
        rbtn4 = (RadioButton) findViewById(R.id.rbtn_4);
        rbtn5 = (RadioButton) findViewById(R.id.rbtn_5);
        rbtn6 = (RadioButton) findViewById(R.id.rbtn_6);
        rbtn7 = (RadioButton) findViewById(R.id.rbtn_7);
        lvData = (ListView) findViewById(R.id.lv_data);

        btnAdd.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    private void initData() {
        dataList = new ArrayList<>(7);
        keys = new String[7];

        keys[0] = SP_KEY_BDAILY_1;
        keys[1] = SP_KEY_BDAILY_2;
        keys[2] = SP_KEY_BDAILY_3;
        keys[3] = SP_KEY_BDAILY_4;
        keys[4] = SP_KEY_BDAILY_5;
        keys[5] = SP_KEY_BDAILY_6;
        keys[6] = SP_KEY_BDAILY_7;

        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_1, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_2, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_3, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_4, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_5, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_6, "待记录"));
        dataList.add(SharedPreferencesUtil.getString(MainActivity.this, SP_KEY_BDAILY_7, "待记录"));
        LogDetails.i(dataList);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
        lvData.setAdapter(adapter);

        // TODO: 2016/7/24 临时入口
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, MonthDailyActivity.class));
            }
        });
    }

    public void onClick(View view) {
        String tmp = etItem.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_add:
                if (TextUtils.isEmpty(tmp)) {
                    LogDetails.i("不能操作空数据");
                    showToastShort("不能操作空数据");
                    return;
                }
                if (customItem.equals(tmp)) {
                    LogDetails.i("不能重复添加数据");
                    showToastShort("不能重复添加数据~");
                    return;
                }
                customItem = customItem + "-" + tmp;
                dataList.set(flag, customItem);
                SharedPreferencesUtil.saveString(MainActivity.this, keys[flag], customItem);
                adapter.notifyDataSetChanged();
                etItem.setText("");
                break;
            case R.id.btn_modify:
                if (TextUtils.isEmpty(tmp)) {
                    LogDetails.i("不能操作空数据");
                    showToastShort("不能操作空数据");
                    return;
                }
                if (customItem.equals(tmp)) {
                    LogDetails.i("不能重复修改数据");
                    showToastShort("不能重复修改数据~");
                    return;
                }
                customItem = tmp;
                dataList.set(flag, customItem);
                SharedPreferencesUtil.saveString(MainActivity.this, keys[flag], customItem);
                adapter.notifyDataSetChanged();
                LogDetails.i("修改一条数据");
                showToastShort("修改一条数据");
                etItem.setText("");
                break;
            case R.id.btn_clear:
                dataList.set(flag, "待记录");
                SharedPreferencesUtil.saveString(MainActivity.this, keys[flag], "待记录");
                adapter.notifyDataSetChanged();
                LogDetails.i("删除一条数据");
                showToastShort("删除一条数据");
                etItem.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
    }
}
