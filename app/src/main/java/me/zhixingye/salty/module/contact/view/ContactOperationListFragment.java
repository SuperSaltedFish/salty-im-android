package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import me.zhixingye.base.component.mvp.MVPBasicFragment;
import me.zhixingye.base.listener.OnRecyclerViewItemClickListener;
import me.zhixingye.base.view.DividerItemDecoration;
import me.zhixingye.base.view.OverflowPopupMenu;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.contract.ContactOperationListContract;
import me.zhixingye.salty.tool.OverflowMenuShowHelper;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.adapter.ContactOperationMessageAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactOperationMessageHolder;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */
public class ContactOperationListFragment
        extends MVPBasicFragment
        implements ContactOperationListContract.View {

    private SwipeRefreshLayout mSrlContactOperation;
    private RecyclerView mRvContactOperation;
    private OverflowPopupMenu mContactOperationMenu;
    private ContactOperationMessageAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_operation_list;
    }

    @Override
    protected void init(View rootView) {
        mSrlContactOperation = (SwipeRefreshLayout) rootView.findViewById(R.id.mSrlContactOperation);
        mRvContactOperation = (RecyclerView) rootView.findViewById(R.id.mRvContactOperation);
        mContactOperationMenu = new OverflowPopupMenu(mContext);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setupSwipeRefreshLayout();
        setupRecyclerViewAndAdapter();
        setOverflowMenu();

        loadData();
    }

    private void setupSwipeRefreshLayout() {
        mSrlContactOperation.setOnRefreshListener(this::loadData);
        mSrlContactOperation.setColorSchemeResources(R.color.colorAccent);
    }

    private void setupRecyclerViewAndAdapter() {
        mRvContactOperation.setLayoutManager(new LinearLayoutManager(mContext));
        mRvContactOperation.addItemDecoration(new DividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.dividerColor), DividerItemDecoration.HORIZONTAL));
        mRvContactOperation.setHasFixedSize(true);

        mAdapter = new ContactOperationMessageAdapter(profile -> getPresenter().isMySelf(profile));
        mAdapter.setOnClickListener(new ContactOperationMessageHolder.OnClickListener() {
            @Override
            public void onClickAccept(int position) {
                getPresenter().acceptContactRequest(mAdapter.getItem(position));
            }

            @Override
            public void onClickRefused(int position) {
                getPresenter().refusedContactRequest(mAdapter.getItem(position));
            }

            @Override
            public void onClickItem(int position) {

            }

            @Override
            public void onLongClickItem(int position, View itemView, int touchX, int touchY) {
                mContactOperationMenu.setParam(mAdapter.getItem(position));
                OverflowMenuShowHelper.show(
                        itemView,
                        mContactOperationMenu,
                        mRvContactOperation.getHeight(),
                        touchX,
                        touchY);
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

    private void loadData() {
        getPresenter().loadAllContactOperationMessage();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSrlContactOperation.post(() -> mSrlContactOperation.setRefreshing(refreshing));
    }

    @Override
    public void showContactOperation(List<ContactOperationMessage> messageList) {
        mSrlContactOperation.setRefreshing(false);
        mSrlContactOperation.setEnabled(false);
        mAdapter.submitList(messageList);
    }
}
