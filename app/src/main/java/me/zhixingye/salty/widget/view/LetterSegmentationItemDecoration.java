package me.zhixingye.salty.widget.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.salty.protos.ContactProfile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.widget.adapter.ContactListAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactListHolder;


/**
 * Created by YZX on 2017年06月29日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */

public class LetterSegmentationItemDecoration extends RecyclerView.ItemDecoration {

    private float mTextHeight;
    private int mSpaceHeight;
    private float mStartDrawX;

    private TextPaint mTextPaint;
    private Paint mBackgroundPaint;

    private SparseArray<String> mDrawPositionMap;

    public LetterSegmentationItemDecoration() {
        super();

        mDrawPositionMap = new SparseArray<>();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        RecyclerView.ViewHolder currHolder = parent.getChildViewHolder(view);
        if (manager == null || !(currHolder instanceof ContactListHolder)) {
            return;
        }
        ConcatAdapter concatAdapter = (ConcatAdapter) parent.getAdapter();
        if (concatAdapter == null || concatAdapter.getItemCount() == 0) {
            return;
        }
        ContactListAdapter contactListAdapter = null;
        for (RecyclerView.Adapter<?> adapter : concatAdapter.getAdapters()) {
            if (adapter instanceof ContactListAdapter) {
                contactListAdapter = (ContactListAdapter) adapter;
                break;
            }
        }
        if (contactListAdapter == null) {
            return;
        }
        List<ContactProfile> list = contactListAdapter.getCurrentList();
        if (list.isEmpty()) {
            return;
        }

        int currAbsolutePosition = currHolder.getAbsoluteAdapterPosition();
        int currBindingAdapterPosition = currHolder.getBindingAdapterPosition();
        int prevPosition = currBindingAdapterPosition - 1;
        outRect.top = 0;
        mDrawPositionMap.delete(currAbsolutePosition);
        if (currBindingAdapterPosition != 0 && isIdenticalDrawContent(contactListAdapter.getCurrentList(), currBindingAdapterPosition, prevPosition)) {
            return;
        }

        outRect.top = mSpaceHeight;
        mDrawPositionMap.put(currAbsolutePosition, getDrawLetter(list, currBindingAdapterPosition));
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        if (manager == null) {
            return;
        }
        int startIndex = manager.findFirstVisibleItemPosition();
        int endIndex = manager.findLastVisibleItemPosition();
        for (int i = startIndex; i <= endIndex; i++) {
            String drawLetter = mDrawPositionMap.get(i);
            if (TextUtils.isEmpty(drawLetter)) {
                continue;
            }
            View view = manager.findViewByPosition(i);
            if (view == null) {
                continue;
            }
            float top;
            float textWidth;
            top = view.getTop() - mSpaceHeight;
            textWidth = mTextPaint.measureText(drawLetter);
            c.drawRect(0, top, parent.getWidth(), top + mSpaceHeight, mBackgroundPaint);
            c.drawText(drawLetter, mStartDrawX - textWidth / 2f, top + mSpaceHeight - (mSpaceHeight - mTextHeight) / 2, mTextPaint);
        }
    }

    public int findPositionByLetter(String letter) {
        int index = mDrawPositionMap.indexOfValue(letter);
        if (index >= 0) {
            return mDrawPositionMap.keyAt(index);
        }
        return -1;
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        mTextHeight = Math.abs(mTextPaint.getFontMetrics().ascent);
        mSpaceHeight = (int) (textSize * 1.4);
    }

    public void setTextColor(@ColorInt int textColor) {
        mTextPaint.setColor(textColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
    }

    public void setStartDrawX(float startDrawX) {
        mStartDrawX = startDrawX;
    }

    private static String getDrawLetter(List<ContactProfile> list, int position) {
        if (list.size() <= position) {
            return "#";
        }
        ContactProfile profile = list.get(position);
        if (profile == null || TextUtils.isEmpty(profile.getSortKey())) {
            return "#";
        }
        return profile.getSortKey().substring(0, 1);
    }

    private static boolean isIdenticalDrawContent(List<ContactProfile> list, int i1, int i2) {
        return TextUtils.equals(getDrawLetter(list, i1), getDrawLetter(list, i2));
    }
}
