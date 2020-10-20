package me.zhixingye.salty.widget.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil.ItemCallback;

import com.salty.protos.ContactProfile;

import androidx.annotation.NonNull;
import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactListHolder;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ContactListAdapter extends BasicListAdapterAdapter<ContactProfile, ContactListHolder> {

    public ContactListAdapter() {
        super(generateComparatorsCallback());
    }

    @NonNull
    @Override
    public ContactListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactListHolder(parent);
    }

    private static ItemCallback<ContactProfile> generateComparatorsCallback() {
        return new ItemCallback<ContactProfile>() {
            @Override
            public boolean areItemsTheSame(@NonNull ContactProfile oldItem,
                                           @NonNull ContactProfile newItem) {
                return TextUtils.equals(oldItem.getUserProfile().getUserId(),
                        newItem.getUserProfile().getUserId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ContactProfile oldItem,
                                              @NonNull ContactProfile newItem) {
                return false;
            }
        };
    }
}
