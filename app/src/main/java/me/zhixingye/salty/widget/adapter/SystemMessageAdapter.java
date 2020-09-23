package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.salty.basic.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.SystemMessageHolder;

/**
 * Created by YZX on 2018年05月25日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SystemMessageAdapter extends BasicListAdapterAdapter<Void, SystemMessageHolder> {

    public SystemMessageAdapter() {
        super(new DiffUtil.ItemCallback<Void>() {
            @Override
            public boolean areItemsTheSame(@NonNull Void oldItem, @NonNull Void newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Void oldItem, @NonNull Void newItem) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public SystemMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SystemMessageHolder(parent);
    }

}
