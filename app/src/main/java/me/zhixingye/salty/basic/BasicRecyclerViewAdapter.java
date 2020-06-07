package me.zhixingye.salty.basic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

public abstract class BasicRecyclerViewAdapter<D, VH extends BasicRecyclerViewAdapter.BasicViewHolder<D>>
        extends RecyclerView.Adapter<BasicRecyclerViewAdapter.BasicViewHolder<?>> {

    private static final int HOLDER_TYPE_HEADER = -1000;
    private static final int HOLDER_TYPE_FOOTER = -2000;

    protected Context mContext;
    private View mHeaderView;
    private View mFooterView;

    protected abstract VH getViewHolder(ViewGroup parent, int viewType);

    public abstract int getViewHolderType(int position);

    public abstract int getViewHolderCount();

    public abstract D getItemData(int position);

    @NonNull
    @Override
    public BasicViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        switch (viewType) {
            case HOLDER_TYPE_HEADER:
            case HOLDER_TYPE_FOOTER:
                return new HeaderOrFooterHolder(mHeaderView);
            default:
                return getViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        if (mContext == null) {
            mContext = holder.itemView.getContext();
        }

        if (holder instanceof BasicRecyclerViewAdapter.HeaderOrFooterHolder) {
            return;
        }
        if (mHeaderView != null) {
            holder.onBindData(getItemData(position - 1));
        } else {
            holder.onBindData(getItemData(position));
        }
    }

    @Override
    public void onViewRecycled(@NonNull BasicViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof HeaderOrFooterHolder) {
            return;
        }
        holder.onRecycled();
    }

    @Override
    public final int getItemCount() {
        int count = getViewHolderCount();
        if (mHeaderView != null) {
            count++;
        }
        if (mFooterView != null) {
            count++;
        }
        return count;
    }

    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return HOLDER_TYPE_HEADER;
        }
        if (mFooterView != null && position == getItemCount() - 1) {
            return HOLDER_TYPE_FOOTER;
        }
        if (mHeaderView != null) {
            return getViewHolderType(position - 1);
        } else {
            return getViewHolderType(position);
        }
    }

    public void setHeaderView(View headerView) {
        if (mHeaderView != headerView) {
            notifyDataSetChanged();
        }
        mHeaderView = headerView;
    }

    public void setFooterView(View footerView) {
        if (mFooterView != footerView) {
            notifyDataSetChanged();
        }
        mFooterView = footerView;
    }

    public boolean isHasHeaderView() {
        return mHeaderView != null;
    }

    public boolean isHasFooterView() {
        return mFooterView != null;
    }

    public final void notifyItemRangeInsertedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeInserted(positionStart, itemCount);
        } else {
            this.notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }

    public final void notifyItemRangeRemovedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeRemoved(positionStart, itemCount);
        } else {
            this.notifyItemRangeRemoved(positionStart + 1, itemCount);
        }
    }

    public final void notifyItemMovedEx(int fromPosition, int toPosition) {
        if (mHeaderView == null) {
            this.notifyItemMoved(fromPosition, toPosition);
        } else {
            this.notifyItemMoved(fromPosition + 1, toPosition);
        }
    }

    public final void notifyItemRangeChangedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeChanged(positionStart, itemCount);
        } else {
            this.notifyItemRangeChanged(positionStart + 1, itemCount);
        }
    }

    public final void notifyItemRangeChangedEx(int positionStart, int itemCount, Object payload) {
        if (mHeaderView == null) {
            this.notifyItemRangeChanged(positionStart, itemCount, payload);
        } else {
            this.notifyItemRangeChanged(positionStart + 1, itemCount, payload);
        }
    }

    public final void notifyItemInsertedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemInserted(position);
        } else {
            this.notifyItemInserted(position + 1);
        }
    }

    public final void notifyItemChangedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemChanged(position);
        } else {
            this.notifyItemChanged(position + 1);
        }
    }

    public final void notifyItemRemovedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemRemoved(position);
        } else {
            this.notifyItemRemoved(position + 1);
        }
    }

    public abstract static class BasicViewHolder<T> extends RecyclerView.ViewHolder {

        protected Context mContext;

        public BasicViewHolder(@LayoutRes int layoutRes, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
            mContext = parent.getContext();
            onCreateItemView(itemView);
        }

        public BasicViewHolder(View itemView) {
            super(itemView);
        }

        protected abstract void onCreateItemView(View itemView);

        protected abstract void onBindData(T data);

        protected abstract void onRecycled();
    }

    private static class HeaderOrFooterHolder extends BasicViewHolder<Void> {

        private HeaderOrFooterHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onCreateItemView(View itemView) {

        }

        @Override
        protected void onBindData(Void data) {

        }

        @Override
        protected void onRecycled() {

        }
    }

    //由于扩展了局部刷新，ListUpdateCallback也要扩展一下
    public static class ListUpdateCallback implements
            androidx.recyclerview.widget.ListUpdateCallback {

        private BasicRecyclerViewAdapter mAdapter;

        public ListUpdateCallback(BasicRecyclerViewAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onInserted(int position, int count) {
            mAdapter.notifyItemRangeInsertedEx(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            mAdapter.notifyItemRangeRemovedEx(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            mAdapter.notifyItemMovedEx(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            mAdapter.notifyItemRangeChangedEx(position, count, payload);
        }
    }
}
