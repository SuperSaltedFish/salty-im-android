package me.zhixingye.salty.widget.listener;

import android.os.SystemClock;
import android.view.View;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class OnOnlySingleClickListener implements View.OnClickListener {

    private static final int MAX_CLICK_INTERVAL = 400;

    private long lastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long nowTime = SystemClock.elapsedRealtime();
        if (nowTime - lastClickTime >= MAX_CLICK_INTERVAL) {
            lastClickTime = nowTime;
            onSingleClick(v);
        }
    }
}
