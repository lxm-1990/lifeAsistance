package com.lxm.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneLocationAcitivity extends BaseActivity implements View.OnClickListener{

    private EditText et_number;
    private ImageView iv_company;
    private TextView tv_result;
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_del,btn_query;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_location_acitivity);
        initView();
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_number.setText("");
                return false;
            }
        });
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String str = et_number.getText().toString();
        switch (view.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag == true) {
                    flag = false;
                    str = "";
                }
                et_number.setText(str + ((Button)view).getText().toString());
                et_number.setSelection(str.length()+1);
                break;
            case R.id.btn_del:
                if (!TextUtils.isEmpty(str)){
                    et_number.setText(str.substring(0,str.length()-1));
                    et_number.setSelection(str.length()-1);
                }
                break;
            case R.id.btn_query:
                String phone = et_number.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(PhoneLocationAcitivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.PHONE_KEY;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        parseJson(t);
                    }
                });
                flag = true;
                break;
        }
    }
    private void parseJson(String t) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(t);
            String resultcode = jsonObject.getString("resultcode");
            int error_code = jsonObject.getInt("error_code");
            if (resultcode.equals("200") && error_code == 0) {
                JSONObject result = jsonObject.getJSONObject("result");
                String province = result.getString("province");
                String city = result.getString("city");
                String areacode = result.getString("areacode");
                String zip = result.getString("zip");
                String company = result.getString("company");
                String card = result.getString("card");
                tv_result.setText("归属地:" + province + city + "\n"
                        + "区号:" + areacode + "\n"
                        + "邮编:" + zip + "\n"
                        + "运营商:" + company + "\n"
                        + "类型:" + card);
                if (company.equals("移动")) {
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                } else if (company.equals("联通")){
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                } else if (company.equals("电信")){
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
