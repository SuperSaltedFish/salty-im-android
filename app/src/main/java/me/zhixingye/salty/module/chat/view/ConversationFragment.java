package me.zhixingye.salty.module.chat.view;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import me.zhixingye.base.component.BasicFragment;
import me.zhixingye.base.component.mvp.MVPBasicFragment;
import me.zhixingye.base.view.DividerItemDecoration;
import me.zhixingye.salty.R;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.ViewUtil;
import me.zhixingye.salty.widget.adapter.ConversationAdapter;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ConversationFragment extends MVPBasicFragment {

    private Toolbar mDefaultToolbar;
    private ImageView mIvEmptyHintImage;
    private TextView mITvEmptyHintText;
    private RecyclerView mRecyclerView;

    private ConversationAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void init(View rootView) {
        mDefaultToolbar = rootView.findViewById(R.id.mDefaultToolbar);
        mIvEmptyHintImage = rootView.findViewById(R.id.mIvEmptyHintImage);
        mITvEmptyHintText = rootView.findViewById(R.id.mITvEmptyHintText);
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);

        mAdapter = new ConversationAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        mDefaultToolbar.setTitle(R.string.app_name);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(mContext, R.anim.anim_fade_in_layout));
        mRecyclerView.addItemDecoration(new DividerItemDecoration((int) AndroidHelper.dip2px(1), ContextCompat.getColor(mContext, R.color.dividerColor), DividerItemDecoration.HORIZONTAL));
        ViewUtil.setSupportsCancelChangeAnimations(mRecyclerView, false);

    }
}
