package me.zhixingye.base.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    protected DiffUtil.ItemCallback<D> mDiffItemCallback;
    protected OnItemClickListener<D> mOnItemClickListener;

    protected BasicListAdapterAdapter(@NonNull DiffUtil.ItemCallback<D> diffCallback) {
        super(diffCallback);
        mDiffItemCallback = diffCallback;
    }

    protected BasicListAdapterAdapter(@NonNull AsyncDifferConfig<D> config) {
        super(config);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mContext == null) {
            mContext = holder.itemView.getContext();
        }

        D data = getItem(position);

        holder.onBindData(data);

        holder.setOnHolderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position, data);
                }
            }
        });
        holder.setOnHolderLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onLongClick(position, data);
                }
                return false;
            }
        });
        holder.setOnHolderTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onTouchEvent(position, data, event);
                }
                return false;
            }
        });
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

    public int getPosition(D data) {
        for (int i = 0, size = getItemCount(); i < size; i++) {
            D currentData = getItem(i);
            if (mDiffItemCallback.areItemsTheSame(currentData, data)) {
                return i;
            }
        }
        return -1;
    }

    public void notifyItemChanged(D data) {
        int position = getPosition(data);
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void notifyItemRemoved(D data) {
        int position = getPosition(data);
        if (position >= 0) {
            getCurrentList().remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<D> listener) {
        mOnItemClickListener = listener;
    }

    public abstract static class BasicViewHolder<D> extends RecyclerView.ViewHolder {

        protected Context mContext;

        public BasicViewHolder(@LayoutRes int layoutRes, ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
        }

        public BasicViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            onCreateItemView(itemView);
        }

        protected abstract void onCreateItemView(View itemView);

        protected abstract void onBindData(D data);

        protected abstract void onRecycled();

        protected void setOnHolderClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        protected void setOnHolderLongClickListener(View.OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }

        protected void setOnHolderTouchListener(View.OnTouchListener listener) {
            itemView.setOnTouchListener(listener);
        }
    }

    public interface OnItemClickListener<D> {
        void onClick(int position, D data);

        boolean onLongClick(int position, D data);

        boolean onTouchEvent(int position, D data, MotionEvent event);
    }

    public static abstract class SimpleOnItemClickListener<D> implements OnItemClickListener<D> {

        @Override
        public boolean onLongClick(int position, D data) {
            return false;
        }

        @Override
        public boolean onTouchEvent(int position, D data, MotionEvent event) {
            return false;
        }
    }
}
