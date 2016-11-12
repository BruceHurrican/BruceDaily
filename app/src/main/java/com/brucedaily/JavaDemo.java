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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BruceHurrican on 2016/8/20.
 */
public class JavaDemo {
    public static void main(String[] args) {
//        test01();
//        test02();
        test03();
    }

    private static void test03() {
        List<Constants> list = new ArrayList<>(5);
        list.add(new Constants());
        System.out.println(list.contains(Constants.class));
    }

    private static void test02() {
        System.out.println(checkIdCard("342931186620223344"));
        System.out.println(checkMobile("12510103698"));
        System.out.println(checkPhone("12510103698"));
        if (!checkMobile("12510103698") && !checkPhone("12510103698")) {
            System.out.println("非有效号码");
        }
    }

    private static void test01() {
        float aa = 65412.324f;
        NumberFormat.getInstance().setMaximumFractionDigits(2);
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setMaximumFractionDigits(2);
//        System.out.println(format.format(aa));
//        System.out.println(String.format("%.2f", aa));
        String ss = "55433.3452";
//        System.out.println(String.format("%2s",ss));

        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");//判断小数点后一位的数字的正则表达式
        String price = "545d33.2654";
        Matcher matcher = pattern.matcher(price);
//        System.out.println(matcher.matches());
        price = "43543.15";
//        System.out.println(pattern.matcher(price).matches());

        ArrayList<AA> list = new ArrayList<>();
        AA a1 = new AA("a1", 32, 543);
        AA a2 = new AA("a2", 32, 321);
        AA a3 = new AA("a3", 14, 54);
        AA a4 = new AA("a4", 56, 43);
        AA a5 = new AA("a5", 14, 23);
        AA a6 = new AA("a6", 43, 44);
        AA a7 = new AA("a7", 32, 543);
        AA a8 = new AA("a8", 2, 33);
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        list.add(a5);
        list.add(a6);
        list.add(a7);
        list.add(a8);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        Comparator<AA> comparator = new Comparator<AA>() {
            @Override
            public int compare(AA lhs, AA rhs) {
                if (lhs.age != rhs.age) {
                    return lhs.age - rhs.age;
                } else {
                    return lhs.money - rhs.money;
                }
            }
        };
        System.out.println("==================");
//        Collections.sort(list, comparator);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    private static class AA implements Comparable<AA> {
        String name;
        int age;
        int money;

        public AA(String name, int age, int money) {
            this.name = name;
            this.age = age;
            this.money = money;
        }

        @Override
        public String toString() {
            return "AA{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", money=" + money +
                    '}';
        }

        @Override
        public int compareTo(AA another) {
            if (this.age != another.age) {
                return this.age - another.age;
            } else {
                return this.money - another.money;
            }
        }
    }

    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }

    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,8}";
        return Pattern.matches(regex, phone);
    }
}
