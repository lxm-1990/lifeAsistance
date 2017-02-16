package com.lxm.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.lxm.smartbutler.R;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {

    private static final int HANDLER_DOWNLOADING = 1001;
    private static final int HANDLER_SUCCESS = 1002;
    private static final int HANDLER_FAIL = 1003;

    private NumberProgressBar progressBar;
    private TextView tv_size;
    private String url;
    private String path;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_DOWNLOADING:
                    Bundle bundle = message.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + "/" + totalSize);
                    progressBar.setProgress((int)((float)transferredBytes/(float) totalSize * 100));
                    break;
                case HANDLER_SUCCESS:
                    tv_size.setText("下载成功");
                    installAPK();
                    finish();
                    break;
                case HANDLER_FAIL:
                    tv_size.setText("下载失败");
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        url = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        progressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        tv_size = (TextView) findViewById(R.id.tv_size);
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        RxVolley.download(path, url, new ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_DOWNLOADING;
                Bundle bundle = new Bundle();
                bundle.putLong("transferredBytes",transferredBytes);
                bundle.putLong("totalSize",totalSize);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_SUCCESS;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_FAIL;
                mHandler.sendMessage(message);
            }
        });
    }
    private void installAPK(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
