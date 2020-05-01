package me.zhixingye.im.service.impl;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.ContactService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ContactServiceImpl implements ContactService {


    public ContactServiceImpl() {
        super();
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .requestContact(userId, reason, callback);
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .refusedContact(userId, reason, callback);
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .acceptContact(userId, callback);
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ContactApi.class)
                .deleteContact(userId, callback);
    }


}
