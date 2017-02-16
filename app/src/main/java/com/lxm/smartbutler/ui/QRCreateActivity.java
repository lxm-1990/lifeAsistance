package com.lxm.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.lxm.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class QRCreateActivity extends BaseActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcreate);

        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        int width = getResources().getDisplayMetrics().widthPixels;
        String contentString = "1234";
        if (!contentString.equals("")) {
            //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
            Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString, width/2, width/2,
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            imageView.setImageBitmap(qrCodeBitmap);
        } else {
            Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
