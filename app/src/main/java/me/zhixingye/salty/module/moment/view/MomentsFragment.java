package me.zhixingye.salty.module.moment.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.adapter.MomentsAdapter;
import me.zhixingye.salty.widget.listener.ImageAutoLoadScrollListener;
import me.zhixingye.salty.widget.view.SpacesItemDecoration;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class MomentsFragment extends BasicFragment {
    public static final String TAG = "MomentsFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MomentsAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_moments;
    }

    @Override
    protected void init(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.MomentsFragment_mRecyclerView);
        mSwipeRefreshLayout = parentView.findViewById(R.id.MomentsFragment_mSwipeRefreshLayout);
//        mAdapter = new MomentsAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
//            }
//        });
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) AndroidHelper.dip2px(12), SpacesItemDecoration.VERTICAL, true, true));
    }
}
