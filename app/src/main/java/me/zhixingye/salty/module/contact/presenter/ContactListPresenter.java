package me.zhixingye.salty.module.contact.presenter;


import me.zhixingye.salty.module.contact.contract.ContactListContract;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月07日.
 */
public class ContactListPresenter implements ContactListContract.Presenter {

    private ContactListContract.View mView;

    @Override
    public void attachView(ContactListContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loadNotifyMessageUnreadCount() {

    }

    @Override
    public void loadAllContact() {

    }

    @Override
    public void loadTagCount() {

    }
}
