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


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.brucedaily.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MonthDailyActivityTestUI {

    @Rule
    public ActivityTestRule<MonthDailyActivity> mActivityTestRule = new ActivityTestRule<>(MonthDailyActivity.class);

    @Test
    public void monthDailyActivityTestUI() {
        ViewInteraction button = onView(allOf(withId(R.id.btn_add), withText("增加记录"), withParent(allOf(withId(R.id.rl_root), withParent(withId(android.R.id.content)))), isDisplayed()));
//        button.perform(click());

        ViewInteraction tv = onView(allOf(withId(R.id.tv_date)));
        tv.check(ViewAssertions.doesNotExist());
        ViewInteraction textView = onView(allOf(withText("中餐"), isDisplayed()));
//        textView.perform(click());
//
//        ViewInteraction textView2 = onView(allOf(withText("招行信用卡支出"), isDisplayed()));
//        textView2.perform(click());
//
//        ViewInteraction multiAutoCompleteTextView = onView(allOf(withId(R.id.mactw_content), isDisplayed()));
//        multiAutoCompleteTextView.perform(replaceText("招行信用卡支出, "), closeSoftKeyboard());
//
//        ViewInteraction textView3 = onView(allOf(withText("4"), isDisplayed()));
//        textView3.perform(click());
//
//        ViewInteraction editText = onView(allOf(withId(R.id.et_price), withParent(withId(R.id.ll_content)), isDisplayed()));
//        editText.perform(replaceText("43.890"), closeSoftKeyboard());
//
//        ViewInteraction button2 = onView(allOf(withId(R.id.btn_ok), withText("确定"), isDisplayed()));
//        button2.perform(click());
//
//        ViewInteraction editText2 = onView(allOf(withId(R.id.et_price), withText("43.890"), withParent(withId(R.id.ll_content)), isDisplayed()));
//        editText2.perform(click());
//
//        ViewInteraction editText3 = onView(allOf(withId(R.id.et_price), withText("43.890"), withParent(withId(R.id.ll_content)), isDisplayed()));
//        editText3.perform(replaceText("43.89"), closeSoftKeyboard());
//
//        ViewInteraction button3 = onView(allOf(withId(R.id.btn_ok), withText("确定"), isDisplayed()));
//        button3.perform(click());
//
//        ViewInteraction button4 = onView(allOf(withId(R.id.btn_grid), withText("网格"), withParent(allOf(withId(R.id.tv_rv_type), withParent(withId(R.id.rl_root)))), isDisplayed()));
//        button4.perform(click());

//        ViewInteraction button5 = onView(allOf(withId(R.id.btn_list), withText("列表"), withParent(allOf(withId(R.id.tv_rv_type), withParent(withId(R.id.rl_root)))), isDisplayed()));
//        button5.perform(click());

    }

}
