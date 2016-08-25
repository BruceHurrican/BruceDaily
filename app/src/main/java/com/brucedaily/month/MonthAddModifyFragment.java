/*
 * BruceHurrican
 *  Copyright (c) 2016.
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     This document is Bruce's individual learning the android demo, wherein the use of the code from the Internet, only to use as a learning exchanges.
 *     And where any person can download and use, but not for commercial purposes.
 *     Author does not assume the resulting corresponding disputes.
 *     If you have good suggestions for the code, you can contact BurrceHurrican@foxmail.com
 *     本文件为Bruce's个人学习android的作品, 其中所用到的代码来源于互联网，仅作为学习交流使用。
 *     任和何人可以下载并使用, 但是不能用于商业用途。
 *     作者不承担由此带来的相应纠纷。
 *     如果对本代码有好的建议，可以联系BurrceHurrican@foxmail.com
 */

package com.brucedaily.month;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.brucedaily.AppUtils;
import com.brucedaily.R;
import com.bruceutils.base.BaseFragment;
import com.bruceutils.utils.LogUtils;
import com.bruceutils.utils.logdetails.LogDetails;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * 增加 修改界面
 * Created by BruceHurrican on 16/8/22.
 */
public class MonthAddModifyFragment extends BaseFragment {
    @Bind(R.id.actw_title)
    AutoCompleteTextView actwTitle;
    @Bind(R.id.actw_content)
    AutoCompleteTextView actwContent;
    @Bind(R.id.actw_time)
    AutoCompleteTextView actwTime;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    private boolean isAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_item_add_modify, container, false);
        ButterKnife.bind(this, view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogDetails.i("拦截 fragment 触摸事件生效");
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        isAdd = bundle.getBoolean(MonthDailyActivity.KEY_IS_ADD);
        if (!isAdd) {
            actwTitle.setHint(bundle.getString(MonthDailyActivity.KEY_COST_TITLE));
            actwContent.setHint(bundle.getString(MonthDailyActivity.KEY_COST_DETAIL));
            actwTime.setHint(bundle.getString(MonthDailyActivity.KEY_COST_DAY));
            etPrice.setHint(bundle.getString(MonthDailyActivity.KEY_COST_PRICE));
        }
        initACTWdata(actwTitle, actwContent, actwTime);
    }

    @OnFocusChange({R.id.actw_title, R.id.actw_content, R.id.actw_time})
    public void onFocusChange(final View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.actw_title:
                LogDetails.i("title 获取焦点");
                break;
            case R.id.actw_content:
                LogDetails.i("content 获取焦点");
                break;
            case R.id.actw_time:
                LogDetails.i("time 获取焦点");
                break;
        }
        if (hasFocus) {
            ((AutoCompleteTextView) v).showDropDown();
        }
    }

    private void initACTWdata(AutoCompleteTextView titleView, AutoCompleteTextView contentView, AutoCompleteTextView timeView) {
        // 初始化标题候选列表
        List<String> titleList = new ArrayList<>(10);
        titleList.add("早餐");
        titleList.add("中餐");
        titleList.add("晚餐");
        titleList.add("超市购物");
        titleList.add("淘宝购物");
        titleList.add("房租");
        titleList.add("充公交卡");
        titleList.add("充手机话费");
        titleList.add("孝敬长辈");
        titleList.add("房东小店");
        titleList.add("地铁坐摩的到宿舍");
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.month_data_item, titleList);
        titleView.setAdapter(titleAdapter);

        // 初始化内容候选列表
        List<String> contentList = new ArrayList<>(8);
        contentList.add("现金");
        contentList.add("招行信用卡支出");
        contentList.add("广发信用卡支出");
        contentList.add("余额宝支出");
        contentList.add("蚂蚁花呗支出");
        contentList.add("微信转账");
        contentList.add("平安卡支出");
        contentList.add("招行卡支出");
        ArrayAdapter<String> contentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.month_data_item, contentList);
        contentView.setAdapter(contentAdapter);

        // 初始化时候候选列表
        List<Byte> timeList = new ArrayList<>(31);
        for (byte i = 1; i <= 31; i++) {
            timeList.add(i);
        }
        ArrayAdapter<Byte> timeAdapter = new ArrayAdapter<Byte>(getActivity(), R.layout.month_data_item, timeList);
        timeView.setAdapter(timeAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                String title = actwTitle.getEditableText().toString().trim();
                String content = actwContent.getEditableText().toString().trim();
                String time = actwTime.getEditableText().toString().trim();
                String price = etPrice.getEditableText().toString().trim();
                MsgBean msgBean = new MsgBean(isAdd, title, content, time, price);
                if (isAdd) {
                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(time) || TextUtils.isEmpty(price)) {
                        LogDetails.i("必填项不能为空");
                        showToastShort("必填项不能为空");
                        return;
                    }
                    if (!AppUtils.isPriceValid(price)) {
                        LogUtils.e("输入的价格不符合格式规范");
                        showToastShort("输入的价格不符合格式规范");
                        return;
                    }
                    int tmpTime = Integer.valueOf(time);
                    LogDetails.i("tmpTime-" + tmpTime);
                    if (tmpTime >= 32) {
                        LogDetails.w("输入非法数据");
                        showToastShort("日期不能大于31的数字");
                        return;
                    }
                    EventBus.getDefault().post(msgBean);
                } else if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content) && TextUtils.isEmpty(time) && TextUtils.isEmpty(price)) {
                    LogDetails.w("数据未做任何修改");
                    showToastShort("亲,未修改任何信息喔~");
                } else {
                    if (TextUtils.isEmpty(price)) {
                        price = etPrice.getHint().toString().trim();
                        msgBean.price = price;
                    }
                    LogUtils.i("修改数据");
                    EventBus.getDefault().post(msgBean);
                }
                break;
            case R.id.btn_cancel:
                InputMethodManager im = ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (im.isActive() && getActivity().getCurrentFocus() != null) {
                    im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
