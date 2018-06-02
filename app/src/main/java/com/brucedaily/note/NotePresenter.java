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

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.brucedaily.AppUtils;
import com.brucedaily.R;
import com.brucedaily.SharedPreferencesUtil;
import com.brucedaily.database.bean.CostMonth;
import com.brucedaily.database.dao.CostMonthDao;
import com.brucedaily.database.dao.DaoMaster;
import com.brucedaily.database.dao.DaoSession;
import com.brucedaily.month.CostAdapter;
import com.bruceutils.base.BaseActivity;
import com.bruceutils.utils.LogUtils;
import com.bruceutils.utils.ProgressDialogUtils;
import com.bruceutils.utils.logdetails.LogDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BruceHurrican on 16/10/26.
 */

public class NotePresenter {
    private BaseActivity mActivity;
    private List<CostMonth> mDataList = new ArrayList<>(31);
    private INoteActivityView iNoteActivityView;
    // 数据库相关
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase writableDatabase;
    private CostMonthDao costMonthDao;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    public NotePresenter(BaseActivity mActivity, List<CostMonth> dataList, INoteActivityView iNoteActivityView) {
        this.mActivity = mActivity;
        this.mDataList = dataList;
        this.iNoteActivityView = iNoteActivityView;
    }

    public List<CostMonth> initData(CostAdapter costAdapter) {
        LogDetails.i("mDataList 是否为空? " + (null == mDataList));
        // 查询结果集按日期降序排序，如果日期相同则按id降序排序
        mDataList = costMonthDao.queryBuilder().orderDesc(CostMonthDao.Properties.CostDay).orderDesc(CostMonthDao.Properties.CostModifyDate).list();
        if (mDataList == null) {
            mDataList = new ArrayList<>(1);
        }
        costAdapter.setDataList(mDataList);
        return mDataList;
    }

    public void initDatabase() {
        devOpenHelper = new DaoMaster.DevOpenHelper(mActivity, NoteActivity.DB_NAME, null);
        writableDatabase = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(writableDatabase);
        daoSession = daoMaster.newSession();
        costMonthDao = daoSession.getCostMonthDao();
    }

