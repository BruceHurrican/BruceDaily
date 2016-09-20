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

package com.brucedaily;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.brucedaily.month.MonthDailyActivity;
import com.bruceutils.utils.LogUtils;
import com.bruceutils.utils.logdetails.LogDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * Created by BruceHurrican on 2016/8/20.
 */
public class AppUtils {
    private AppUtils() {

    }

    /**
     * 按照金额格式化
     * e.g. 54333.4565f -> "54,333.46"
     *
     * @param price
     * @return
     */
    public static String float2StringPrice(float price) {
        LogDetails.i("安卓单元测试");
        LogUtils.i("安卓单元测试2");
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setMaximumFractionDigits(2);
        return format.format(price);
    }

    /**
     * 将字符串形式的价格格式化
     * e.g. "54333.4565f" -> "54,333.46"
     *
     * @param price
     * @return
     */
    public static String str2strPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            LogUtils.e("输入数据格式错误 price-" + price);
            return "-1";
        }
        float fPrice = Float.valueOf(price);
        return float2StringPrice(fPrice);
    }

    /**
     * 校验金额是否合法
     *
     * @param price
     * @return true 合法
     */
    public static boolean isPriceValid(String price) {
        if (TextUtils.isEmpty(price)) {
            LogUtils.i("price ->" + price);
            return false;
        }
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");//判断小数点后一位的数字的正则表达式
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    public static void operatorDBfile(Context context, String command) throws IOException {
        File dbFile = context.getDatabasePath(MonthDailyActivity.DB_NAME);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "BruceDailyDBfile");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        if (command.equals(MonthDailyActivity.DB_BACKUP)) {
            LogDetails.i("执行备份数据库命令");
            backup.createNewFile();
            copyFile(dbFile, backup);
            LogDetails.i("成功备份数据库");
        } else if (command.equals(MonthDailyActivity.DB_RESTORE)) {
            LogDetails.i("执行恢复数据库命令");
            copyFile(backup, dbFile);
            LogDetails.i("成功恢复数据库");
        } else {
            LogDetails.e("错误的命令 " + command);
        }
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    private static void copyFile(File sourceFile, File targetFile) {
        FileChannel inChannel = null, outChannel = null;
        try {
            inChannel = new FileInputStream(sourceFile).getChannel();
            outChannel = new FileOutputStream(targetFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inChannel) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outChannel) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
