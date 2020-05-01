package me.zhixingye.im.api;

import com.salty.protos.AcceptContactReq;
import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactServiceGrpc;
import com.salty.protos.DeleteContactReq;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactReq;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactReq;
import com.salty.protos.RequestContactResp;

import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactApi extends BasicApi {

    private ContactServiceGrpc.ContactServiceStub mContactServiceStub;

    public ContactApi(ManagedChannel channel) {
        mContactServiceStub = ContactServiceGrpc.newStub(channel);
    }

    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        RequestContactReq req = RequestContactReq.newBuilder()
                .setUserId(userId)
                .setReason(reason)
                .build();

        mContactServiceStub.requestContact(
                createReq(req),
                new DefaultStreamObserver<>(RequestContactResp.getDefaultInstance(), callback));
    }

    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        RefusedContactReq req = RefusedContactReq.newBuilder()
                .setUserId(userId)
                .setReason(reason)
                .build();

        mContactServiceStub.refusedContact(
                createReq(req),
                new DefaultStreamObserver<>(RefusedContactResp.getDefaultInstance(), callback));
    }

    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        AcceptContactReq req = AcceptContactReq.newBuilder()
                .setUserId(userId)
                .build();

        mContactServiceStub.acceptContact(
                createReq(req),
                new DefaultStreamObserver<>(AcceptContactResp.getDefaultInstance(), callback));
    }

    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        DeleteContactReq req = DeleteContactReq.newBuilder()
                .setUserId(userId)
                .build();

        mContactServiceStub.deleteContact(
                createReq(req),
                new DefaultStreamObserver<>(DeleteContactResp.getDefaultInstance(), callback));
    }
}
