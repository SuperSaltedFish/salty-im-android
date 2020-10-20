package me.zhixingye.salty.module.contact.presenter;


import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.MessageCommon;
import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.List;

import me.zhixingye.salty.module.contact.contract.ContactOperationListContract;

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
    public void loadAllAndMakeAllAsRead() {

    }

    @Override
    public List<ContactOperationMessage> getAllContactOperationMessage() {
        List<ContactOperationMessage> messageList = new ArrayList<>();
        ContactOperationMessage message;

        UserProfile profile = UserProfile.newBuilder()
                .setBirthday(System.currentTimeMillis())
                .setEmail("244546875@qq.com")
                .setLocation("广东 韶关 仁化县")
                .setNickname("夜之星")
                .setSex(UserProfile.Sex.FEMALE)
                .setDescription("每个不曾起舞的日子，都是对生命的辜负")
                .setUserId("7758258")
                .build();

        MessageCommon common = MessageCommon.newBuilder()
                .setCreatedTime(System.currentTimeMillis())
                .setMessageID("123123")
                .setSortID("1")
                .setIsNeedRemind(true)
                .build();

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.ACCEPT_ACTIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.ACCEPT_PASSIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.DELETE_ACTIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.DELETE_PASSIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.REJECT_ACTIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.REJECT_PASSIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.REQUEST_ACTIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        message = ContactOperationMessage.newBuilder()
                .setRejectReason("对不起，我并不想加你为好友")
                .setAddReason("你好啊！我可以添加你为好友吗")
                .setTriggerProfile(profile)
                .setType(ContactOperationMessage.OperationType.REQUEST_PASSIVE)
                .setCommon(common)
                .build();
        messageList.add(message);

        return messageList;

    }

    @Override
    public void acceptContactRequest(ContactOperationMessage message) {

    }

    @Override
    public void refusedContactRequest(ContactOperationMessage message) {

    }

    @Override
    public void removeContactOperation(ContactOperationMessage message) {

    }

    @Override
    public ContactProfile getContactBy(String userID) {
        return null;
    }
}
