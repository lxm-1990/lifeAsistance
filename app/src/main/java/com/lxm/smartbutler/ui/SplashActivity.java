package com.lxm.smartbutler.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lxm.smartbutler.MainActivity;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.utils.SharePref;
import com.lxm.smartbutler.utils.StaticClass;
import com.lxm.smartbutler.utils.UtilTools;


public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.SPLASH_ACTIVITY:{
                    if (isFirst()){
                        Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    break;
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        mHandler.sendMessageDelayed(mHandler.obtainMessage(StaticClass.SPLASH_ACTIVITY),2000);
    }

    private void initView() {
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        UtilTools.setFont(this,tv_splash);
    }

    private boolean isFirst() {
        boolean isFirst = SharePref.getBoolean(this,StaticClass.IS_FIRST,true);
        if (isFirst) {
            SharePref.setBoolean(this,StaticClass.IS_FIRST,false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
