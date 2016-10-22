package com.minhnpa.coderschool.newyorktimesarticlesearch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MINH NPA on 22 Oct 2016.
 */

public class FullArticleActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.tvHeadline)
    TextView tvHeadline;

    @BindView(R.id.tvUrl)
    TextView tvUrl;

    @BindView(R.id.btnClose)
    ImageButton btnClose;

    String headline, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        ButterKnife.bind(this);

        headline = getIntent().getStringExtra("headline");
        url = getIntent().getStringExtra("web_url");

        tvHeadline.setText(headline);
        tvUrl.setText(url);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
