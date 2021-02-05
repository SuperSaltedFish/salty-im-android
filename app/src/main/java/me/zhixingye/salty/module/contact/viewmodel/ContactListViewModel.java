package me.zhixingye.salty.module.contact.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.salty.protos.ContactProfile;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年02月04日.
 */
public class ContactListViewModel extends BasicViewModel {
    private final MutableLiveData<ContactProfile> mContactProfileListData = new MutableLiveData<>();

//    public void loadAllContact() {
//        IMClient.get().getContactService().
//    }
}
