package me.zhixingye.salty.basic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicListAdapterAdapter<D, VH extends BasicListAdapterAdapter.BasicViewHolder<D>>
        extends ListAdapter<D, VH> {

    protected Context mContext;

    protected BasicListAdapterAdapter(@NonNull DiffUtil.ItemCallback<D> diffCallback) {
        super(diffCallback);
    }

    protected BasicListAdapterAdapter(@NonNull AsyncDifferConfig<D> config) {
        super(config);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mContext == null) {
            mContext = holder.itemView.getContext();
        }
        holder.onBindData(getItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }

    @Override
    public D getItem(int position) {
        return super.getItem(position);
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
}
