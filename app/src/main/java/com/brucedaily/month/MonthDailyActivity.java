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

package com.brucedaily.month;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brucedaily.R;
import com.brucedaily.database.bean.CostMonth;
import com.brucedaily.database.dao.CostMonthDao;
import com.brucedaily.database.dao.DaoMaster;
import com.brucedaily.database.dao.DaoSession;
import com.bruceutils.base.BaseActivity;
import com.bruceutils.utils.logdetails.LogDetails;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 按月统计消费金额
 * Created by BruceHurrican on 2016/7/24.
 */
public class MonthDailyActivity extends BaseActivity {
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

    private List<CostMonth> dataList = new ArrayList<>(31);
    private CostAdapter costAdapter;
    // 数据库相关
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase writableDatabase;
    private CostMonthDao costMonthDao;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_activity_daily);
        ButterKnife.bind(this);
        initDatabase();
        initData();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContainer.setLayoutManager(manager);

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        rvContainer.setLayoutManager(staggeredGridLayoutManager);

        costAdapter = new CostAdapter(dataList);
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
    }

    private void initData() {
        // 查询结果集按日期升序排序，如果日期相同则按id降序排序
        dataList = costMonthDao.queryBuilder().orderAsc(CostMonthDao.Properties.CostDay).orderDesc(CostMonthDao.Properties.Id).list();
        LogDetails.i("dataList 是否为空? " + (null == dataList));
        if (dataList == null) {
            dataList = new ArrayList<>(1);
        }
    }

    private void initDatabase() {
        devOpenHelper = new DaoMaster.DevOpenHelper(this, "md_db", null);
        writableDatabase = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(writableDatabase);
        daoSession = daoMaster.newSession();
        costMonthDao = daoSession.getCostMonthDao();
    }

    /**
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
        LogDetails.i("当月上旬花费共计 %f 元", monthEarly);
        tvCountEarly.setText("上旬消费统计:" + monthEarly);

        // 中旬花费
        float monthMiddle = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.between(11, 20)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthMiddle += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i("当月中旬花费共计 %f 元", monthMiddle);
        tvCountMiddle.setText("中旬消费统计:" + monthMiddle);

        // 下旬花费
        float monthLast = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.ge(21)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthLast += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i("当月下旬花费共计 %f 元", monthLast);
        tvCountLast.setText("下旬消费统计:" + monthLast);

        float monthRemain = total - monthEarly - monthMiddle - monthLast;
        tvRemain.setText("月预算余额:" + monthRemain);

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
        costAdapter.notifyDataSetChanged();
        monthCount();
    }

    /**
     * 操作消费记录,当前只支持 “增加” 和 “删除” 操作
     *
     * @param position
     * @param isAdd
     */
    private void operateCostRecord(final int position, final boolean isAdd) {
        LogDetails.i("position-%s", position);
        View view = getLayoutInflater().inflate(R.layout.month_item_add_modify, null);
        final EditText etTitle = (EditText) view.findViewById(R.id.et_title);
        final EditText etContent = (EditText) view.findViewById(R.id.et_content);
        final EditText etTime = (EditText) view.findViewById(R.id.et_time);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_price);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        if (!isAdd) {
            etTitle.setHint(dataList.get(position).costTitle);
            etContent.setHint(dataList.get(position).costDetail);
            etTime.setHint(dataList.get(position).costDay + "");
            etPrice.setHint(dataList.get(position).costPrice);
        }

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rlRoot, Gravity.CENTER, 0, 0);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getEditableText().toString().trim();
                String content = etContent.getEditableText().toString().trim();
                String tmpTime = etTime.getEditableText().toString().trim();
                String price = etPrice.getEditableText().toString().trim();
                if (isAdd) {
                    if (!addCostRecord(title, content, tmpTime, price)) {
                        LogDetails.i("增加记录失败");
                        return;
                    }
                } else {
                    if (!modifyCostRecord(position, title, content, tmpTime, price)) {
                        LogDetails.i("修改消费记录失败");
                        return;
                    }
                }
                refreshData();
                popupWindow.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content) && TextUtils.isEmpty(tmpTime) && TextUtils.isEmpty(price)) {
            LogDetails.w("数据未做任何修改");
            showToastShort("亲,未修改任何信息喔~");
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
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (time == dataList.get(i).costDay) {
                    index = i;
                    break;
                }
            }
            LogDetails.i("index-" + index);
            tmpCostMonth.costDay = time;
            LogDetails.i("tmpCostMonth - %s", tmpCostMonth);
            if (index != -1) {
                dataList.add(index, tmpCostMonth);
            } else {
                if (dataList.size() == 0) {
                    dataList.add(tmpCostMonth);
                } else if (dataList.size() > 0 && time > dataList.get(dataList.size() - 1).costDay) {
                    dataList.add(tmpCostMonth);
                } else if (dataList.size() > 0 && time < dataList.get(dataList.size() - 1).costDay) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (time < dataList.get(i).costDay) {
                            index = i; // 获取相同日期的索引
                            break;
                        }
                    }
                    // 将数据插入到相同日期的最前端
                    dataList.add(index, tmpCostMonth);
                }
            }
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
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(tmpTime) && TextUtils.isEmpty(price)) {
            LogDetails.i("必填项不能为空");
            showToastShort("必填项不能为空");
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
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (time == dataList.get(i).costDay) {
                    index = i; // 获取相同日期的索引
                    break;
                }
            }
            LogDetails.i("index-" + index);
            if (index != -1) {
                // 将数据插入到相同日期的最前端
                dataList.add(index, costMonth);
            } else {
                if (time > dataList.get(dataList.size() - 1).costDay) {
                    // 新增日期大于表中日期
                    dataList.add(costMonth);
                } else if (time < dataList.get(dataList.size() - 1).costDay) {
                    // 新增日期小于表中日期
                    for (int i = 0; i < dataList.size(); i++) {
                        if (time < dataList.get(i).costDay) {
                            index = i; // 获取表中最小日期索引
                            break;
                        }
                    }
                    // 将数据插入到相同日期的最前端
                    dataList.add(index, costMonth);
                }
            }
        }
        addItem(costMonth);
        LogDetails.i("增加数据成功");
        showToastShort("增加数据成功");
        return true;
    }

    @OnClick({R.id.btn_add, R.id.btn_clear, R.id.btn_list, R.id.btn_grid})
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
        }
    }

    private void addItem(CostMonth costMonth) {
        costMonthDao.insertOrReplace(costMonth);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        LogDetails.i(costMonthDao.count() + "\n" + costMonthDao.queryBuilder().list() + "\n" + costMonthDao.getKey(costMonth) + "\n" + costMonthDao.getPkProperty().columnName);
        LogDetails.i(costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.eq(23)).orderAsc(CostMonthDao.Properties.Id).list());
    }

    private void updateItem(CostMonth costMonth) {
        costMonthDao.update(costMonth);
    }

    private void deleteItem(CostMonth costMonth) {
        costMonthDao.delete(costMonth);
    }

    private void deleteAllData() {
        costMonthDao.deleteAll();
    }

    @Override
    protected void onDestroy() {
        daoSession = null;
        devOpenHelper.close();
        devOpenHelper = null;
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
