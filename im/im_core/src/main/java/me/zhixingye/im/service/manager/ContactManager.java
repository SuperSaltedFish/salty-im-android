package me.zhixingye.im.service.manager;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.impl.ContactServiceImpl;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactManager implements ContactService {

    private ContactService mService;

    public ContactManager() {
        super();
        mService = new ContactServiceImpl();
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        mService.requestContact(userId, reason, callback);
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        mService.refusedContact(userId, reason, callback);
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        mService.acceptContact(userId, callback);
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        mService.deleteContact(userId, callback);
    }

    public void destroy() {

    }
}
