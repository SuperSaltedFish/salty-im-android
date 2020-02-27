package me.zhixingye.im.manager.impl;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.ContactManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactManagerImpl implements ContactManager {

    private ContactApi mContactApi;

    public ContactManagerImpl(String userId, ContactApi contactApi) {
        super();
        mContactApi =contactApi;
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        mContactApi.requestContact(userId, reason, callback);
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        mContactApi.refusedContact(userId, reason, callback);
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        mContactApi.acceptContact(userId, callback);
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        mContactApi.deleteContact(userId, callback);
    }


}
