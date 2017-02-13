package com.lxm.smartbutler.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.MyUser;
import com.lxm.smartbutler.ui.LoginActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    private Button btn_logOff,btn_sure_modify;
    private EditText et_name,et_age,et_sex,et_desc;
    private TextView tv_modify;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_logOff = (Button) view.findViewById(R.id.btn_exit_user);
        btn_logOff.setOnClickListener(this);
        btn_sure_modify = (Button) view.findViewById(R.id.btn_update_ok);
        btn_sure_modify.setOnClickListener(this);
        et_name = (EditText) view.findViewById(R.id.et_username);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        tv_modify = (TextView) view.findViewById(R.id.edit_user);
        tv_modify.setOnClickListener(this);

        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user != null) {
            et_name.setText(user.getUsername());
            et_sex.setText(user.isSex() ? "男":"女");
            et_age.setText(user.getAge()+"");
            et_desc.setText(user.getDesc());
        }
        setEnbled(false);

    }

    void setEnbled(boolean is){
        et_name.setEnabled(is);
        et_age.setEnabled(is);
        et_sex.setEnabled(is);
        et_desc.setEnabled(is);
    }
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_exit_user:
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btn_update_ok:
                String name = et_name.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String sex = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                if (!TextUtils.isEmpty(name)
                        & !TextUtils.isEmpty(age)
                        & !TextUtils.isEmpty(sex)
                        & !TextUtils.isEmpty(desc)){
                    MyUser newUser = new MyUser();
                    newUser.setUsername(name);
                    newUser.setAge(Integer.parseInt(age));
                    newUser.setSex(sex.equals("男")?true:false);
                    if (TextUtils.isEmpty(desc)) {
                        desc = "用户太懒了，没有设置描述";
                    }
                    newUser.setDesc(desc);
                    MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                                setEnbled(false);
                                btn_sure_modify.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(getActivity(),"修改失败"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_user:
                setEnbled(true);
                btn_sure_modify.setVisibility(View.VISIBLE);
                break;
        }
    }
}
