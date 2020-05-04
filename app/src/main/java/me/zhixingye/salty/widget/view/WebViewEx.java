package me.zhixingye.salty.widget.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import me.jessyan.autosize.AutoSize;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月04日.
 */
public class WebViewEx extends WebView {
    public WebViewEx(Context context) {
        super(context);
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WebViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
        if (getContext() instanceof Activity) {
            AutoSize.autoConvertDensityOfGlobal((Activity) getContext());
        }
    }
}
