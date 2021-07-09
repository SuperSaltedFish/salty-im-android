package me.zhixingye.salty.widget.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.base.view.BadgeView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.view.PushMessageActivity;
import me.zhixingye.salty.util.GlideUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年09月22日.
 */
public class ContactListHeaderHolder extends BasicListAdapterAdapter.BasicViewHolder<ContactListHeaderHolder.Type> {

    public enum Type {
        SYSTEM_NOTIFICATION,
        GROUP,
        CONTACT_LABEL
    }

    private ImageView mIvIcon;
    private TextView mTvTitle;
    private BadgeView mBadgeView;


    public ContactListHeaderHolder(ViewGroup parent) {
        super(R.layout.item_contact_list_header, parent);
    }

    @Override
    protected void onCreateItemView(View itemView) {
        mIvIcon = itemView.findViewById(R.id.mIvIcon);
        mTvTitle = itemView.findViewById(R.id.mTvTitle);
        mBadgeView = itemView.findViewById(R.id.mBadgeView);
    }

    @Override
    protected void onBindData(Type data) {
        switch (data) {
            case SYSTEM_NOTIFICATION:
                bindSystemNotificationType();
                break;
            case GROUP:
                bindGroupType();
                break;
            case CONTACT_LABEL:
                bindContactLabelType();
                break;
            default:
                return;
        }
    }

    @Override
    protected void onRecycled() {

    }

    private void bindSystemNotificationType() {
        GlideUtil.loadAvatarFromUrl(mContext, mIvIcon, R.drawable.ic_contact_notification);
        mTvTitle.setText("通知信息");
    }

    private void bindGroupType() {
        GlideUtil.loadAvatarFromUrl(mContext, mIvIcon, R.drawable.ic_contact_group);
        mTvTitle.setText("我的群聊");
    }

    private void bindContactLabelType() {
        GlideUtil.loadAvatarFromUrl(mContext, mIvIcon, R.drawable.ic_contact_label);
        mTvTitle.setText("我的标签");
    }
}
