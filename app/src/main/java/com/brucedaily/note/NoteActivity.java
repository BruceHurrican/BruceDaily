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

package com.brucedaily.note;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brucedaily.AppUtils;
import com.brucedaily.R;
import com.brucedaily.database.bean.CostMonth;
import com.brucedaily.month.CostAdapter;
import com.bruceutils.base.BaseActivity;
import com.bruceutils.utils.LogUtils;
import com.bruceutils.utils.ProgressDialogUtils;
import com.bruceutils.utils.logdetails.LogDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 按月统计消费金额
 * Created by BruceHurrican on 2016/10/27.
 */
public class NoteActivity extends BaseActivity {
    /**
     * 是否是添加数据
     */
    public static final String KEY_IS_ADD = "isAdd";
    /**
     * 消费标题
     */
    public static final String KEY_COST_TITLE = "costTitle";
    /**
     * 消费详情
     */
    public static final String KEY_COST_DETAIL = "costDetail";
    /**
     * 消费日期
     */
    public static final String KEY_COST_DAY = "costDay";
    /**
     * 消费价格
     */
    public static final String KEY_COST_PRICE = "costPrice";
    /**
     * 备份数据库
     */
    public static final String DB_BACKUP = "dbBackup";
    /**
     * 恢复数据库
     */
    public static final String DB_RESTORE = "dbRestore";
    public static final String DB_NAME = "md_db";
    /**
     * 恢复数据库成功
     */
    public static final int CODE_RESTORE_DB_SUCCESS = 100;
    /**
     * 当月上旬花费统计
     */
    public static final int CODE_MONTH_COUNT_1 = 101;
    /**
     * 当月上旬中旬花费统计
     */
    public static final int CODE_MONTH_COUNT_2 = 102;
    /**
     * 当月下旬花费统计
     */
    public static final int CODE_MONTH_COUNT_3 = 103;
    /**
     * 当月花费预算余额
     */
    public static final int CODE_MONTH_COUNT_REMAIN = 104;
    // robolectric test
    public String testTagInfo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_remain)
    TextView tvRemain;
    @Bind(R.id.tv_count_early)
    TextView tvCountEarly;
    @Bind(R.id.tv_count_middle)
    TextView tvCountMiddle;
    @Bind(R.id.tv_count_last)
    TextView tvCountLast;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.tv_rv_type)
    LinearLayout tvRvTitle;
    @Bind(R.id.rv_container)
    RecyclerView rvContainer;
    @Bind(R.id.btn_list)
    Button btnList;
    @Bind(R.id.btn_grid)
    Button btnGrid;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;
    @Bind(R.id.btn_db_back)
    Button btnDbBack;
    @Bind(R.id.btn_db_restore)
    Button btnDbRestore;
    private List<CostMonth> dataList = new ArrayList<>(31);
    private CostAdapter costAdapter;
    private int position; // 待修改数据位置
    private FragmentManager fragmentManager;
    private long exitFlag;
    private DefaultItemAnimator defaultItemAnimator;
    private NotePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_activity_daily);
        ButterKnife.bind(this);
        tvTitle.setText("月统计2");
        EventBus.getDefault().register(this);
        initUIHandler();
        ProgressDialogUtils.initProgressBar(NoteActivity.this, "操作进行中...", R.mipmap.ic_app);
        ProgressDialogUtils.showProgressDialog();
        costAdapter = new CostAdapter();

        presenter = new NotePresenter(this, dataList, new INoteActivityViewImpl());

        presenter.initDatabase();
        dataList = presenter.initData(costAdapter);
        ProgressDialogUtils.cancelProgressDialog();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContainer.setLayoutManager(manager);

        defaultItemAnimator = new DefaultItemAnimator();
        rvContainer.setItemAnimator(defaultItemAnimator);

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        rvContainer.setLayoutManager(staggeredGridLayoutManager);

        rvContainer.setAdapter(costAdapter);

        costAdapter.setItemClickListener(new CostAdapter.CardViewItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                LogDetails.i("当前选中的数据: " + dataList.get(position).costTitle);
                showDetails(position);
            }
        });
        costAdapter.setItemLongClickListener(new CostAdapter.CardViewItemLongClickListener() {
            @Override
            public void itemLongClick(View view, int position) {
                LogDetails.i("当前选中的数据: " + dataList.get(position).costPrice);
                showOperator(position);
            }
        });

        presenter.monthCount();

        fragmentManager = getFragmentManager();

        // robolectric test
        testTagInfo = AppUtils.robolectricTestInfo("onCreate");
    }

    /**
     * 显示消费详情
     *
     * @param position
     */
    private void showDetails(int position) {
        View view = getLayoutInflater().inflate(R.layout.month_item_detail, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tvTitle.setText(dataList.get(position).costTitle);
        tvDetail.setText(dataList.get(position).costDetail);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击可以消失
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rlRoot, Gravity.CENTER, 0, 0);
    }

    /**
     * 长按每个消费项目操作,当前只支持 “修改” 和 “删除” 操作
     *
     * @param position
     */
    private void showOperator(final int position) {
        LogDetails.i("position-" + position);
        View view = getLayoutInflater().inflate(R.layout.month_item_detail, null);
        TextView tvModify = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDelete = (TextView) view.findViewById(R.id.tv_detail);
        tvModify.setText("修改");
        tvDelete.setText("删除");
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击可以消失
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rlRoot, Gravity.CENTER, 0, 0);
        tvModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                operateCostRecord(position, false);
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先删除数据库中数据再删除内存中数据
//                deleteItem(dataList.get(position));
                presenter.deleteItem(dataList.get(position));
                dataList.remove(position);
                refreshData();
                popupWindow.dismiss();
                LogDetails.i("删除一条数据");
                showToastShort("删除一条数据");
            }
        });
    }

    /**
     * 刷新列表数据和统计数据
     */
    private void refreshData() {
        Collections.sort(dataList);
        LogDetails.i(dataList);
        costAdapter.notifyDataSetChanged();
        presenter.monthCount();
    }

    /**
     * 操作消费记录,当前只支持 “增加” 和 “修改” 操作
     *
     * @param position
     * @param isAdd
     */
    private void operateCostRecord(final int position, final boolean isAdd) {
        this.position = position;
        LogDetails.i("position-%s", position);

        NoteAddModifyFragment noteAddModifyFragment = new NoteAddModifyFragment();
        Bundle bundle = new Bundle();
        if (!isAdd) {
            bundle.putBoolean(KEY_IS_ADD, false);
            bundle.putString(KEY_COST_TITLE, dataList.get(position).costTitle);
            bundle.putString(KEY_COST_DETAIL, dataList.get(position).costDetail);
            bundle.putString(KEY_COST_DAY, dataList.get(position).costDay + "");
            bundle.putString(KEY_COST_PRICE, dataList.get(position).costPrice);
        } else {
            bundle.putBoolean(KEY_IS_ADD, true);
        }
        noteAddModifyFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(NoteAddModifyFragment.class.getSimpleName());
        transaction.replace(android.R.id.content, noteAddModifyFragment).commit();
        // robolectric test
        testTagInfo = AppUtils.robolectricTestInfo("operateCostRecord executed");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMsg(final NoteMsg noteMsg) {
        LogDetails.i(noteMsg);
        if (null == noteMsg) {
            LogDetails.d("msg is null");
            return;
        }
        switch (noteMsg.code) {
            case CODE_RESTORE_DB_SUCCESS:
                dataList = presenter.initData(costAdapter);
                getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        showToastShort(noteMsg.msg);
                    }
                });
                break;
            case CODE_MONTH_COUNT_1:
                tvCountEarly.setText(noteMsg.msg);
                break;
            case CODE_MONTH_COUNT_2:
                tvCountMiddle.setText(noteMsg.msg);
                break;
            case CODE_MONTH_COUNT_3:
                tvCountLast.setText(noteMsg.msg);
                break;
            case CODE_MONTH_COUNT_REMAIN:
                tvRemain.setText(noteMsg.msg);
                break;
            case NoteAddModifyFragment.CODE_MSG_OPERATER_ITEM:
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                if (im.isActive() && getCurrentFocus() != null) {
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (noteMsg.noteItem.isAdd) {
                    if (!presenter.addCostRecord(noteMsg.noteItem.title, noteMsg.noteItem.content, noteMsg.noteItem.time, noteMsg.noteItem.price)) {
                        LogDetails.i("增加记录失败");
                        return;
                    }
                } else {
                    if (!presenter.modifyCostRecord(position, noteMsg.noteItem.title, noteMsg.noteItem.content, noteMsg.noteItem.time, noteMsg.noteItem.price)) {
                        LogDetails.i("修改消费记录失败");
                        return;
                    }
                }
                refreshData();
                break;
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_clear, R.id.btn_list, R.id.btn_grid, R.id.btn_db_back, R.id.btn_db_restore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                operateCostRecord(-1, true);
                break;
            case R.id.btn_clear:
                dataList.clear();
                LogDetails.i("size-" + dataList.size());
                presenter.deleteAllData();
                refreshData();
                break;
            case R.id.btn_list:
                LinearLayoutManager manager = new LinearLayoutManager(NoteActivity.this, LinearLayoutManager.VERTICAL, false);
                rvContainer.setLayoutManager(manager);
                rvContainer.setItemAnimator(defaultItemAnimator);
                break;
            case R.id.btn_grid:
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvContainer.setLayoutManager(staggeredGridLayoutManager);
                rvContainer.setItemAnimator(defaultItemAnimator);
                break;
            case R.id.btn_db_back:
                LogDetails.i("备份数据库");
                presenter.operatorDB(DB_BACKUP);
                break;
            case R.id.btn_db_restore:
                LogDetails.i("恢复数据库");
                presenter.operatorDB(DB_RESTORE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        exitFlag = System.currentTimeMillis();
        // 将入栈的 fragment 按 FILO 规则依次出栈
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.popBackStackImmediate(null, 0)) {
            LogUtils.d("fragment栈中最上层的 fragment 出栈");
            if (fragmentManager.getBackStackEntryCount() == 0) {
                LogUtils.i("fragment 栈已经清空");
            }
            return;
        }

        if (Math.abs(exitFlag - System.currentTimeMillis()) < 2000 && exitFlag > 0) {
            super.onBackPressed();
        } else {
            showToastShort("再按一次退出应用...");
            exitFlag = System.currentTimeMillis();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (null != presenter) {
            presenter.clearReference();
        }
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        recycleUIHandler();
        // robolectric test
        testTagInfo = AppUtils.robolectricTestInfo("onDestroy");
        super.onDestroy();
    }
}
