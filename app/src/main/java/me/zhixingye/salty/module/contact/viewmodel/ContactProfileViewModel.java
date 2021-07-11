package me.zhixingye.salty.module.contact.viewmodel;

import android.text.TextUtils;

import com.salty.protos.ContactProfile;
import com.salty.protos.DeleteContactResp;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.Nullable;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.salty.widget.listener.MVVMCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月11日.
 */
public class ContactProfileViewModel extends BasicViewModel {
    private ContactProfileContract.View mContactProfileView;
    private ContactEntity mContactEntity;
    private AppClient mAppClient;

    @Nullable
    private ContactProfile getContactProfileFromLocal(String contactId) {
        return IMClient.get().getContactService().getContactProfileFromLocal(contactId);
    }

    public void deleteContact(String contactId) {
        IMClient.get().getContactService().deleteContact(contactId, new MVVMCallback<DeleteContactResp>(this, false) {
            @Override
            protected void onSucceed(DeleteContactResp result) {

            }
        });
    }

    @Override
    public ArrayList<String> getAllTags() {
        Set<String> tags = AppClient.getInstance().getContactManager().getAllTags();
        if (tags != null && tags.size() > 0) {
            return new ArrayList<>(tags);
        } else {
            return null;
        }
    }

    @Override
    public void saveRemarkInfo(ContactEntity contact) {
        AppClient.getInstance().getContactManager().updateContactRemark(contact.getContactID(), contact.getRemarkName(), contact.getDescription(), contact.getTelephones(), contact.getTags(), new LifecycleMVPResultCallback<Void>(mContactProfileView) {
            @Override
            protected void onSuccess(Void result) {

            }
        });
    }


    private final ContactService.OnContactChangeListener mOnContactChangeListener = new ContactService.OnContactChangeListener() {
        @Override
        public void onContactProfileChange(ContactProfile profile, @ChangeType int type) {

        }

        @Override
        public void onContactAdded(ContactEntity contact) {

        }

        @Override
        public void onContactDeleted(String contactID) {
            if (TextUtils.equals(mContactEntity.getContactID(), contactID)) {
                mContactProfileView.goBack();
            }
        }

        @Override
        public void onContactUpdate(ContactEntity contact) {
            if (contact.equals(mContactEntity)) {
                mContactEntity = contact;
                mContactProfileView.updateContactInfo(mContactEntity);
            }
        }
    };
}
