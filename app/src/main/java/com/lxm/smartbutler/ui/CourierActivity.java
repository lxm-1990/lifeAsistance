package com.lxm.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.adapter.CourierAdapter;
import com.lxm.smartbutler.entity.CourierData;
import com.lxm.smartbutler.utils.StaticClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourierActivity extends BaseActivity {

    private EditText et_com,et_no;
    private Button btn_query;
    private ListView listView;
    private List<CourierData.CourierItem> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_com = (EditText) findViewById(R.id.et_com);
        et_no = (EditText) findViewById(R.id.et_no);
        et_com.setText("tt");
        et_no.setText("886232427546");
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询数据
                String com = et_com.getText().toString();
                String no = et_no.getText().toString();
                if (TextUtils.isEmpty(com) || TextUtils.isEmpty(no)) {
                    Toast.makeText(CourierActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "http://v.juhe.cn/exp/index?key="
                        + StaticClass.COURIER_KEY + "&com=" + com + "&no= " + no;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        parseJson(t);
                    }
                });

            }
        });
        listView = (ListView) findViewById(R.id.list_courier);
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        CourierData data = gson.fromJson(json, CourierData.class);
        if (data != null && data.getError_code() == 0 && data.getResultcode().equals("200")) {
            listView.setVisibility(View.VISIBLE);
            mList = data.getResult().getList();
            Collections.reverse(mList);
            CourierAdapter adapter = new CourierAdapter(this,mList);
            listView.setAdapter(adapter);
        }
    }

}
