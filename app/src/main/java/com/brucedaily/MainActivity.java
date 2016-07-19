package com.brucedaily;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bruceutils.base.BaseActivity;
import com.bruceutils.utils.logdetails.LogDetails;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final String SP_KEY_BDAILY_1 = "bdaily1";
    public static final String SP_KEY_BDAILY_2 = "bdaily2";
    public static final String SP_KEY_BDAILY_3 = "bdaily3";
    public static final String SP_KEY_BDAILY_4 = "bdaily4";
    public static final String SP_KEY_BDAILY_5 = "bdaily5";
    public static final String SP_KEY_BDAILY_6 = "bdaily6";
    public static final String SP_KEY_BDAILY_7 = "bdaily7";
    @Bind(R.id.et_item)
    EditText etItem;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.lv_data)
    ListView lvData;
    @Bind(R.id.rbtn_1)
    RadioButton rbtn1;
    @Bind(R.id.rbtn_2)
    RadioButton rbtn2;
    @Bind(R.id.rbtn_3)
    RadioButton rbtn3;
    @Bind(R.id.rbtn_4)
    RadioButton rbtn4;
    @Bind(R.id.rbtn_5)
    RadioButton rbtn5;
    @Bind(R.id.rbtn_6)
    RadioButton rbtn6;
    @Bind(R.id.rbtn_7)
    RadioButton rbtn7;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.btn_modify)
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
        ButterKnife.bind(this);
        LogDetails.getLogConfig().configShowBorders(true);
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
    }

    @OnClick({R.id.btn_add, R.id.btn_clear, R.id.btn_modify})
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
        ButterKnife.unbind(this);
    }
}
