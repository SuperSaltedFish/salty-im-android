package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.salty.protos.ContactPushMessage;

import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.tool.OverflowMenuShowHelper;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.adapter.ContactPushMessageAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactPushMessageHolder;
import me.zhixingye.salty.widget.listener.OnRecyclerViewItemClickListener;
import me.zhixingye.salty.widget.view.DividerItemDecoration;
import me.zhixingye.salty.widget.view.OverflowPopupMenu;

/**
 * Created by YZX on 2018年01月18日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */

public class ContactPushMessageFragment extends BasicFragment {

    private RecyclerView mRecyclerView;
    private OverflowPopupMenu mContactOperationMenu;
    private ContactPushMessageAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_push_message;
    }

    @Override
    protected void init(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.mRvContactPushMessage);
        mContactOperationMenu = new OverflowPopupMenu(mContext);
        mAdapter = new ContactPushMessageAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setupRecyclerViewAndAdapter();
        setOverflowMenu();
    }

    private void setupRecyclerViewAndAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.dividerColor), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(mOnRecyclerViewItemClickListener);
        ((DefaultItemAnimator) (Objects.requireNonNull(mRecyclerView.getItemAnimator()))).setSupportsChangeAnimations(false);

        mAdapter.setOnClickListener(new ContactPushMessageHolder.OnClickListener() {
            @Override
            public void onClickAccept(int position) {

            }

            @Override
            public void onClickRefused(int position) {

            }

            @Override
            public void onClickItem(int position) {

            }
        });
    }


    private void setOverflowMenu() {
        mContactOperationMenu.setWidth((int) AndroidHelper.dip2px(128));
        mContactOperationMenu.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mContactOperationMenu.setElevation(AndroidHelper.dip2px(2));
        mContactOperationMenu.inflate(R.menu.menu_contact_message_overflow);
        mContactOperationMenu.setOnMenuItemClickListener(new OverflowPopupMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, int menuID) {
                ContactPushMessage message = (ContactPushMessage) mContactOperationMenu.getParam();
                if (message == null) {
                    return;
                }
                if (menuID == R.id.menu_delete) {

                }
            }
        });
    }

    private final OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(int position, RecyclerView.ViewHolder viewHolder, float touchX, float touchY) {

        }

        @Override
        public void onItemLongClick(int position, RecyclerView.ViewHolder viewHolder, float touchX, float touchY) {
            mContactOperationMenu.setParam(mAdapter.getItem(position));
            OverflowMenuShowHelper.show(viewHolder.itemView, mContactOperationMenu, mRecyclerView.getHeight(), (int) touchX, (int) touchY);
        }
    };

}
