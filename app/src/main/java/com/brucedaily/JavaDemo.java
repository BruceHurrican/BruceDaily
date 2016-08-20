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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BruceHurrican on 2016/8/20.
 */
public class JavaDemo {
    public static void main(String[] args) {
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
}
