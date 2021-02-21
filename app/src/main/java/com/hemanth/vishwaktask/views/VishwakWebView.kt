package com.hemanth.vishwaktask.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.hemanth.vishwaktask.views.client.VishwakWebViewClient

class VishwakWebView: WebView {

    constructor(context: Context) : super(context) {
        initDefaultSettings()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initDefaultSettings()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        initDefaultSettings()

            }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attributeSet, defStyleAttr, defStyleRes) {
        initDefaultSettings()

    }

    private fun initDefaultSettings() {
        settings.javaScriptEnabled = true
        webChromeClient = WebChromeClient()
        webViewClient = VishwakWebViewClient()
    }


}