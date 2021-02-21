package com.hemanth.vishwaktask.views.client

import android.webkit.WebView
import android.webkit.WebViewClient

class VishwakWebViewClient: WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
        }
        return true
    }
}