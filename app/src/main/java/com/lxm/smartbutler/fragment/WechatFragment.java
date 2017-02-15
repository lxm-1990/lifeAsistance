package com.lxm.smartbutler.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.adapter.ChatAdapter;
import com.lxm.smartbutler.adapter.WeChatAdapter;
import com.lxm.smartbutler.entity.ChatData;
import com.lxm.smartbutler.entity.WeChatData;
import com.lxm.smartbutler.ui.WebViewActivity;
import com.lxm.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WechatFragment extends Fragment {

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        return view;
    }
    private void findView(View view){
        mListView = (ListView) view.findViewById(R.id.mListView);
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parseJson(t);
            }
        });
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                JSONObject resultObject = jsonObject.getJSONObject("result");
                JSONArray list = resultObject.getJSONArray("list");
                for (int i = 0; i < list.length() ; ++i) {
                    JSONObject object = list.getJSONObject(i);
                    WeChatData data = new WeChatData();
                    data.setTitle(object.getString("title"));
                    data.setFirstImg(object.getString("firstImg"));
                    data.setUrl(object.getString("url"));
                    data.setSource(object.getString("source"));
                    mList.add(data);
                }
            }
            WeChatAdapter adapter = new WeChatAdapter(getActivity(),mList);
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    WeChatData data = mList.get(i);
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title",data.getTitle());
                    intent.putExtra("url",data.getUrl());
                    getActivity().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
