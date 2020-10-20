package me.zhixingye.salty.widget.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.R;
import me.zhixingye.salty.util.GlideUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年09月23日.
 */
public class MaybeKnowHolder extends BasicListAdapterAdapter.BasicViewHolder<Object> {

    private ImageView mIvAvatar;

    public MaybeKnowHolder(ViewGroup parent) {
        super(R.layout.item_maybe_know, parent);
    }

    @Override
    protected void onCreateItemView(View itemView) {
        mIvAvatar = itemView.findViewById(R.id.MaybeKnowAdapter_mIvAvatar);
    }

    @Override
    protected void onBindData(Object data) {
        GlideUtil.loadAvatarFromUrl(mContext, mIvAvatar, R.drawable.temp_head_image);
    }

    @Override
    protected void onRecycled() {
        GlideUtil.clear(mContext,mIvAvatar);
    }
}
