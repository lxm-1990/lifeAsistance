package com.lxm.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.utils.SharePref;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Switch sw_speak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = (Switch) findViewById(R.id.switch_speak);
        sw_speak.setChecked(SharePref.getBoolean(this,"isSpeak",false));
        sw_speak.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_speak:
                SharePref.setBoolean(this,"isSpeak",sw_speak.isChecked());
                break;
        }
    }
}
