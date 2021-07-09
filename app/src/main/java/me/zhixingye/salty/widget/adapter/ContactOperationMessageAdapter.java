package me.zhixingye.salty.widget.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.salty.protos.ContactOperationMessage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactOperationMessageHolder;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */
public class ContactOperationMessageAdapter extends BasicListAdapterAdapter<ContactOperationMessage, ContactOperationMessageHolder> {


    private final ContactOperationMessageHolder.DataAdapter mDataAdapter;
    private OnContactOperationClickListener mOnContactOperationClickListener;

    public ContactOperationMessageAdapter(@NonNull ContactOperationMessageHolder.DataAdapter dataAdapter) {
        super(generateComparatorsCallback());
        mDataAdapter = dataAdapter;
    }

    @NonNull
    @Override
    public ContactOperationMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactOperationMessageHolder(parent, mDataAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactOperationMessageHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.setOnClickAcceptListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactOperationClickListener != null) {
                    mOnContactOperationClickListener.onClickAccept(position, getItem(position));
                }
            }
        });

        holder.setOnClickRejectListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactOperationClickListener != null) {
                    mOnContactOperationClickListener.onClickRefused(position, getItem(position));
                }
            }
        });
    }

    public void setOnContactOperationClickListener(OnContactOperationClickListener listener) {
        mOnContactOperationClickListener = listener;
    }

    private static DiffUtil.ItemCallback<ContactOperationMessage> generateComparatorsCallback() {
        return new DiffUtil.ItemCallback<ContactOperationMessage>() {
            @Override
            public boolean areItemsTheSame(@NonNull ContactOperationMessage oldItem, @NonNull ContactOperationMessage newItem) {
                return TextUtils.equals(oldItem.getCommon().getMessageId(), newItem.getCommon().getMessageId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ContactOperationMessage oldItem, @NonNull ContactOperationMessage newItem) {
                return true;
            }
        };
    }

    public interface OnContactOperationClickListener {
        void onClickAccept(int position, ContactOperationMessage message);

        void onClickRefused(int position, ContactOperationMessage message);
    }


}
