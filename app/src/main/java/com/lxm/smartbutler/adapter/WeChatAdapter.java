package com.lxm.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.ChatData;
import com.lxm.smartbutler.entity.WeChatData;
import com.lxm.smartbutler.utils.L;
import com.lxm.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * Created by lxm on 17/2/15.
 */

public class WeChatAdapter  extends BaseAdapter{

    private List<WeChatData> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private int width,height;

    public WeChatAdapter(Context mContext, List<WeChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
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

        WeChatHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.wechat_item,null);
            holder = new WeChatHolder();
            holder.iv_img = (ImageView) view.findViewById(R.id.iv_icon);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_resource = (TextView) view.findViewById(R.id.tv_source);
            view.setTag(holder);
        } else {
            holder = (WeChatHolder) view.getTag();
        }
        WeChatData data = mList.get(i);
        holder.tv_title.setText(data.getTitle());
        holder.tv_resource.setText(data.getSource());
        if (TextUtils.isEmpty(data.getFirstImg())){
            L.e(data + "");
        } else {
            PicassoUtils.loadImageViewSize(mContext,data.getFirstImg(),width/3,150,holder.iv_img);
        }
        return view;
    }

    private class WeChatHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_resource;
    }
}
