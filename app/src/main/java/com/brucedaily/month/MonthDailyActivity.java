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
    @Bind(R.id.tv_count1)
    TextView tvCount1;
    @Bind(R.id.tv_count2)
    TextView tvCount2;
    @Bind(R.id.tv_count3)
    TextView tvCount3;
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

    private List<CostBean> dataList = new ArrayList<>(31);
    private CostAdapter costAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_activity_daily);
        ButterKnife.bind(this);

        // TODO: 2016/7/25 从数据库中获取数据
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
                LogDetails.i("当前选中的数据: " + dataList.get(position).title);
                showDetails(position);
            }
        });
        costAdapter.setItemLongClickListener(new CostAdapter.CardViewItemLongClickListener() {
            @Override
            public void itemLongClick(View view, int position) {
                LogDetails.i("当前选中的数据: " + dataList.get(position).price);
                showOperator(position);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(new CostBean(i + 1, "中餐" + i, "中餐详情" + i, (3.66f + i)));
        }
    }

    private void showDetails(int position) {
        View view = getLayoutInflater().inflate(R.layout.month_item_detail, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tvTitle.setText(dataList.get(position).title);
        tvDetail.setText(dataList.get(position).detail);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击可以消失
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rlRoot, Gravity.CENTER, 0, 0);
    }

    private void showOperator(final int position) {
        View view = getLayoutInflater().inflate(R.layout.month_item_detail, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tvTitle.setText("修改");
        tvDetail.setText("删除");
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showAddModifyPop(position, false);
            }
        });
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                costAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
                LogDetails.i("删除一条数据");
                showToastShort("删除一条数据");
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击可以消失
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rlRoot, Gravity.CENTER, 0, 0);
    }

    private void showAddModifyPop(final int position, final boolean isAdd) {
        LogDetails.i("position-%s,isAdd-%s", position, isAdd);
        View view = getLayoutInflater().inflate(R.layout.month_item_add_modify, null);
        final EditText etTitle = (EditText) view.findViewById(R.id.et_title);
        final EditText etContent = (EditText) view.findViewById(R.id.et_content);
        final EditText etTime = (EditText) view.findViewById(R.id.et_time);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_price);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        if (!isAdd) {
            etTitle.setHint(dataList.get(position).title);
            etContent.setHint(dataList.get(position).detail);
            etTime.setHint(dataList.get(position).date + "");
            etPrice.setHint(dataList.get(position).price + "");
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
                String tmpPrice = etPrice.getEditableText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(tmpTime) || TextUtils.isEmpty(tmpPrice)) {
                    LogDetails.w("输入数据不能为空");
                    showToastShort("不能输入空数据");
                    return;
                }
                int time = Integer.valueOf(tmpTime);
                if (time > 32) {
                    LogDetails.w("输入非法数据");
                    showToastShort("日期不能大于31的数字");
                    return;
                }
                float price = Float.valueOf(tmpPrice);
                CostBean costBean = null;
                if (isAdd) {
                    costBean = new CostBean(time, title, content, price);
                    if (dataList.size() == 0) {
                        dataList.add(costBean);
                    } else {
                        int index = -1;
                        for (int i = 0; i < dataList.size(); i++) {
                            if (time == dataList.get(i).date) {
                                index = i;
                            }
                        }
                        if (index != -1) {
                            dataList.add(index, costBean);
                        } else {
                            if (time > dataList.get(dataList.size() - 1).date) {
                                dataList.add(costBean);
                            } else if (time < dataList.get(dataList.size() - 1).date) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    if (time < dataList.get(i).date) {
                                        index = i;
                                        break;
                                    }
                                }
                                dataList.add(index, costBean);
                            }
                        }
                    }
                } else {
                    costBean = dataList.get(position);
                    LogDetails.i("time-%s", time);
                    if (time == dataList.get(position).date) {
                        costBean.title = title;
                        costBean.detail = content;
                        costBean.price = price;
                        dataList.set(position, costBean);
                    } else {
                        dataList.remove(position);
                        costBean = new CostBean(time, title, content, price);
                        int index = -1;
                        for (int i = 0; i < dataList.size(); i++) {
                            if (time == dataList.get(i).date) {
                                index = i;
                            }
                        }
                        costBean.date = time;
                        if (index != -1) {
                            dataList.add(index, costBean);
                        } else {
                            if (dataList.size() == 0) {
                                dataList.add(costBean);
                            } else if (dataList.size() > 0 && time > dataList.get(dataList.size() - 1).date) {
                                dataList.add(costBean);
                            } else if (dataList.size() > 0 && time < dataList.get(dataList.size() - 1).date) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    if (time < dataList.get(i).date) {
                                        index = i;
                                        break;
                                    }
                                }
                                dataList.add(index + 1, costBean);
                            }
                        }
                    }
                }

                popupWindow.dismiss();
                costAdapter.notifyDataSetChanged();
                if (isAdd) {
                    LogDetails.i("增加成功");
                    showToastShort("增加成功");
                } else {
                    LogDetails.i("修改成功");
                    showToastShort("修改成功");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.btn_add, R.id.btn_clear, R.id.btn_list, R.id.btn_grid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
//                showAddModifyPop(-1, true);
                accessGreenDao();
                break;
            case R.id.btn_clear:
                dataList.clear();
                LogDetails.i("size-" + dataList.size());
                costAdapter.notifyDataSetChanged();
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

    // TODO: 2016/8/7 入口需要调整
    private void accessGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "md_db", null);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        DaoSession daoSession = daoMaster.newSession();
        CostMonthDao costMonthDao = daoSession.getCostMonthDao();
        CostMonth costMonth = new CostMonth();
        costMonth.setCostDay(23);
//                costMonth.setId(10);
        costMonth.setCostTitle("aatitle");
        costMonth.setCostDetail("aadetail");
        costMonth.setCostPrice("88.26");
//        costMonth = new CostMonth(0,23,"aatitle","aadetail","88.32");
        costMonthDao.insertOrReplace(costMonth);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        LogDetails.i(costMonthDao.count() + "\n" + costMonthDao.queryBuilder().list() + "\n" + costMonthDao.getKey(costMonth) + "\n" + costMonthDao.getPkProperty().columnName);
        LogDetails.i(costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.eq(23)).orderAsc(CostMonthDao.Properties.Id).list());
        daoSession = null;
        helper.close();
        helper = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
