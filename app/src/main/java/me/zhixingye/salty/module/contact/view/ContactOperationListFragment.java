package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.base.component.mvvm.MVVMFragment;
import me.zhixingye.base.view.DividerItemDecoration;
import me.zhixingye.base.view.OverflowPopupMenu;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.viewmodel.ContactOperationListViewModel;
import me.zhixingye.salty.tool.OverflowMenuShowHelper;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.adapter.ContactOperationMessageAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactOperationMessageHolder;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */
public class ContactOperationListFragment extends MVVMFragment {

    private SwipeRefreshLayout mSrlContactOperation;
    private RecyclerView mRvContactOperation;
    private OverflowPopupMenu mContactOperationMenu;
    private ContactOperationMessageAdapter mAdapter;

    private ContactOperationListViewModel mContactOperationListViewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_operation_list;
    }

    @Override
    protected void init(View rootView) {
        mSrlContactOperation = rootView.findViewById(R.id.mSrlContactOperation);
        mRvContactOperation = rootView.findViewById(R.id.mRvContactOperation);
        mContactOperationMenu = new OverflowPopupMenu(mContext);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setupSwipeRefreshLayout();
        setupRecyclerViewAndAdapter();
        setOverflowMenu();

        setupViewModule();
    }

    private void setupSwipeRefreshLayout() {
        mSrlContactOperation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContactOperationListViewModel.loadAllContactOperationMessage();
            }
        });
        mSrlContactOperation.setColorSchemeResources(R.color.colorAccent);
    }

    private void setupRecyclerViewAndAdapter() {
        mRvContactOperation.setLayoutManager(new LinearLayoutManager(mContext));
        mRvContactOperation.addItemDecoration(new DividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.dividerColor), DividerItemDecoration.HORIZONTAL));
        mRvContactOperation.setHasFixedSize(true);

        mAdapter = new ContactOperationMessageAdapter(new ContactOperationMessageHolder.DataAdapter() {
            @Override
            public boolean isMySelf(UserProfile profile) {
                return mContactOperationListViewModel.isMySelf(profile);
            }
        });
        mAdapter.setOnItemClickListener(new BasicListAdapterAdapter.SimpleOnItemClickListener<ContactOperationMessage>() {

            private int lastTouchX;
            private int lastTouchY;

            @Override
            public void onClick(int position, ContactOperationMessage data) {

            }

            @Override
            public boolean onLongClick(int position, ContactOperationMessage data) {
                mContactOperationMenu.setParam(data);
                OverflowMenuShowHelper.show(
                        mRvContactOperation.getRootView(),
                        mContactOperationMenu,
                        mRvContactOperation.getHeight(),
                        lastTouchX,
                        lastTouchY);
                return true;
            }

            @Override
            public boolean onTouchEvent(int position, ContactOperationMessage data, MotionEvent event) {
                lastTouchX = (int) event.getRawX();
                lastTouchY = (int) event.getRawY();
                return false;
            }
        });

        mAdapter.setOnContactOperationClickListener(new ContactOperationMessageAdapter.OnContactOperationClickListener() {
            @Override
            public void onClickAccept(int position, ContactOperationMessage message) {
                mContactOperationListViewModel.acceptContactRequest(mAdapter.getItem(position));
            }

            @Override
            public void onClickRefused(int position, ContactOperationMessage message) {
                mContactOperationListViewModel.refusedContactRequest(mAdapter.getItem(position));
            }
        });
        mRvContactOperation.setAdapter(mAdapter);
    }


    private void setOverflowMenu() {
        mContactOperationMenu.setWidth((int) AndroidHelper.dip2px(128));
        mContactOperationMenu.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mContactOperationMenu.setElevation(AndroidHelper.dip2px(2));
        mContactOperationMenu.inflate(R.menu.menu_contact_message_overflow);
        mContactOperationMenu.setOnMenuItemClickListener((position, menuID) -> {
            ContactOperationMessage message = (ContactOperationMessage) mContactOperationMenu.getParam();
            if (message == null) {
                return;
            }
            if (menuID == R.id.menu_delete) {

            }
        });
    }

    private void setupViewModule() {
        mContactOperationListViewModel = createViewModel(ContactOperationListViewModel.class);

        mContactOperationListViewModel.getContactOperationMessageListData().observe(this, new Observer<List<ContactOperationMessage>>() {
            @Override
            public void onChanged(List<ContactOperationMessage> contactOperationMessageList) {
                mAdapter.submitList(contactOperationMessageList);
                mContactOperationMenu.dismiss();
            }
        });

        mContactOperationListViewModel.getNeedUpdateContactOperationMessage().observe(this, new Observer<ContactOperationMessage>() {
            @Override
            public void onChanged(ContactOperationMessage message) {
                mAdapter.notifyItemChanged(message);
                mContactOperationMenu.dismiss();
            }
        });

        mContactOperationListViewModel.getNeedDeleteContactOperationMessage().observe(this, new Observer<ContactOperationMessage>() {
            @Override
            public void onChanged(ContactOperationMessage message) {
                mAdapter.notifyItemRemoved(message);
                mContactOperationMenu.dismiss();
            }
        });

        mContactOperationListViewModel.getLoadAllContactOperationMessageLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean data) {
                mSrlContactOperation.setRefreshing(Boolean.TRUE.equals(data));
            }
        });

        mContactOperationListViewModel.loadAllContactOperationMessage();
    }
}
