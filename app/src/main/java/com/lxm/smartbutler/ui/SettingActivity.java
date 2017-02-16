package com.lxm.smartbutler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.service.SmsService;
import com.lxm.smartbutler.utils.SharePref;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Switch sw_speak;
    private Switch sw_sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {

        sw_speak = (Switch) findViewById(R.id.switch_speak);
        sw_speak.setChecked(SharePref.getBoolean(this,"isSpeak",false));
        sw_speak.setSelected(SharePref.getBoolean(this,"isSpeak",false));
        sw_speak.setOnClickListener(this);

        sw_sms = (Switch) findViewById(R.id.switch_sms);
        sw_sms.setChecked(SharePref.getBoolean(this,"isSMS",false));
        sw_sms.setSelected(SharePref.getBoolean(this,"isSMS",false));
        sw_sms.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_speak:
                sw_speak.setSelected(!sw_speak.isSelected());
                SharePref.setBoolean(this,"isSpeak",sw_speak.isSelected());
                break;
            case R.id.switch_sms:
                sw_sms.setSelected(!sw_sms.isSelected());
                SharePref.setBoolean(this,"isSMS",sw_sms.isSelected());
                if (sw_sms.isSelected()) {
                    //开启短信提醒服务
                    Intent intent = new Intent(this, SmsService.class);
                    startService(intent);
                } else {
                    //关闭短信提醒服务
                    Intent intent = new Intent(this, SmsService.class);
                    stopService(intent);
                }
                break;
        }
    }
}
