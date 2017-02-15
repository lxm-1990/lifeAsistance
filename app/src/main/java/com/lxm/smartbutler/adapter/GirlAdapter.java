package com.lxm.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.GirlData;
import com.lxm.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * Created by lxm on 17/2/15.
 */

public class GirlAdapter extends BaseAdapter {

    private Context mContext;
    private List<GirlData> mList;
    private LayoutInflater inflater;
    private int width;

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GirlViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.girl_item,null);
            holder = new GirlViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.iv_img);
            view.setTag(holder);
        } else {
            holder = (GirlViewHolder) view.getTag();
        }
        GirlData data = mList.get(i);
        if (!TextUtils.isEmpty(data.getUrl())) {
            PicassoUtils.loadImageViewSize(mContext,data.getUrl(),width/2,500,holder.imageView);
        }
        return view;
    }

    private class GirlViewHolder {
        private ImageView imageView;
    }
}
