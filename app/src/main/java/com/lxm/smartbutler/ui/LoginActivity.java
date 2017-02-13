package com.lxm.smartbutler.ui;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxm.smartbutler.MainActivity;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.MyUser;
import com.lxm.smartbutler.utils.SharePref;
import com.lxm.smartbutler.view.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_user,et_password;
    private Button btn_register,btn_login;
    private CheckBox remember_pass;
    private TextView find_pass;
    private CustomDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        btn_register = (Button) findViewById(R.id.btn_registered);
        btn_register.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_login.setOnClickListener(this);
        et_user = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        remember_pass = (CheckBox) findViewById(R.id.keep_password);
        remember_pass.setChecked(false);
        find_pass = (TextView) findViewById(R.id.tv_forget);
        find_pass.setOnClickListener(this);
        if (SharePref.getBoolean(this,"keeppass",false)){
            remember_pass.setChecked(true);
            et_user.setText(SharePref.getString(this,"name",""));
            et_password.setText(SharePref.getString(this,"password",""));
        }
        dialog = new CustomDialog(this,R.layout.custom_layout,R.style.theme_dialog);
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registered:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                String name = et_user.getText().toString().trim();
                String pass = et_password.getText().toString().trim();
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(pass)) {
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(pass);
                    dialog.show();
                    user.login(new SaveListener<MyUser>() {

                        @Override
                        public void done(MyUser bmobUser, BmobException e) {
                            dialog.dismiss();
                            if(e==null){
                                if (bmobUser.getEmailVerified()) {
                                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this,"注册时邮箱没有激活", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"登录失败:"+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_forget:
                Intent intent1 = new Intent(this,findPassActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharePref.setBoolean(this, "keeppass",remember_pass.isChecked());
        if (remember_pass.isChecked()) {
            SharePref.setString(this,"name",et_user.getText().toString());
            SharePref.setString(this,"password",et_password.getText().toString());
        } else {
            SharePref.remove(this,"name");
            SharePref.remove(this,"password");
        }
    }
}
