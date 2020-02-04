package me.zhixingye.im.service.impl;

import com.salty.protos.LoginReq;
import com.salty.protos.LoginResp;
import com.salty.protos.RegisterReq;
import com.salty.protos.RegisterResp;
import com.salty.protos.UserProfile;
import com.salty.protos.UserServiceGrpc;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.util.Sha256Util;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserServiceImpl extends BasicService implements UserService {

    private static final String PASSWORD_SALTY = "salty";

    private UserServiceGrpc.UserServiceStub mUserServiceStub;

    public UserServiceImpl() {
        super();
        mUserServiceStub = UserServiceGrpc.newStub(getChannel());
    }

    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setTelephone(telephone)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.register(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setTelephone(telephone)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.login(createReq(req), new DefaultStreamObserver<>(callback));
    }

    public void destroy() {

    }
}