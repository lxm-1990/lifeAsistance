package com.lxm.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.ChatData;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by lxm on 17/2/15.
 */

public class ChatAdapter extends BaseAdapter {

    public static final int LEFT_TYPE = 0;
    public static final int RIGHT_TYPE = 1;

    private List<ChatData> mList;
    private Context mContext;
    private LayoutInflater inflater;

    public ChatAdapter(Context mContext, List<ChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        int type = getItemViewType(position);
        LeftViewHolder leftViewHolder = null;
        RightViewHolder rightViewHolder = null;
        if (view == null) {
            if (type == LEFT_TYPE){
                view = inflater.inflate(R.layout.left_item,null);
                leftViewHolder = new LeftViewHolder();
                leftViewHolder.tv_left_text = (TextView) view.findViewById(R.id.text);
                view.setTag(leftViewHolder);
            } else if (type == RIGHT_TYPE) {
                view = inflater.inflate(R.layout.right_item,null);
                rightViewHolder = new RightViewHolder();
                rightViewHolder.tv_right_text = (TextView) view.findViewById(R.id.text);
                view.setTag(rightViewHolder);
            }
        }
        if (type == LEFT_TYPE) {
            leftViewHolder = (LeftViewHolder) view.getTag();
            leftViewHolder.tv_left_text.setText(mList.get(position).getText());
        } else if (type == RIGHT_TYPE) {
            rightViewHolder = (RightViewHolder) view.getTag();
            rightViewHolder.tv_right_text.setText(mList.get(position).getText());
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class LeftViewHolder {
        private TextView tv_left_text;
    }

    private class RightViewHolder {
        private TextView tv_right_text;
    }
}
