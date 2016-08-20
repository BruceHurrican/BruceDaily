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

package com.brucedaily.database.bean;

import com.bruceutils.utils.LogUtils;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.PropertyType;
import org.greenrobot.greendao.generator.Schema;

import java.io.IOException;

/**
 * bean 生成器
 * Created by BruceHurrican on 2016/8/7.
 */
public class BeanGenerator {

    /* 对应生成的类名 */
    public static final String COST_ENTITY_NAME = "CostMonth";
    /* 数据库表名 */
    public static final String COST_TABLE_NAME = "t_CostMonth";
    /* 字段名称 消费日期, 按当前日期所在月顺序记录(1-31) */
    public static final String COST_DAY = "costDay";
    /* 字段名称 消费项目标题 */
    public static final String COST_TITLE = "costTitle";
    /* 字段名称 消费项目详情 */
    public static final String COST_DETAIL = "costDetail";
    /* 字段名称 消费金额 */
    public static final String COST_PRICE = "costPrice";
    /* 字段名称 修改日期 */
    public static final String COST_MODIFY_DATE = "costModifyDate";
    /* 生成对象所在包名 */
    public static final String PACKAGE_NAME = "com.brucedaily.database.bean";
    /* 生成 dao */
    public static final String DB_DAO = "com.brucedaily.database.dao";
    /* 生成对象所在包路径 */
    public static final String PACKAGE_DIR = "./app/src/main/java";

    public static void generateTble() {
        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            Schema schema = new Schema(1, PACKAGE_NAME);
            Entity month = schema.addEntity(COST_ENTITY_NAME);
            month.setTableName(COST_TABLE_NAME);
//            month.addIdProperty().primaryKey().autoincrement().notNull().unique();
            // addIdProperty 会自动调用 primaryKey, 此时不能调用 notNull 否则会造成insert数据失败
            month.addIdProperty().autoincrement().unique();
            month.addIntProperty(COST_DAY).columnName(COST_DAY).columnType(PropertyType.Int.toString()).notNull();
            month.addStringProperty(COST_TITLE).columnName(COST_TITLE).columnType(PropertyType.String.toString()).notNull();
            month.addStringProperty(COST_DETAIL).columnName(COST_DETAIL).columnType(PropertyType.String.toString());
            month.addStringProperty(COST_PRICE).columnName(COST_PRICE).columnType(PropertyType.String.toString()).notNull();
            month.addDateProperty(COST_MODIFY_DATE).columnName(COST_MODIFY_DATE).columnType(PropertyType.Date.toString()).notNull();
            schema.setDefaultJavaPackageDao(DB_DAO);
            daoGenerator.generateAll(schema, PACKAGE_DIR);
        } catch (IOException e) {
            LogUtils.e(e.toString());
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    public static void main(String[] args) {
        generateTble();
    }
}
