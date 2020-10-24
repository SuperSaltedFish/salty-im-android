package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.salty.protos.Conversation;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ConversationHolder;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月24日.
 */
public class ConversationAdapter extends BasicListAdapterAdapter<Conversation, ConversationHolder> {

    public ConversationAdapter() {
        super(generateComparatorsCallback());
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    private static DiffUtil.ItemCallback<Conversation> generateComparatorsCallback() {
        return new DiffUtil.ItemCallback<Conversation>() {
            @Override
            public boolean areItemsTheSame(@NonNull Conversation oldItem,
                                           @NonNull Conversation newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Conversation oldItem,
                                              @NonNull Conversation newItem) {
                return false;
            }
        };
    }
}
