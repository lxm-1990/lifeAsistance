package com.lxm.smartbutler.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by lxm on 17/2/7.
 */

public class UtilTools {
    public static void setFont(Context mContext, TextView textView){
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(tf);
    }
}
