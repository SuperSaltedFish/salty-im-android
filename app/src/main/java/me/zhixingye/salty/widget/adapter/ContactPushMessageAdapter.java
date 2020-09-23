package me.zhixingye.salty.widget.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.salty.protos.ContactPushMessage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.salty.basic.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactPushMessageHolder;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */


public class ContactPushMessageAdapter extends BasicListAdapterAdapter<ContactPushMessage, ContactPushMessageHolder> {

    private ContactPushMessageHolder.OnClickListener mOnClickListener;

    public ContactPushMessageAdapter() {
        super(generateComparatorsCallback());
    }

    @NonNull
    @Override
    public ContactPushMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactPushMessageHolder holder = new ContactPushMessageHolder(parent);
        holder.setOnClickListener(mProxyClickListener);
        return holder;
    }

    public void setOnClickListener(ContactPushMessageHolder.OnClickListener listener) {
        mOnClickListener = listener;
    }

    private static DiffUtil.ItemCallback<ContactPushMessage> generateComparatorsCallback() {
        return new DiffUtil.ItemCallback<ContactPushMessage>() {
            @Override
            public boolean areItemsTheSame(@NonNull ContactPushMessage oldItem, @NonNull ContactPushMessage newItem) {
                return TextUtils.equals(oldItem.getCommon().getMessageID(), newItem.getCommon().getMessageID());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ContactPushMessage oldItem, @NonNull ContactPushMessage newItem) {
                return true;
            }
        };
    }

    private final ContactPushMessageHolder.OnClickListener mProxyClickListener = new ContactPushMessageHolder.OnClickListener() {
        @Override
        public void onClickAccept(int position) {
            if (mOnClickListener != null) {
                mOnClickListener.onClickAccept(position);
            }
        }

        @Override
        public void onClickRefused(int position) {
            if (mOnClickListener != null) {
                mOnClickListener.onClickRefused(position);
            }
        }

        @Override
        public void onClickItem(int position) {
            if (mOnClickListener != null) {
                mOnClickListener.onClickItem(position);
            }
        }
    };
}
