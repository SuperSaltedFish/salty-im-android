package me.zhixingye.salty.module.contact.presenter;


import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.contact.contract.StrangerProfileContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * Created by YZX on 2018年01月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class StrangerProfilePresenter implements StrangerProfileContract.Presenter {

    private StrangerProfileContract.View mView;

    @Override
    public void attachView(StrangerProfileContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public UserProfile getUserProfile(String userId) {
        return IMClient.get().getUserService().;
    }

    @Override
    public ContactOperationMessage getContactOperationMessage(String messageId) {
        return null;
    }

    @Override
    public void requestAddContact(String userId, String addReason) {
        IMClient.get().getContactService().requestContact(userId, addReason, new LifecycleMVPRequestCallback<RequestContactResp>(mView) {
            @Override
            protected void onSuccess(RequestContactResp result) {

            }
        });
    }

    @Override
    public void acceptAddContact(String userId) {
        IMClient.get().getContactService().acceptContact(userId, new LifecycleMVPRequestCallback<AcceptContactResp>(mView) {
            @Override
            protected void onSuccess(AcceptContactResp result) {

            }
        });
    }

    @Override
    public void refusedAddContact(String userId, String rejectReason) {
        IMClient.get().getContactService().refusedContact(userId, rejectReason, new LifecycleMVPRequestCallback<RefusedContactResp>(mView) {
            @Override
            protected void onSuccess(RefusedContactResp result) {

            }
        });
    }
}
