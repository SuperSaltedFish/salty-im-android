package me.zhixingye.salty.module.contact.presenter;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.GetContactOperationMessageListResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.contact.contract.ContactOperationListContract;
import me.zhixingye.salty.widget.listener.LifecycleRequestCallback;

/**
 * Created by YZX on 2018年01月20日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactOperationListPresenter implements ContactOperationListContract.Presenter {

    private ContactOperationListContract.View mView;

    @Override
    public void attachView(ContactOperationListContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loadAllContactOperationMessage() {
        IMClient.get().getContactService().getContactOperationMessageList(0, new LifecycleRequestCallback<GetContactOperationMessageListResp>(mView) {
            @Override
            protected void onPreRequest() {
                super.onPreRequest();
                mView.setRefreshing(true);
            }

            @Override
            protected void onSuccess(GetContactOperationMessageListResp result) {
                mView.showContactOperation(result.getMessageListList());
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                mView.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadAllAndMakeAllAsRead() {

    }

    @Override
    public void acceptContactRequest(ContactOperationMessage message) {
        IMClient.get().getContactService().acceptContact(message.getTriggerProfile().getUserId(), new LifecycleRequestCallback<AcceptContactResp>(mView) {
            @Override
            protected void onSuccess(AcceptContactResp result) {

            }
        });
    }

    @Override
    public void refusedContactRequest(ContactOperationMessage message) {
        IMClient.get().getContactService().refusedContact(message.getTriggerProfile().getUserId(), "", new LifecycleRequestCallback<RefusedContactResp>(mView) {
            @Override
            protected void onSuccess(RefusedContactResp result) {

            }
        });
    }

    @Override
    public void removeContactOperation(ContactOperationMessage message) {

    }

    @Nullable
    @Override
    public ContactProfile getContactBy(String userID) {
        return null;
    }

    @Override
    public boolean isMySelf(UserProfile profile) {
        if (profile == null) {
            return false;
        }
        UserProfile mySelf = IMClient.get().getUserService().getCurrentUserProfile();
        if (mySelf == null) {
            return false;
        }
        return TextUtils.equals(profile.getUserId(), mySelf.getUserId());
    }
}
