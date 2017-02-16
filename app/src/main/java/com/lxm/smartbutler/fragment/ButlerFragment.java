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

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.adapter.ChatAdapter;
import com.lxm.smartbutler.entity.ChatData;
import com.lxm.smartbutler.utils.L;
import com.lxm.smartbutler.utils.SharePref;
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
    private SpeechSynthesizer mTts;

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

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts= SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

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
                et_chat.setText("");
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
                if (SharePref.getBoolean(getActivity(),"isSpeak",false)) {
                    startSpeak(text);
                }
                mList.add(new ChatData(ChatAdapter.LEFT_TYPE,text));
                mChatAdapter.notifyDataSetChanged();
                mChatListView.setSelection(mChatListView.getBottom());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startSpeak(String text) {
        //开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {}
        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}
        //开始播放
        public void onSpeakBegin() {}
        //暂停播放
        public void onSpeakPaused() {}
        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
    };
}
