package me.zhixingye.salty.module.contact.presenter;


import com.salty.protos.QueryUserInfoResp;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.contact.contract.FindNewContactContract;
import me.zhixingye.salty.widget.listener.LifecycleRequestCallback;

/**
 * Created by YZX on 2017年11月27日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */


public class FindNewContactPresenter implements FindNewContactContract.Presenter {

    private FindNewContactContract.View mView;

    @Override
    public void attachView(FindNewContactContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
    }


    @Override
    public void searchUser(String nicknameOrTelephone) {
        IMClient.get().getUserService().queryUserInfoByTelephone(nicknameOrTelephone, new LifecycleRequestCallback<QueryUserInfoResp>(mView) {
            @Override
            protected void onSuccess(QueryUserInfoResp result) {
                if(result.hasProfile()){
                    mView.showStrangerProfile(result.getProfile());
                }else {
                    mView.showSearchNotExist();
                }
            }
        });
    }
}
