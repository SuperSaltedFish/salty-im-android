package me.zhixingye.salty.module.contact.view;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.widget.adapter.SystemMessageAdapter;
import me.zhixingye.salty.widget.view.DividerItemDecoration;

/**
 * Created by YZX on 2018年05月25日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SystemMessageFragment extends BasicFragment {

    private RecyclerView mRecyclerView;
    private SystemMessageAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_system_message;
    }

    @Override
    protected void init(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.SystemMessageFragment_mRecyclerView);
        mAdapter = new SystemMessageAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.dividerColor),DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        ((DefaultItemAnimator) (mRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

    }
}
