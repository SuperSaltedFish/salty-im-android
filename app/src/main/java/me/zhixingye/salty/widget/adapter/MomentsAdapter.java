package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;

import me.zhixingye.salty.basic.BasicRecyclerViewAdapter;
import me.zhixingye.salty.widget.adapter.holder.MomentsHolder;

/**
 * Created by YZX on 2017年08月12日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public class MomentsAdapter extends BasicRecyclerViewAdapter<Integer, MomentsHolder> {

    @Override
    public MomentsHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MomentsHolder(parent);
    }

    @Override
    public int getViewHolderType(int position) {
        return 0;
    }

    @Override
    public int getViewHolderCount() {
        return 10;
    }

    @Override
    public Integer getItemData(int position) {
        return position;
    }

}
