package com.lxm.smartbutler.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lxm.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    private ListView mlistView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        mlistView = (ListView) findViewById(R.id.mListView);
        mList.add("应用名:" + getString(R.string.app_name));
        mList.add("当前版本:" + getVersion());
        mList.add("官网:http://www.lxm.com");
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        mlistView.setAdapter(mAdapter);

    }
    private String getVersion(){
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(),0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }
    }
}
