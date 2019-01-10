package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.yourstrulyssj.placementguru.R;

public class webView extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent i = getIntent();
        String url = i.getStringExtra("url");


        webView = (WebView) findViewById(R.id.Page);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewController());
    }
}
