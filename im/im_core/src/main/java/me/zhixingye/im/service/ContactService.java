package me.zhixingye.im.service;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface ContactService extends BasicService {
    void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback);

    void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback);

    void acceptContact(String userId, RequestCallback<AcceptContactResp> callback);

    void deleteContact(String userId, RequestCallback<DeleteContactResp> callback);
}
