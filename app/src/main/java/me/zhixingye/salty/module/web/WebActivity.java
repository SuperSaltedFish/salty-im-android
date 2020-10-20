package me.zhixingye.salty.module.web;

import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.salty.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class WebActivity
        extends BasicActivity
        implements WebFragment.OnWebClientListener {


    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebFragment.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setToolbarId(R.id.mDefaultToolbar,true);

        String url = getIntent().getStringExtra(WebFragment.EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        WebFragment fragment = WebFragment.newInstance(url);
        fragment.setOnWebClientListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mFlContent, fragment)
                .commit();
    }

    @Override
    public void onReceivedTitle(String title) {
        setTitle(title);
    }
}
