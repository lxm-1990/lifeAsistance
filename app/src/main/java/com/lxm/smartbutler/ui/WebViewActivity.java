package com.lxm.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lxm.smartbutler.R;

public class WebViewActivity extends BaseActivity {

    private ProgressBar progressBar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        initView();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webView = (WebView)findViewById(R.id.webView);

        final String url = getIntent().getStringExtra("url");
        //设置webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebChromeClient(new WebViewClient());

        webView.loadUrl(url);

        webView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