    /**
     * 月消费统计
     */
    public void monthCount() {
        float total = SharedPreferencesUtil.getInt(mActivity, NoteActivity.LOCAL_DATA_KEY_TOTAL_BUDGET, NoteActivity.TOTAL_BUDGET);
        List<CostMonth> tmpList;
        // 上旬花费
        float monthEarly = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.between(1, 10)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthEarly += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月上旬花费共计 %s 元", AppUtils.float2StringPrice(monthEarly)));
//        tvCountEarly.setText(String.format("上旬消费统计 %s 元", AppUtils.float2StringPrice(monthEarly)));
        iNoteActivityView.countMonth1(String.format("上旬消费统计 %s 元", AppUtils.float2StringPrice(monthEarly)));

        // 中旬花费
        float monthMiddle = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.between(11, 20)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthMiddle += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月中旬花费共计 %s 元", AppUtils.float2StringPrice(monthMiddle)));
//        tvCountMiddle.setText(String.format("中旬消费统计 %s 元", AppUtils.float2StringPrice(monthMiddle)));
        iNoteActivityView.countMonth2(String.format("中旬消费统计 %s 元", AppUtils.float2StringPrice(monthMiddle)));

        // 下旬花费
        float monthLast = 0;
        tmpList = costMonthDao.queryBuilder().where(CostMonthDao.Properties.CostDay.ge(21)).list();
        LogDetails.i(tmpList);
        for (int i = 0; i < tmpList.size(); i++) {
            monthLast += Float.valueOf(tmpList.get(i).costPrice);
        }
        LogDetails.i(String.format("当月下旬花费共计 %s 元", AppUtils.float2StringPrice(monthLast)));
//        tvCountLast.setText(String.format("下旬消费统计 %s 元", AppUtils.float2StringPrice(monthLast)));
        iNoteActivityView.countMonth3(String.format("下旬消费统计 %s 元", AppUtils.float2StringPrice(monthLast)));

        float monthRemain = total - monthEarly - monthMiddle - monthLast;
//        tvRemain.setText(String.format("月预算余额: %s 元", AppUtils.float2StringPrice(monthRemain)));
        iNoteActivityView.countRemain(String.format("月预算余额: %s 元", AppUtils.float2StringPrice(monthRemain)));
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
    public boolean modifyCostRecord(int position, String title, String content, String tmpTime, String price) {
        CostMonth costMonth = mDataList.get(position);
        if (!AppUtils.isPriceValid(price)) {
            LogUtils.e(mActivity.getString(R.string.md_price_invalid));
            mActivity.showToastShort(mActivity.getString(R.string.md_price_invalid));
            return false;
        }
        int time;
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
            mActivity.showToastShort("日期不能大于31的数字");
            return false;
        }

        LogDetails.i("time-%s", time);
        if (time == mDataList.get(position).costDay) {
            mDataList.set(position, costMonth);
            updateItem(costMonth);
        } else {
            costMonth.costDay = time;
            mDataList.remove(position);
            CostMonth tmpCostMonth = costMonth;
            mDataList.add(tmpCostMonth);
            addItem(tmpCostMonth);
        }
        LogDetails.i("修改成功");
        mActivity.showToastShort("修改成功");
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
    public boolean addCostRecord(String title, String content, String tmpTime, String price) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(tmpTime) || TextUtils.isEmpty(price)) {
            LogDetails.i("必填项不能为空");
            mActivity.showToastShort("必填项不能为空");
            return false;
        }
        if (!AppUtils.isPriceValid(price)) {
            LogUtils.e(mActivity.getString(R.string.md_price_invalid));
            mActivity.showToastShort(mActivity.getString(R.string.md_price_invalid));
            return false;
        }
        int time = Integer.valueOf(tmpTime);
        LogDetails.i("time-" + time);
        if (time >= 32) {
            LogDetails.w("输入非法数据");
            mActivity.showToastShort("日期不能大于31的数字");
            return false;
        }
        CostMonth costMonth = new CostMonth();
        costMonth.costDay = time;
        costMonth.costTitle = title;
        costMonth.costDetail = content;
        costMonth.costPrice = price;

        if (mDataList.size() == 0) {
            mDataList.add(costMonth);
        } else {
            mDataList.add(costMonth);
        }
        addItem(costMonth);
        LogDetails.i("增加数据成功");
        mActivity.showToastShort("增加数据成功");
        return true;
    }

    /**
     * 操作数据库
     *
     * @param command 备份 {@link NoteActivity#DB_BACKUP}, 支持 {@link NoteActivity#DB_RESTORE}
     */
    public void operatorDB(final String command) {
        ProgressDialogUtils.showProgressDialog();
        ExecutorService back = Executors.newCachedThreadPool();
        back.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AppUtils.operatorDBfile(mActivity, command);
                    if (command.equals(NoteActivity.DB_RESTORE)) {
                        EventBus.getDefault().post(new NoteMsg(NoteActivity.CODE_RESTORE_DB_SUCCESS, "恢复数据成功"));
                    } else {
                        mActivity.showToastShort("备份数据成功");
                    }
                } catch (IOException e) {
                    mActivity.showToastShort("操作失败");
                    e.printStackTrace();
                } finally {
                    mActivity.getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialogUtils.cancelProgressDialog();
                        }
                    }, 1000);
                }
            }
        });
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

    public void deleteItem(CostMonth costMonth) {
        costMonthDao.delete(costMonth);
    }

    public void deleteAllData() {
        costMonthDao.deleteAll();
    }

    public void clearReference() {
        daoSession = null;
        devOpenHelper.close();
        devOpenHelper = null;
    }
}
