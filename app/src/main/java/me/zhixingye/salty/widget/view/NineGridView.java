package me.zhixingye.salty.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import me.zhixingye.salty.widget.listener.OnOnlySingleClickListener;

/**
 * Created by YZX on 2017年08月26日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */
public class NineGridView extends ViewGroup {

    private OnItemClickListener mOnItemClickListener;
    private int mSpacing;
    private int mRow;
    private int mColumn;
    private int mViewWidth;
    private int mViewHeight;

    private Adapter mAdapter;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, context.getResources().getDisplayMetrics());
    }

    void updateView() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            destroyAllView();
            return;
        }

        final int childCount = getChildCount();
        final int itemCount = mAdapter.getCount();
        mRow = (int) Math.ceil(itemCount / 3d);
        if (mRow > 9) {
            mRow = 9;
        }
        if (itemCount > 2) {
            mColumn = 3;
        } else {
            mColumn = itemCount;
        }

        if (itemCount > childCount) {
            for (int i = childCount; i < itemCount; i++) {
                addView(createView(i), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
        } else if (itemCount < childCount) {
            for (int i = childCount - 1; i >= itemCount; i--) {
                destroyView(i);
            }
        }
        for (int i = 0; i < itemCount; i++) {
            mAdapter.setupImageResource(i, getChildAt(i));
        }
    }

    private View createView(final int index) {
        View View = mAdapter.createView(this);
        View.setOnClickListener(new OnOnlySingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, index);
                }
            }
        });
        return View;
    }

    private void destroyView(int index) {
        View view = getChildAt(index);
        if (view != null) {
            view.setOnClickListener(null);
            if (mAdapter != null) {
                mAdapter.destroyView(view);
            }
            removeViewAt(index);
        }
    }

    private void destroyAllView() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            destroyView(i);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width = maxWidth;
        int height = maxHeight;

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            if (mRow == 1 && mColumn == 1) {
                height = width * 9 / 16;
            } else {
                height = width * mRow / 3;
            }
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = height;
        } else {
            int minSize = Math.min(width, height);
            width = minSize;
            height = minSize;
        }

        if (mRow == 1 && mColumn == 1) {
            measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        } else {
            int childMeasureSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY);
            measureChildren(childMeasureSpec, childMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int childSize = (mViewWidth - 2 * mSpacing) / 3;
        int row = 0;
        int column = 0;
        for (int i = 0; i < count; i++) {
            if (column >= mColumn) {
                column = 0;
                row++;
            }
            if (row >= mRow) {
                row = 0;
            }
            int childLeft = childSize * column;
            int childTop = childSize * row;
            if (column != 0) {
                childLeft += (mSpacing * column);
            }
            if (row != 0) {
                childTop += (mSpacing * row);
            }
            if (count == 1) {
                getChildAt(i).layout(0, 0, mViewWidth, mViewHeight);
            } else {
                getChildAt(i).layout(childLeft, childTop, childLeft + childSize, childTop + childSize);
            }
            column++;
        }
    }


    public int getSpacing() {
        return mSpacing;
    }

    public void setSpacing(int spacing) {
        mSpacing = spacing;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeOnItemClickListener() {
        mOnItemClickListener = null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter != adapter) {
            if (mAdapter != null) {
                mAdapter.setNineGridView(null);
            }
            mAdapter = adapter;
            if (mAdapter != null) {
                mAdapter.setNineGridView(this);
            }
            updateView();
        }
    }

    public abstract static class Adapter {
        protected abstract View createView(ViewGroup parent);

        protected abstract void setupImageResource(int position, View v);

        protected abstract void destroyView(View v);

        protected abstract int getCount();

        private NineGridView mNineGridView;

        void setNineGridView(NineGridView v) {
            mNineGridView = v;
        }

        public void notifyDataSetChanged() {
            if (mNineGridView != null) {
                mNineGridView.updateView();
            }
        }
    }
}

