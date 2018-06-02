///*
// * BruceHurrican
// *  Copyright (c) 2016.
// *     Licensed under the Apache License, Version 2.0 (the "License");
// *     you may not use this file except in compliance with the License.
// *     You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// *     Unless required by applicable law or agreed to in writing, software
// *     distributed under the License is distributed on an "AS IS" BASIS,
// *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *     See the License for the specific language governing permissions and
// *     limitations under the License.
// *
// *     This document is Bruce's individual learning the android demo, wherein the use of the code from the Internet, only to use as a learning exchanges.
// *     And where any person can download and use, but not for commercial purposes.
// *     Author does not assume the resulting corresponding disputes.
// *     If you have good suggestions for the code, you can contact BurrceHurrican@foxmail.com
// *     本文件为Bruce's个人学习android的作品, 其中所用到的代码来源于互联网，仅作为学习交流使用。
// *     任和何人可以下载并使用, 但是不能用于商业用途。
// *     作者不承担由此带来的相应纠纷。
// *     如果对本代码有好的建议，可以联系BurrceHurrican@foxmail.com
// */
//
//package com.brucedaily;
//
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//
//import com.bruceutils.utils.LogUtils;
//import com.github.moduth.blockcanary.BlockCanaryContext;
//
///**
// * Created by BruceHurrican on 2016/3/1.
// */
//public class BDBlockCanaryContext extends BlockCanaryContext {
//    String qualifier = "DemoBlock";
//
//    @Override
//    public String getQualifier() {
//        try {
//            PackageInfo info = DailyApplication.getAppContext().getPackageManager().getPackageInfo(DailyApplication.getAppContext().getPackageName(), 0);
//            qualifier += info.versionCode + "_" + info.versionName + "_Bruce";
//        } catch (PackageManager.NameNotFoundException e) {
//            LogUtils.e(e.toString());
//        }
//        return qualifier;
//    }
//
//    @Override
//    public String getUid() {
//        return Build.SERIAL;
//    }
//
//    @Override
//    public String getNetworkType() {
//        return "4G";
//    }
//
//    @Override
//    public int getConfigDuration() {
//        return 9999;
//    }
//
//    @Override
//    public int getConfigBlockThreshold() {
//        return 500;
//    }
//
//    @Override
//    public boolean isNeedDisplay() {
//        return BuildConfig.DEBUG;
//    }
//}
