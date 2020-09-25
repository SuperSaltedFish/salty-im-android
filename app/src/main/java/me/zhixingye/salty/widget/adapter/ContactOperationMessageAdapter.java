package me.zhixingye.salty.widget.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.salty.protos.ContactOperationMessage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.salty.basic.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactOperationMessageHolder;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */


public class ContactOperationMessageAdapter extends BasicListAdapterAdapter<ContactOperationMessage, ContactOperationMessageHolder> {

    private ContactOperationMessageHolder.OnClickListener mOnClickListener;

    public ContactOperationMessageAdapter() {
        super(generateComparatorsCallback());
    }

    @NonNull
    @Override
    public ContactOperationMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactOperationMessageHolder holder = new ContactOperationMessageHolder(parent);
        holder.setOnClickListener(mProxyClickListener);
        return holder;
    }

    public void setOnClickListener(ContactOperationMessageHolder.OnClickListener listener) {
        mOnClickListener = listener;
    }

    private static DiffUtil.ItemCallback<ContactOperationMessage> generateComparatorsCallback() {
        return new DiffUtil.ItemCallback<ContactOperationMessage>() {
            @Override
            public boolean areItemsTheSame(@NonNull ContactOperationMessage oldItem, @NonNull ContactOperationMessage newItem) {
                return TextUtils.equals(oldItem.getCommon().getMessageID(), newItem.getCommon().getMessageID());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ContactOperationMessage oldItem, @NonNull ContactOperationMessage newItem) {
                return true;
            }
        };
    }

    private final ContactOperationMessageHolder.OnClickListener mProxyClickListener = new ContactOperationMessageHolder.OnClickListener() {
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
