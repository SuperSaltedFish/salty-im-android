package me.zhixingye.salty.module.contact.viewmodel;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.GetContactOperationMessageListResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.UserProfile;

import java.util.List;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.salty.widget.listener.MVVMCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月8日.
 */
public class ContactOperationListViewModel extends BasicViewModel {

    private final MutableLiveData<List<ContactOperationMessage>> mContactOperationMessageListData = new MutableLiveData<>();
    private final MutableLiveData<ContactOperationMessage> mNeedUpdateContactOperationMessage = new MutableLiveData<>();
    private final MutableLiveData<ContactOperationMessage> mNeedDeleteContactOperationMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadAllContactOperationMessageLoading = new MutableLiveData<>();

    public ContactOperationListViewModel() {
        IMClient.get().getContactService().addOnContactOperationChangeListener(mOnContactOperationChangeListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        IMClient.get().getContactService().removeOnContactOperationChangeListener(mOnContactOperationChangeListener);
    }


    public void loadAllAndMakeAllAsRead() {

    }

    public void loadAllContactOperationMessage() {
        IMClient.get().getContactService().getContactOperationList(1, new MVVMCallback<GetContactOperationMessageListResp>(this) {
            @Override
            protected void setShowLoading(boolean isShow) {
                postValue(mLoadAllContactOperationMessageLoading, isShow);
            }

            @Override
            protected void onSucceed(GetContactOperationMessageListResp result) {
                postValue(mContactOperationMessageListData, result.getMessageListList());
            }
        });
    }

    public void acceptContactRequest(ContactOperationMessage message) {
        IMClient.get().getContactService().acceptContact(message.getTriggerProfile().getUserId(), new MVVMCallback<AcceptContactResp>(this) {
            @Override
            protected void onSucceed(AcceptContactResp result) {

            }
        });
    }

    public void refusedContactRequest(ContactOperationMessage message) {
        IMClient.get().getContactService().refusedContact(message.getTriggerProfile().getUserId(), "", new MVVMCallback<RefusedContactResp>(this) {
            @Override
            protected void onSucceed(RefusedContactResp result) {

            }
        });
    }

    public void removeContactOperation(ContactOperationMessage message) {

    }

    @Nullable
    public ContactProfile getContactBy(String userID) {
        return null;
    }

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

    private final ContactService.OnContactOperationChangeListener mOnContactOperationChangeListener = new ContactService.OnContactOperationChangeListener() {
        @Override
        public void onContactOperationChange(ContactOperationMessage message, @ChangeType int type) {
            switch (type) {
                case ContactService.OnContactOperationChangeListener.CHANGE_TYPE_DELETE:
                    postValue(mNeedDeleteContactOperationMessage, message);
                    break;
                case ContactService.OnContactOperationChangeListener.CHANGE_TYPE_UPDATE:
                    postValue(mNeedUpdateContactOperationMessage, message);
                    break;
                default:
                    loadAllContactOperationMessage();
                    break;
            }
        }
    };

    public LiveData<List<ContactOperationMessage>> getContactOperationMessageListData() {
        return mContactOperationMessageListData;
    }

    public LiveData<ContactOperationMessage> getNeedUpdateContactOperationMessage() {
        return mNeedUpdateContactOperationMessage;
    }

    public LiveData<ContactOperationMessage> getNeedDeleteContactOperationMessage() {
        return mNeedDeleteContactOperationMessage;
    }

    public LiveData<Boolean> getLoadAllContactOperationMessageLoading() {
        return mLoadAllContactOperationMessageLoading;
    }

}
