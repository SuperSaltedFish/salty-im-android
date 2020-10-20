package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.MomentsHolder;

/**
 * Created by YZX on 2017年08月12日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public class MomentsAdapter extends BasicListAdapterAdapter<Integer, MomentsHolder> {


    protected MomentsAdapter(@NonNull DiffUtil.ItemCallback<Integer> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MomentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MomentsHolder(parent);
    }
}
