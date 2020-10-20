package me.zhixingye.salty.widget.adapter;

import android.view.ViewGroup;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactListHeaderHolder;

import static me.zhixingye.salty.widget.adapter.holder.ContactListHeaderHolder.*;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年09月22日.
 */
public class ContactListHeaderAdapter extends BasicListAdapterAdapter<Type, ContactListHeaderHolder> {

    public ContactListHeaderAdapter() {
        super(generateComparatorsCallback());
        submitList(Arrays.asList(Type.values()));
    }

    @NonNull
    @Override
    public ContactListHeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactListHeaderHolder(parent);
    }

    private static DiffUtil.ItemCallback<Type> generateComparatorsCallback() {
        return new DiffUtil.ItemCallback<Type>() {
            @Override
            public boolean areItemsTheSame(@NonNull Type oldItem, @NonNull Type newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Type oldItem, @NonNull Type newItem) {
                return oldItem.equals(newItem);
            }
        };
    }
}
