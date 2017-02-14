package com.lxm.smartbutler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxm.smartbutler.R;
import com.lxm.smartbutler.entity.CourierData;

import java.util.List;

/**
 * Created by lxm on 17/2/14.
 */

public class CourierAdapter extends BaseAdapter {

    private List<CourierData.CourierItem> mLists;
    private Context mContext;
    private LayoutInflater inflater;

    public CourierAdapter(Context context,List<CourierData.CourierItem> mLists) {
        this.mLists = mLists;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.courier_item,null);
            viewHolder = new MyViewHolder();
            viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone = (TextView) convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            convertView.setTag(viewHolder);
        }
        viewHolder = (MyViewHolder) convertView.getTag();
        viewHolder.tv_remark.setText(mLists.get(i).getRemark());
        viewHolder.tv_datetime.setText(mLists.get(i).getDatetime());
        viewHolder.tv_zone.setText(mLists.get(i).getZone());
        return convertView;
    }

    private class MyViewHolder{
        public TextView tv_remark;
        public TextView tv_zone;
        public TextView tv_datetime;
    }
}
