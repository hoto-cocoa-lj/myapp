package com.slq.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.slq.myapp.R;
import com.slq.myapp.jsbridge.BridgeHandler;
import com.slq.myapp.jsbridge.BridgeWebView;
import com.slq.myapp.jsbridge.CallBackFunction;

import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {
    BridgeWebView bridgeWebView;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        // ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        initView1();
        initData1();
    }
    void initView1() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
        bridgeWebView.registerHandler("a",new BridgeHandler(){
            @Override
            public void handler(String data, CallBackFunction function) {
                //finish();
            }
        });
    }

    void initData1() {
        url = getIntent().getExtras().getString("url");
        initBridgeWebView();
    }

    private void initBridgeWebView() {
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl(url);

    }
}