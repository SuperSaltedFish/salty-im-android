package me.zhixingye.salty.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by zhixingye on 2020年03月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class PhoneEditText extends LinearLayout {
    public PhoneEditText(Context context) {
        this(context, null);
    }

    public PhoneEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
