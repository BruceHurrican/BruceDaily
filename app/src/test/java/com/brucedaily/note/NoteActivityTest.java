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

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brucedaily.BuildConfig;
import com.brucedaily.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;
import org.robolectric.util.FragmentController;
import org.robolectric.util.FragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 用 robolectric 单元测试 NoteActivity
 * Created by BruceHurrican on 16/10/27.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NoteActivityTest {
    private TextView tvRemain, tvCountEarly, tvCountMiddle, tvCountLast;
    private Button btn_add, btn_clear;
    private ActivityController<NoteActivity> controller;
    private NoteActivity activity;

    @Before
    public void setUp() throws Exception {
        controller = Robolectric.buildActivity(NoteActivity.class).create().start();
        activity = controller.get();
    }

    @Test
    public void testActivityLifeCycle() throws Exception {
        assertNotNull(activity);
        assertEquals("NoteActivity", activity.getClass().getSimpleName());
        assertEquals("onCreate", activity.testTagInfo);
        controller.destroy();
        assertEquals("onDestroy", activity.testTagInfo);
    }

    @Test
    public void testWidgets() throws Exception {
        btn_add = (Button) activity.findViewById(R.id.btn_add);
        btn_clear = (Button) activity.findViewById(R.id.btn_clear);
        assertTrue(btn_add.isClickable());
        assertTrue(btn_clear.isClickable());
        btn_add.performClick();
        assertEquals("operateCostRecord executed", activity.testTagInfo);
    }

    @Test
    public void testMonthAddModifyFragment() throws Exception {
        NoteAddModifyFragment fragment = new NoteAddModifyFragment();
        FragmentController<NoteAddModifyFragment> fragmentController = FragmentController.of(fragment);
        FragmentTestUtil.startFragment(fragment);
//        assertEquals("onCreateView", fragment.testInfo);
        assertEquals("onViewCreated", fragment.testInfo);
        assertNotNull(fragment.getView());
        Button btnOk = (Button) fragment.getView().findViewById(R.id.btn_ok);
        assertNotNull(btnOk);
        btnOk.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), "必填项不能为空");
        AutoCompleteTextView actwTitle = (AutoCompleteTextView) fragment.getView().findViewById(R.id.actw_title);
        AutoCompleteTextView actwTime = (AutoCompleteTextView) fragment.getView().findViewById(R.id.actw_time);
        EditText etPrice = (EditText) fragment.getView().findViewById(R.id.et_price);
        actwTitle.setText("ICBC");
        actwTime.setText("22");
        etPrice.setText("34.554");
        btnOk.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), "输入的价格不符合格式规范");
    }
}