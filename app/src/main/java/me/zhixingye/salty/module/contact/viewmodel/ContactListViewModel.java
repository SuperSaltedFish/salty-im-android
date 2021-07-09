package me.zhixingye.salty.module.contact.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.ContactProfile;
import com.salty.protos.GetContactsResp;

import java.util.List;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.widget.listener.MVVMCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年02月04日.
 */
public class ContactListViewModel extends BasicViewModel {
    private final MutableLiveData<List<ContactProfile>> mContactListData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mContactListLoadingStateData = new MutableLiveData<>();

    public void loadAllContact() {
        IMClient.get().getContactService().getContacts(new MVVMCallback<GetContactsResp>(this) {
            @Override
            protected void setShowLoading(boolean isShow) {
                postValue(mContactListLoadingStateData, isShow);
            }

            @Override
            protected void onSucceed(GetContactsResp result) {
                postValue(mContactListData, result.getContactsList());
            }
        });
    }

    public LiveData<List<ContactProfile>> getContactListData() {
        return mContactListData;
    }

    public LiveData<Boolean> getContactListLoadingStateData() {
        return mContactListLoadingStateData;
    }
}
