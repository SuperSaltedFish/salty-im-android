package me.zhixingye.base.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public abstract class OnViewTouchClickListener implements View.OnTouchListener {

    protected abstract void onClick(View v, int touchX, int touchY);

    protected abstract void onLongClick(View v, int touchX, int touchY);

    private int mLastPointX;
    private int mLastPointY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mLastPointX = (int) event.getX();
        mLastPointY = (int) event.getY();
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
        return false;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OnViewTouchClickListener.this.onClick(v, mLastPointX, mLastPointY);
        }
    };

    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            OnViewTouchClickListener.this.onLongClick(v, mLastPointX, mLastPointY);
            return true;
        }
    };
}
