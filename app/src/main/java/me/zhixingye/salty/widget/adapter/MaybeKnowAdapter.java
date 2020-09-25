package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.salty.basic.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.MaybeKnowHolder;

/**
 * Created by YZX on 2017年11月21日.
 * 每一个不曾起舞的日子,都是对生命的辜负.
 */


public class MaybeKnowAdapter extends BasicListAdapterAdapter<Object, MaybeKnowHolder> {

    public MaybeKnowAdapter() {
        super(new DiffUtil.ItemCallback<Object>() {
            @Override
            public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public MaybeKnowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MaybeKnowHolder(parent);
    }
}
