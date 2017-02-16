package com.lxm.smartbutler.application;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lxm.smartbutler.service.SmsService;
import com.lxm.smartbutler.utils.SharePref;
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

        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=" + StaticClass.TTS_APPID);
        SDKInitializer.initialize(getApplicationContext());
        if (SharePref.getBoolean(this,"isSMS",false)) {
            //开启短信提醒服务
            Intent intent = new Intent(this, SmsService.class);
            startService(intent);
        }
    }
}
