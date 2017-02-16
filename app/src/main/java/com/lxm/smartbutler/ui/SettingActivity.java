package com.lxm.smartbutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.service.SmsService;
import com.lxm.smartbutler.utils.L;
import com.lxm.smartbutler.utils.SharePref;
import com.lxm.smartbutler.view.CustomDialog;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Switch sw_speak;
    private Switch sw_sms;
    private LinearLayout layout_check_verison;
    private TextView currentVersion;
    private String versionName = "";
    private int versionCode = 0;
    private LinearLayout layout_scan;
    private LinearLayout layout_create;
    private TextView tv_result;
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

        layout_check_verison = (LinearLayout) findViewById(R.id.check_version);
        layout_check_verison.setOnClickListener(this);
        currentVersion = (TextView) findViewById(R.id.currentVersion);

        //获得当前版本信息
        getPackageNameAndCode();
        currentVersion.setText("当前版本:" + versionName);

        layout_scan = (LinearLayout) findViewById(R.id.layout_scan);
        layout_create = (LinearLayout) findViewById(R.id.layout_create);
        layout_scan.setOnClickListener(this);
        layout_create.setOnClickListener(this);
        tv_result = (TextView) findViewById(R.id.tv_result);
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
            case R.id.check_version:
                String url = "http://192.168.0.101:8080/lxm/version.json";
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        parseJson(t);
                    }
                });
                break;
            case R.id.layout_scan:
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.layout_create:
                startActivity(new Intent(this,QRCreateActivity.class));
                break;
        }
    }

    private void parseJson(String t) {
        L.i(t);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("verisonCode");
            String content = jsonObject.getString("content");
            final String url = jsonObject.getString("url");
            if (code > versionCode) {
                new AlertDialog.Builder(this)
                        .setTitle("有新版本")
                        .setMessage(content)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(SettingActivity.this,DownloadActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPackageNameAndCode(){
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(),0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_result.setText(scanResult);
        }
    }
}
