package com.lxm.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by lxm on 17/2/7.
 */

public class UtilTools {
    public static void setFont(Context mContext, TextView textView){
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(tf);
    }

    public static void setImageViewToShare(Context mContext, ImageView imageView){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,outputStream);

        byte[] array = outputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(array,Base64.DEFAULT));

        SharePref.setString(mContext,"image",imageString);
    }

    public static void getImageViewFromShare(Context mContext, ImageView imageView) {
        String imageString = SharePref.getString(mContext,"image","");
        if (!TextUtils.isEmpty(imageString)) {
            byte[] array = Base64.decode(imageString,Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        }
    }
}
