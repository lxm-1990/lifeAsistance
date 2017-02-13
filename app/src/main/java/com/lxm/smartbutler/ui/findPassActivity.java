package com.lxm.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lxm.smartbutler.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class findPassActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_old_pass,et_new_pass,et_new_repass,et_email;
    private Button btn_modify_pass,btn_find_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        initView();
    }

    private void initView() {
        et_old_pass = (EditText) findViewById(R.id.et_now);
        et_new_pass = (EditText) findViewById(R.id.et_new);
        et_new_repass = (EditText) findViewById(R.id.et_new_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_modify_pass = (Button) findViewById(R.id.btn_update_password);
        btn_modify_pass.setOnClickListener(this);
        btn_find_pass = (Button) findViewById(R.id.btn_forget_password);
        btn_find_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password:
                String old = et_old_pass.getText().toString().trim();
                String new_pass = et_new_pass.getText().toString().trim();
                String new_repass = et_new_repass.getText().toString().trim();
                if (!TextUtils.isEmpty(old) & !TextUtils.isEmpty(new_pass) & !TextUtils.isEmpty(new_repass)){
                    if (new_pass.equals(new_repass)) {
                        BmobUser.updateCurrentUserPassword(old, new_pass, new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(findPassActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(findPassActivity.this,"修改密码失败"+e,Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_forget_password:
                final String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(findPassActivity.this,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(findPassActivity.this,"重置密码失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
