package me.zhixingye.im.service;

import com.salty.protos.LoginReq;
import com.salty.protos.LoginResp;
import com.salty.protos.RegisterReq;
import com.salty.protos.RegisterResp;
import com.salty.protos.UserProfile;
import com.salty.protos.UserServiceGrpc;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.util.Sha256Util;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserService extends BasicService {

    private static final String PASSWORD_SALTY = "salty";

    private UserServiceGrpc.UserServiceStub mUserServiceStub;

    public UserService() {
        super();
        mUserServiceStub = UserServiceGrpc.newStub(getChannel());
    }

    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setTelephone(telephone)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setUserProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .setRegisterType(RegisterReq.RegisterType.TELEPHONE)
                .build();

        mUserServiceStub.register(createReq(req), new DefaultStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setTelephone(telephone)
                .setLoginType(LoginReq.LoginType.TELEPHONE)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.login(createReq(req), new DefaultStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }
}