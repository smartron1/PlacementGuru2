package com.yourstrulyssj.placementguru.activity;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by SSJ_Recognized on 16-05-2018.
 */

public class WebViewController extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
