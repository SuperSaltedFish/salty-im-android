package me.zhixingye.base.listener;

import android.content.Context;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YZX on 2017年09月08日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */
public abstract class OnRecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    // 默认值 - 连续点击时，两次点击的最小间隔（防止快速点击），小于该时间则忽略本次点击事件
    private static final int INTERVAL_CLICK_MIN_DEFAULT = 200;

    // 记录上一次点击触发时的时间
    private long mLastClickTime = 0;

    // 持有监听的 RecyclerView
    private RecyclerView mRecyclerView;
    // 手势识别器
    private GestureDetectorCompat mGestureDetector;


    /**
     * RecyclerView 的一个 item 被点击时回调
     *
     * @param holder 被点击的viewHolder
     * @param event  点击点击事件
     */
    public abstract void onItemClicked(@NonNull RecyclerView.ViewHolder holder, @NonNull MotionEvent event);

    /**
     * RecyclerView 的一个 item 被长按时回调
     *
     * @param holder 被点击的viewHolder
     * @param event  点击点击事件
     */
    public void onItemLongClicked(@NonNull RecyclerView.ViewHolder holder, @NonNull MotionEvent event) {/* Do Nothing */ }

    /**
     * 提供给子类重写 {@link #INTERVAL_CLICK_MIN_DEFAULT}
     *
     * @return 点击间隔，单位毫秒
     */
    protected int getMinClickInterval() {
        return INTERVAL_CLICK_MIN_DEFAULT;
    }

    /**
     * 提供给子类重写，RecyclerView 是否需要拦截子控件的触摸事件
     */
    protected boolean needIntercept() {
        return false;
    }


    public OnRecyclerViewItemClickListener() {
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
        if (needIntercept()) {
            // 如果需要 RecyclerView 拦截事件，则该 RecyclerView 会回调进 onTouchEvent 再处理
            return true;
        }
        // 否则就在这里处理触摸事件
        processTouchEvent(recyclerView, event);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        // 当该方法回调时，说明该 RecyclerView 拦截了触摸事件，那就在这里处理
        processTouchEvent(recyclerView, motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { /* Do Nothing */ }


    /**
     * 对触摸事件处理，转换成上层需要的回调
     */
    private void processTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        this.mRecyclerView = recyclerView;

        if (mGestureDetector == null) {
            // 懒加载，第一次触发时对手势识别器做初始化
            initGestureDetectorCompat(recyclerView.getContext().getApplicationContext());
        }
        // 将触摸事件透传给手势识别器
        mGestureDetector.onTouchEvent(motionEvent);
    }

    private void initGestureDetectorCompat(@Nullable Context context) {
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (e != null) {
                    onGestureClicked(e);
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (e != null) {
                    onGestureLongPress(e);
                }
            }
        });
    }

    /**
     * GestureDetectorCompat 识别到一个点击操作时回调。
     */
    private void onGestureClicked(@NonNull MotionEvent event) {
        // 需要判断两次点击的时间间隔是否符合设定的最小间隔要求，防止快速点击
        long nowTime = SystemClock.elapsedRealtime();
        if (nowTime - mLastClickTime > getMinClickInterval()) {
            // 两次点击间隔时间大于指定值才相应
            RecyclerView.ViewHolder holder = getTargetViewHolder(event);
            if (holder != null) {
                onItemClicked(holder, event);
            }
        }
        mLastClickTime = nowTime;
    }

    /**
     * GestureDetectorCompat 识别到一个长按操作时回调
     */
    private void onGestureLongPress(@NonNull MotionEvent event) {
        RecyclerView.ViewHolder holder = getTargetViewHolder(event);
        if (holder != null) {
            onItemLongClicked(holder, event);
        }
    }

    /**
     * 根据触摸的坐标获取对应位置的 viewHolder
     */
    @Nullable
    private RecyclerView.ViewHolder getTargetViewHolder(@NonNull MotionEvent motionEvent) {
        // 根据触摸坐标查询是否有 item view
        View targetChildView = mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (targetChildView == null) {
            return null;
        }
        return mRecyclerView.getChildViewHolder(targetChildView);
    }
}