package com.lxm.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name,et_age,et_desc,et_pass,et_repass,et_email;
    private RadioGroup mRadioGroup;
    private Button btn_register;

    private boolean isGender = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_repass = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);

        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.rb_boy == i) {
                    isGender = true;
                } else {
                    isGender = false;
                }
            }
        });
        btn_register = (Button) findViewById(R.id.btnRegistered);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistered:
                register();
                break;
        }
    }

    private void register() {
        String name = et_name.getText().toString().trim();
        String age = et_age.getText().toString().trim();
        String desc = et_desc.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();
        String repass = et_repass.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if (!TextUtils.isEmpty(name)
                & !TextUtils.isEmpty(age)
                & !TextUtils.isEmpty(pass)
                & !TextUtils.isEmpty(repass)
                & !TextUtils.isEmpty(email)){
            if (TextUtils.isEmpty(desc)) {
                desc = "用户太懒了，没有设置描述";
            }
            if (pass.equals(repass)) {
                MyUser myUser = new MyUser();
                myUser.setUsername(name);
                myUser.setAge(Integer.parseInt(age));
                myUser.setPassword(pass);
                myUser.setDesc(desc);
                myUser.setEmail(email);
                myUser.setSex(isGender);
                myUser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,"注册失败:"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
