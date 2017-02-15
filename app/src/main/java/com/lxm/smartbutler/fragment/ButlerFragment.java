package com.lxm.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.adapter.ChatAdapter;
import com.lxm.smartbutler.entity.ChatData;
import com.lxm.smartbutler.utils.L;
import com.lxm.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButlerFragment extends Fragment implements View.OnClickListener{
    private ListView mChatListView;
    private EditText et_chat;
    private Button btn_send;
    private ChatAdapter mChatAdapter;
    private List<ChatData> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        et_chat = (EditText) view.findViewById(R.id.et_chat);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        mList.add(new ChatData(ChatAdapter.LEFT_TYPE,"你好,请问有什么吩咐"));
        mChatAdapter = new ChatAdapter(getActivity(),mList);
        mChatListView.setAdapter(mChatAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String chat = et_chat.getText().toString().trim();
                if (TextUtils.isEmpty(chat)) {
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (chat.length() > 30) {
                    Toast.makeText(getActivity(),"超出字符限制",Toast.LENGTH_SHORT).show();
                    return;
                }
                mList.add(new ChatData(ChatAdapter.RIGHT_TYPE,chat));
                mChatAdapter.notifyDataSetChanged();
                mChatListView.setSelection(mChatListView.getBottom());
                String url = "http://op.juhe.cn/robot/index?info=" + chat
                        + "&key=" + StaticClass.CHAT_LIST_KEY;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("!!!!!" + t);
                        super.onSuccess(t);
                        parseJson(t);
                    }
                });
                break;
        }
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                JSONObject resultObject = jsonObject.getJSONObject("result");
                String text = resultObject.getString("text");
                mList.add(new ChatData(ChatAdapter.LEFT_TYPE,text));
                mChatAdapter.notifyDataSetChanged();
                mChatListView.setSelection(mChatListView.getBottom());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
