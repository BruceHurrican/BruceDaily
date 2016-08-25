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

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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
import com.brucedaily.database.dao.CostMonthDao;
import com.brucedaily.database.dao.DaoMaster;
import com.brucedaily.database.dao.DaoSession;
import com.bruceutils.base.BaseFragmentActivity;
import com.bruceutils.utils.LogUtils;
import com.bruceutils.utils.ProgressDialogUtils;
import com.bruceutils.utils.logdetails.LogDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 按月统计消费金额
 * Created by BruceHurrican on 2016/7/24.
 */
public class MonthDailyActivity extends BaseFragmentActivity {
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
    // 数据库相关
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase writableDatabase;
    private CostMonthDao costMonthDao;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    private int position; // 待修改数据位置
    private FragmentManager fragmentManager;
    private long exitFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_activity_daily);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initUIHandler();
        ProgressDialogUtils.initProgressBar(MonthDailyActivity.this, "操作进行中...", R.mipmap.ic_app);
        ProgressDialogUtils.showProgressDialog();
        costAdapter = new CostAdapter();
        initDatabase();
        initData();
        ProgressDialogUtils.cancelProgressDialog();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContainer.setLayoutManager(manager);

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

        monthCount();

        fragmentManager = getSupportFragmentManager();
    }

    private void initData() {
        // 查询结果集按日期降序排序，如果日期相同则按id降序排序
        dataList = costMonthDao.queryBuilder().orderDesc(CostMonthDao.Properties.CostDay).orderDesc(CostMonthDao.Properties.CostModifyDate).list();
        LogDetails.i("dataList 是否为空? " + (null == dataList));
        if (dataList == null) {
            dataList = new ArrayList<>(1);
        }
        costAdapter.setDataList(dataList);
    }

    private void initDatabase() {
        devOpenHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        writableDatabase = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(writableDatabase);
        daoSession = daoMaster.newSession();
        costMonthDao = daoSession.getCostMonthDao();
    }

    /**
     * mmbw qqwts
     * 月消费统计
     */
    private void monthCount() {
        float total = 4500;
        List<CostMonth> tmpList = null;
        // 上旬花费
        float monthEarly = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.between(1, 10)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthEarly += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月上旬花费共计 %s 元", AppUtils.float2StringPrice(monthEarly)));
        tvCountEarly.setText(String.format("上旬消费统计 %s 元", AppUtils.float2StringPrice(monthEarly)));

        // 中旬花费
        float monthMiddle = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.between(11, 20)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthMiddle += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月中旬花费共计 %s 元", AppUtils.float2StringPrice(monthMiddle)));
        tvCountMiddle.setText(String.format("中旬消费统计 %s 元", AppUtils.float2StringPrice(monthMiddle)));

        // 下旬花费
        float monthLast = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.ge(21)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthLast += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月下旬花费共计 %s 元", AppUtils.float2StringPrice(monthLast)));
        tvCountLast.setText(String.format("下旬消费统计 %s 元", AppUtils.float2StringPrice(monthLast)));

        float monthRemain = total - monthEarly - monthMiddle - monthLast;
        tvRemain.setText(String.format("月预算余额: %s 元", AppUtils.float2StringPrice(monthRemain)));

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
                deleteItem(dataList.get(position));
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
        monthCount();
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

        MonthAddModifyFragment monthAddModifyFragment = new MonthAddModifyFragment();
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
        monthAddModifyFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(MonthAddModifyFragment.class.getSimpleName());
        transaction.replace(android.R.id.content, monthAddModifyFragment).commit();
    }

    /**
     * 修改消费记录
     *
     * @param position 修改消费项索引
     * @param title    消费标题{@link CostMonth#costTitle}
     * @param content  消费详情{@link CostMonth#costDetail}
     * @param tmpTime  消费时间{@link CostMonth#costDay}
     * @param price    消费金额{@link CostMonth#costPrice}
     * @return true 修改记录成功
     */
    private boolean modifyCostRecord(int position, String title, String content, String tmpTime, String price) {
        CostMonth costMonth = dataList.get(position);
        if (!AppUtils.isPriceValid(price)) {
            LogUtils.e("输入的价格不符合格式规范");
            showToastShort("输入的价格不符合格式规范");
            return false;
        }
        int time = 0;
        if (!TextUtils.isEmpty(title)) {
            costMonth.costTitle = title;
        }
        if (!TextUtils.isEmpty(content)) {
            costMonth.costDetail = content;
        }
        if (!TextUtils.isEmpty(tmpTime)) {
            time = Integer.valueOf(tmpTime);
        } else {
            time = costMonth.costDay;
        }
        if (!TextUtils.isEmpty(price)) {
            costMonth.costPrice = price;
        }
        if (time >= 32) {
            LogDetails.w("输入非法数据");
            showToastShort("日期不能大于31的数字");
            return false;
        }

        LogDetails.i("time-%s", time);
        if (time == dataList.get(position).costDay) {
            dataList.set(position, costMonth);
            updateItem(costMonth);
        } else {
            costMonth.costDay = time;
            dataList.remove(position);
            CostMonth tmpCostMonth = costMonth;
            dataList.add(tmpCostMonth);
            addItem(tmpCostMonth);
        }
        LogDetails.i("修改成功");
        showToastShort("修改成功");
        return true;
    }

    /**
     * 增加消费记录
     *
     * @param title   消费标题{@link CostMonth#costTitle}
     * @param content 消费详情{@link CostMonth#costDetail}
     * @param tmpTime 消费时间{@link CostMonth#costDay}
     * @param price   消费金额{@link CostMonth#costPrice}
     * @return true 增加记录成功
     */
    private boolean addCostRecord(String title, String content, String tmpTime, String price) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(tmpTime) || TextUtils.isEmpty(price)) {
            LogDetails.i("必填项不能为空");
            showToastShort("必填项不能为空");
            return false;
        }
        if (!AppUtils.isPriceValid(price)) {
            LogUtils.e("输入的价格不符合格式规范");
            showToastShort("输入的价格不符合格式规范");
            return false;
        }
        int time = Integer.valueOf(tmpTime);
        LogDetails.i("time-" + time);
        if (time >= 32) {
            LogDetails.w("输入非法数据");
            showToastShort("日期不能大于31的数字");
            return false;
        }
        CostMonth costMonth = new CostMonth();
        costMonth.costDay = time;
        costMonth.costTitle = title;
        costMonth.costDetail = content;
        costMonth.costPrice = price;

        if (dataList.size() == 0) {
            dataList.add(costMonth);
        } else {
            dataList.add(costMonth);
        }
        addItem(costMonth);
        LogDetails.i("增加数据成功");
        showToastShort("增加数据成功");
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDataFromFragment(MsgBean msgBean) {
        LogUtils.i("position-> " + position);
        LogDetails.i(msgBean);
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        if (im.isActive() && getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (null == msgBean) {
            return;
        }
        if (msgBean.isAdd) {
            if (!addCostRecord(msgBean.title, msgBean.content, msgBean.time, msgBean.price)) {
                LogDetails.i("增加记录失败");
                return;
            }
        } else {
            if (!modifyCostRecord(position, msgBean.title, msgBean.content, msgBean.time, msgBean.price)) {
                LogDetails.i("修改消费记录失败");
                return;
            }
        }
        refreshData();
    }

    /**
     * 操作数据库
     *
     * @param command 备份 {@link #DB_BACKUP}, 支持 {@link #DB_RESTORE}
     */
    private void operatorDB(final String command) {
        ProgressDialogUtils.showProgressDialog();
        ExecutorService back = Executors.newCachedThreadPool();
        back.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AppUtils.operatorDBfile(MonthDailyActivity.this, command);
                    if (command.equals(DB_RESTORE)) {
                        initData();
                        getUIHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                refreshData();
                                showToastShort("恢复数据成功");
                            }
                        });
                    } else {
                        showToastShort("备份数据成功");
                    }
                } catch (IOException e) {
                    showToastShort("操作失败");
                    e.printStackTrace();
                } finally {
                    getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialogUtils.cancelProgressDialog();
                        }
                    }, 1000);
                }
            }
        });
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
                deleteAllData();
                refreshData();
                break;
            case R.id.btn_list:
                LinearLayoutManager manager = new LinearLayoutManager(MonthDailyActivity.this, LinearLayoutManager.VERTICAL, false);
                rvContainer.setLayoutManager(manager);
                break;
            case R.id.btn_grid:
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvContainer.setLayoutManager(staggeredGridLayoutManager);
                break;
            case R.id.btn_db_back:
                LogDetails.i("备份数据库");
                operatorDB(DB_BACKUP);
                break;
            case R.id.btn_db_restore:
                LogDetails.i("恢复数据库");
                operatorDB(DB_RESTORE);
                break;
        }
    }

    private void addItem(CostMonth costMonth) {
        costMonth.costModifyDate = new Date(System.currentTimeMillis());
        costMonthDao.insertOrReplace(costMonth);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        LogDetails.i(costMonthDao.count() + "\n" + costMonthDao.queryBuilder().list() + "\n" + costMonthDao.getKey(costMonth) + "\n" + costMonthDao.getPkProperty().columnName);
    }

    private void updateItem(CostMonth costMonth) {
        costMonth.costModifyDate = new Date(System.currentTimeMillis());
        costMonthDao.update(costMonth);
    }

    private void deleteItem(CostMonth costMonth) {
        costMonthDao.delete(costMonth);
    }

    private void deleteAllData() {
        costMonthDao.deleteAll();
    }

    @Override
    public void onBackPressed() {
//        exitFlag = System.currentTimeMillis();
        // 将入栈的 fragment 按 FILO 规则依次出栈
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.popBackStackImmediate(null, 0)) {
            LogUtils.d("fragment栈中最上层的 fragment 出栈");
            if (fragmentManager.getBackStackEntryCount() == 0) {
                LogUtils.i("fragment 栈已经清空");
                if (Math.abs(exitFlag - System.currentTimeMillis()) < 2000 && exitFlag > 0) {
                    super.onBackPressed();
                } else {
                    showToastShort("再按一次退出应用...");
                    exitFlag = System.currentTimeMillis();
                }
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
        daoSession = null;
        devOpenHelper.close();
        devOpenHelper = null;
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        recycleUIHandler();
        super.onDestroy();
    }
}
