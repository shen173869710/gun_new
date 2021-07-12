package com.auto.di.guan;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.basemodel.presenter.LoginPresenter;
import com.auto.di.guan.event.LoginEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebviewActivity extends IBaseActivity {

    @BindView(R.id.title_bar_back_layout)
    RelativeLayout titleBarBackLayout;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.title_bar_title)
    TextView titleBarTitle;


    private String url = "http://www.weather.com.cn/weather/101260601.shtml";

    @Override
    protected int setLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void init() {

        titleBarTitle.setText("天气情况");
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.loadUrl(url);

//        bindingView.get().web.loadData(content,"text/html; charset=UTF-8","UTF-8");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                Timber.e("onReceivedSslError");
//                handler.proceed();
//                super.onReceivedSslError(view, handler, error);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected LoginPresenter createPresenter() {
        return null;
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_bar_back_layout)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {

    }
}
