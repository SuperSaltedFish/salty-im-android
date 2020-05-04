package me.zhixingye.salty.module.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.tool.DirectoryHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月04日.
 */
public class WebFragment extends BasicFragment {

    public static final String EXTRA_URL = "Url";

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private OnWebClientListener mOnWebClientListener;

    private String mUrl;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_web;
    }

    @Override
    protected void init(View parentView) {
        mWebView = parentView.findViewById(R.id.mWebView);
        mProgressBar = parentView.findViewById(R.id.mProgressBar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(EXTRA_URL);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setup(Bundle savedInstanceState) {
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);  //开启JS支持
        settings.setDomStorageEnabled(true);  //开启DOM形式存储
        settings.setDatabaseEnabled(true);    //开启数据库形式存储
        settings.setAppCacheEnabled(true);    //开启数据库形式存储
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath(DirectoryHelper.getInternalWebCachePath());

        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);

        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    private final WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            String url = mWebView.getUrl();
            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(title) && url.contains(title)) {//防止title显示网站
                return;
            }
            if (mOnWebClientListener != null) {
                mOnWebClientListener.onReceivedTitle(title);
            }
        }

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }


        }
    };

    private final WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    public boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }

    public void setOnWebClientListener(OnWebClientListener onWebClientListener) {
        mOnWebClientListener = onWebClientListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroyView() {
        mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        mWebView.clearHistory();
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.setVisibility(View.GONE);
        mWebView.destroy();
        super.onDestroyView();
        System.gc();
    }

    public interface OnWebClientListener {
        void onReceivedTitle(String title);
    }

}
