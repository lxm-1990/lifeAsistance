package com.lxm.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lxm.smartbutler.R;
import com.lxm.smartbutler.adapter.GirlAdapter;
import com.lxm.smartbutler.entity.GirlData;
import com.lxm.smartbutler.utils.PicassoUtils;
import com.lxm.smartbutler.utils.StaticClass;
import com.lxm.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class GrilFragment extends Fragment {

    private GridView mGridView;
    private List<GirlData> mList = new ArrayList<>();
    private CustomDialog dialog;
    private PhotoView iv_img;
    private PhotoViewAttacher mAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gril, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,R.layout.girl_dialog,R.style.theme_dialog, Gravity.CENTER);
        iv_img = (PhotoView) dialog.findViewById(R.id.iv_img);

        String welfare = null;
        try {
            //Gank升級 需要转码
            welfare = URLEncoder.encode("福利", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //加载数据
        String url = StaticClass.GIRL_URL.replace("option",welfare);
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parseJson(t);
            }
        });


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!TextUtils.isEmpty(mList.get(i).getUrl())){
                    PicassoUtils.loadImageView(getActivity(),mList.get(i).getUrl(),iv_img);
                    mAttacher = new PhotoViewAttacher(iv_img);
                    mAttacher.update();
                }
                dialog.show();
            }
        });
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean error = jsonObject.getBoolean("error");
            if (!error) {
                JSONArray array = jsonObject.getJSONArray("results");
                for (int i = 0 ; i<array.length();++i) {
                    String url = array.getJSONObject(i).getString("url");
                    GirlData data = new GirlData();
                    data.setUrl(url);
                    mList.add(data);
                }
            }
            GirlAdapter adapter = new GirlAdapter(getActivity(),mList);
            mGridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
