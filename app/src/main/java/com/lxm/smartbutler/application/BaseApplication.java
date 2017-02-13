package com.lxm.smartbutler.application;

import android.app.Application;

import com.lxm.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 * Created by lxm on 17/2/7.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.APPID, true);
        Bmob.initialize(this, StaticClass.BMOB_APPID);
    }
}
