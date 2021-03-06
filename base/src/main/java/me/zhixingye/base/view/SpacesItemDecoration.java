package me.zhixingye.base.view;

import android.graphics.Rect;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YZX on 2017年06月27日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }


    private int space;
    private int mOrientation;
    private boolean isFirstSpace;
    private boolean isLastSpace;

    public SpacesItemDecoration(int space) {
        this(space, VERTICAL);
    }

    public SpacesItemDecoration(int space, @Orientation int orientation) {
        this(space, orientation, false, false);
    }

    public SpacesItemDecoration(int space, @Orientation int orientation, boolean isFirstSpace, boolean isLastSpace) {
        this.space = space;
        this.mOrientation = orientation;
        this.isFirstSpace = isFirstSpace;
        this.isLastSpace = isLastSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int index = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (mOrientation == VERTICAL) {
            if (index == 0) {
                if (isFirstSpace) {
                    outRect.top = space;
                }
                outRect.bottom = space;
            } else if (index == itemCount - 1) {
                if (isLastSpace) {
                    outRect.bottom = space;
                }
            } else {
                outRect.bottom = space;
            }
        } else {
            if (index == 0) {
                if (isFirstSpace) {
                    outRect.left = space;
                }
                if (index == itemCount - 1) {
                    outRect.right = space;
                }
            } else {
                outRect.left = space;
                if (index == itemCount - 1 && isLastSpace) {
                    outRect.right = space;
                }
            }
        }
    }
}