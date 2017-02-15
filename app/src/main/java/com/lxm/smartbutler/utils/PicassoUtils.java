package com.lxm.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by lxm on 17/2/15.
 */

public class PicassoUtils {
    public static void loadImageView(Context mContext, String url, ImageView imageView){
        Picasso.with(mContext)
                .load(url)
                .into(imageView);
    }

    public static void loadImageViewSize(Context mContext, String url, int width,int height,ImageView imageView) {
        Picasso.with(mContext)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImageViewHolder(Context mContext, String url,int holder,int error, ImageView imageView){
        Picasso.with(mContext)
                .load(url)
                .placeholder(holder)
                .error(error)
                .into(imageView);
    }

    public static void loadImageViewCrop(Context mContext, String url, ImageView imageView){
        Picasso.with(mContext)
                .load(url)
                .transform(new CropSquareTransformation())
                .into(imageView);
    }

    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
