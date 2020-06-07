package me.zhixingye.salty.widget.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.salty.protos.ContactProfile;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.basic.BasicRecyclerViewAdapter;
import me.zhixingye.salty.tool.UserDataFormatter;
import me.zhixingye.salty.widget.adapter.holder.ContactHolder;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ContactAdapter extends BasicRecyclerViewAdapter<ContactProfile, ContactHolder> {

    private SparseArray<String> mIdentitySparseArray;

    public ContactAdapter() {
        this(false);
    }

    public ContactAdapter(boolean isSearchMode) {
        mIdentitySparseArray = new SparseArray<>(32);
        if (!isSearchMode) {
            registerAdapterDataObserver(mDataObserver);
        }
    }

    @Override
    protected ContactHolder getViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(parent);
    }

    @Override
    public int getViewHolderType(int position) {
        return 0;
    }

    @Override
    public int getViewHolderCount() {
        return mAsyncListDiffer.getCurrentList().size();
    }

    @Override
    public ContactProfile getItemData(int position) {
        return mAsyncListDiffer.getCurrentList().get(position);
    }

    public void submitList(List<ContactProfile> contactList) {
        mAsyncListDiffer.submitList(contactList);
    }

    public List<ContactProfile> getContactList() {
        return mAsyncListDiffer.getCurrentList();
    }

    public int findPositionByLetter(String letter) {
        int keyIndex = mIdentitySparseArray.indexOfValue(letter);
        if (keyIndex != -1) {
            return mIdentitySparseArray.keyAt(keyIndex);
        }
        return -1;
    }

    private void resetLetter() {
        mIdentitySparseArray.clear();
        List<ContactProfile> contactList = mAsyncListDiffer.getCurrentList();
        if (contactList.size() != 0) {
            String identity;
            String abbreviation;
            String currentIdentity = null;
            for (int i = 0, length = contactList.size(); i < length; i++) {
                abbreviation = UserDataFormatter.getAbbreviation(contactList.get(i));
                if (!TextUtils.isEmpty(abbreviation)) {
                    identity = abbreviation.substring(0, 1);
                    if (!identity.equals(currentIdentity)) {
                        mIdentitySparseArray.append(i, identity.toUpperCase().intern());
                        currentIdentity = identity;
                    }
                }
            }
        }
    }


    private final AsyncListDiffer<ContactProfile> mAsyncListDiffer = new AsyncListDiffer<>(
            new BasicRecyclerViewAdapter.ListUpdateCallback(this),
            new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<ContactProfile>() {
                @Override
                public boolean areItemsTheSame(@NonNull ContactProfile oldItem, @NonNull ContactProfile newItem) {
                    return TextUtils.equals(oldItem.getUserProfile().getUserId(), newItem.getUserProfile().getUserId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull ContactProfile oldItem, @NonNull ContactProfile newItem) {
                    return false;
                }

            }).build());

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            resetLetter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            resetLetter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            resetLetter();
        }

    };

}
