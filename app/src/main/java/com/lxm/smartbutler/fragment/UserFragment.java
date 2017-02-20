package com.lxm.smartbutler.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.MyUser;
import com.lxm.smartbutler.ui.CourierActivity;
import com.lxm.smartbutler.ui.LoginActivity;
import com.lxm.smartbutler.ui.PhoneLocationAcitivity;
import com.lxm.smartbutler.utils.UtilTools;
import com.lxm.smartbutler.view.CustomDialog;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    private static final int TAKE_PIC = 0;
    private static final int CHOOSE_PIC = 1;
    private static final int CROP_PIC = 3;
    private static final String IMAGE_NAME = "output_image.png";

    private Button btn_logOff,btn_sure_modify;
    private EditText et_name,et_age,et_sex,et_desc;
    private TextView tv_modify;
    private CircleImageView takePic;
    private CustomDialog dialog;
    private Button take_pic,choose_pic,cancel;
    private Uri take_imageUri;
    private TextView tv_courier,tv_phone;
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

        takePic = (CircleImageView) view.findViewById(R.id.profile_image);
        UtilTools.getImageViewFromShare(getActivity(),takePic);
        takePic.setOnClickListener(this);
        dialog = new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,R.layout.choose_pic_layout,R.style.theme_dialog, Gravity.BOTTOM);
        dialog.setCancelable(false);

        take_pic = (Button) dialog.findViewById(R.id.take_pic);
        take_pic.setOnClickListener(this);
        choose_pic = (Button) dialog.findViewById(R.id.choose_pic);
        choose_pic.setOnClickListener(this);
        cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

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
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.take_pic:
                takePicture();
                break;
            case R.id.choose_pic:
                choosePicture();
                break;
            case R.id.tv_courier:
                Intent intent_courier = new Intent(getActivity(), CourierActivity.class);
                getActivity().startActivity(intent_courier);
                break;
            case R.id.tv_phone:
                Intent intent_phone = new Intent(getActivity(), PhoneLocationAcitivity.class);
                getActivity().startActivity(intent_phone);
                break;
        }
    }

    private void takePicture() {
        //getExternalCacheDir关联缓存目录，在6.0以上可以不用动态申请权限
        File outputImage = new File(getActivity().getExternalCacheDir(),IMAGE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getActivity(),"com.lxm.smartbutler.fileprovider",outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent_take = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_take.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent_take,TAKE_PIC);
        dialog.dismiss();
    }
    private void choosePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PIC);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED){
            switch (requestCode) {
                case TAKE_PIC:{
                    //遗留问题：android7.0以上系统无法剪裁，开能是uri问题
                    File outputImage1 = new File(getActivity().getExternalCacheDir(),IMAGE_NAME);
                    if (Build.VERSION.SDK_INT >= 24) {
                        take_imageUri = FileProvider.getUriForFile(getActivity(),"com.lxm.smartbutler.fileprovider",outputImage1);
                    } else {
                        take_imageUri = Uri.fromFile(outputImage1);
                    }
                    startPhotoZoom(take_imageUri);
                    break;
                }
                case CHOOSE_PIC:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_PIC:{
                    File outputImage = new File(getActivity().getExternalCacheDir(),IMAGE_NAME);
                    if (outputImage != null) {
                        outputImage.delete();
                    }
                    setImageView(data);
                    break;
                }

            }
        }
    }
    private void startPhotoZoom(Uri uri){
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);// 输出图片大小
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, CROP_PIC);
    }

    public void setImageView(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            takePic.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilTools.setImageViewToShare(getActivity(),takePic);
    }
}
