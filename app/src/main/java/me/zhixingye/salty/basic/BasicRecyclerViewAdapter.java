package me.zhixingye.salty.basic;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YZX on 2018年08月20日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */

//这个Class主要为RecycleView提供了header和footer的能力，由于本质上都是基于getViewHolderType做的，所以onCreateViewHolder等抽象方法和局部刷新功能也要重新包装一下
@SuppressWarnings("unchecked")
public abstract class BasicRecyclerViewAdapter<VH extends BasicRecyclerViewAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<BasicRecyclerViewAdapter.BaseViewHolder> {

    private static final int HOLDER_TYPE_HEADER = -1000;  //header的type
    private static final int HOLDER_TYPE_FOOTER = -2000;  //footer的type

    protected Context mContext;
    private View mHeaderView; //headerView
    private View mFooterView; //footerView

    //子类实现，用来获取ViewHolder,类似onCreateViewHolder();
    public abstract VH getViewHolder(ViewGroup parent, int viewType);

    //子类实现，用来绑定ViewHolder,类似onBindViewHolder();
    public abstract void bindDataToViewHolder(VH holder, int position);

    //子类实现，用来获取ViewHolder数量（不包含header和footer）,类似getItemCount();
    public abstract int getViewHolderCount();

    public void onViewHolderRecycled(VH holder) {
    }

    //子类实现，用来获取ViewHolder类型（不包含header和footer）,类似getItemViewType();
    public int getViewHolderType(int position) {
        return super.getItemViewType(position);
    }

    //加了final防止被重写，做了一些header和footer的封装
    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == HOLDER_TYPE_HEADER) {
            return new HeaderHolder(mHeaderView);
        }
        if (viewType == HOLDER_TYPE_FOOTER) {
            return new FooterHolder(mFooterView);
        }
        return getViewHolder(parent, viewType);
    }

    //加了final防止被重写，做了一些header和footer的封装
    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (mContext == null) {
            mContext = holder.itemView.getContext();
        }

        if (holder instanceof HeaderHolder || holder instanceof FooterHolder) {
            return;
        }
        if (mHeaderView != null) {
            bindDataToViewHolder((VH) holder, position - 1);
        } else {
            bindDataToViewHolder((VH) holder, position);
        }
    }

    //加了final防止被重写，做了一些header和footer的封装
    @Override
    public final void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof HeaderHolder || holder instanceof FooterHolder) {
            return;
        }
        onViewHolderRecycled((VH) holder);
    }

    //加了final防止被重写，做了一些header和footer的封装
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

    //加了final防止被重写，做了一些header和footer的封装
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

    //设置headerView
    public void setHeaderView(View headerView) {
        if (mHeaderView != headerView) {
            notifyDataSetChanged();
        }
        mHeaderView = headerView;
    }

    //设置FooterView
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

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemRangeInsertedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeInserted(positionStart, itemCount);
        } else {
            this.notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemRangeRemovedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeRemoved(positionStart, itemCount);
        } else {
            this.notifyItemRangeRemoved(positionStart + 1, itemCount);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemMovedEx(int fromPosition, int toPosition) {
        if (mHeaderView == null) {
            this.notifyItemMoved(fromPosition, toPosition);
        } else {
            this.notifyItemMoved(fromPosition + 1, toPosition);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemRangeChangedEx(int positionStart, int itemCount) {
        if (mHeaderView == null) {
            this.notifyItemRangeChanged(positionStart, itemCount);
        } else {
            this.notifyItemRangeChanged(positionStart + 1, itemCount);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemRangeChangedEx(int positionStart, int itemCount, Object payload) {
        if (mHeaderView == null) {
            this.notifyItemRangeChanged(positionStart, itemCount, payload);
        } else {
            this.notifyItemRangeChanged(positionStart + 1, itemCount, payload);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemInsertedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemInserted(position);
        } else {
            this.notifyItemInserted(position + 1);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemChangedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemChanged(position);
        } else {
            this.notifyItemChanged(position + 1);
        }
    }

    //局部刷新，要根据是否有header或footer来判断，因为加了之后位置可能有变化
    public final void notifyItemRemovedEx(int position) {
        if (mHeaderView == null) {
            this.notifyItemRemoved(position);
        } else {
            this.notifyItemRemoved(position + 1);
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class HeaderHolder extends BaseViewHolder {

        private HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FooterHolder extends BaseViewHolder {

        private FooterHolder(View itemView) {
            super(itemView);
        }
    }

    //由于扩展了局部刷新，ListUpdateCallback也要扩展一下
    public static class ListUpdateCallback implements androidx.recyclerview.widget.ListUpdateCallback {

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
