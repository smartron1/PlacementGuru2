package com.yourstrulyssj.placementguru.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.yourstrulyssj.placementguru.R;

public class alumnaeFeedback extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnae_feedback);

        webView = (WebView) findViewById(R.id.form);
        webView.loadUrl("https://docs.google.com/forms/d/13hG4NqApeqpjSmOksYb97FeeGokvpIaiBTzKNzl4VoM/edit");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewController());


    }
}
