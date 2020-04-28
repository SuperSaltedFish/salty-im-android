package me.zhixingye.im.service;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface ContactService extends BasicService {
    void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback);

    void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback);

    void acceptContact(String userId, RequestCallback<AcceptContactResp> callback);

    void deleteContact(String userId, RequestCallback<DeleteContactResp> callback);
}
