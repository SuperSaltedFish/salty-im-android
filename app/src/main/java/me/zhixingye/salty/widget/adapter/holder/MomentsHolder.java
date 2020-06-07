package me.zhixingye.salty.widget.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicRecyclerViewAdapter;
import me.zhixingye.salty.util.GlideUtil;
import me.zhixingye.salty.widget.adapter.MomentsNineGridAdapter;
import me.zhixingye.salty.widget.view.NineGridView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月06日.
 */
public class MomentsHolder extends BasicRecyclerViewAdapter.BasicViewHolder<Integer> {
    private NineGridView mNineGridView;
    private ImageView mIvAvatar;

    private List<Object> mImageUrlList = new ArrayList<>(9);
    private MomentsNineGridAdapter mNineGridAdapter;

    public MomentsHolder(ViewGroup parent) {
        super(R.layout.item_moments, parent);
    }

    @Override
    protected void onCreateItemView(View itemView) {
        mNineGridView = itemView.findViewById(R.id.mNineGridImageView);
        mIvAvatar = itemView.findViewById(R.id.mIvAvatar);

        mNineGridAdapter = new MomentsNineGridAdapter(mImageUrlList);
        mNineGridView.setAdapter(mNineGridAdapter);
    }

    @Override
    protected void onBindData(Integer data) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < data; i++) {
            list.add(R.drawable.temp_share_image);
        }

        mImageUrlList.clear();
        mImageUrlList.addAll(list);
        mNineGridAdapter.notifyDataSetChanged();

        GlideUtil.loadAvatarFromUrl(itemView.getContext(), mIvAvatar, R.drawable.temp_head_image);

        if (mImageUrlList.size() == 0) {
            mNineGridView.setVisibility(View.GONE);
        } else {
            mNineGridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRecycled() {
        GlideUtil.clear(mContext, mIvAvatar);
    }
}
