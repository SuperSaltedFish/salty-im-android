package me.zhixingye.im.api;

import com.salty.protos.GetUserInfoReq;
import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginReq;
import com.salty.protos.LoginResp;
import com.salty.protos.LogoutReq;
import com.salty.protos.LogoutResp;
import com.salty.protos.QueryUserInfoReq;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.RegisterReq;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordReq;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoReq;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;
import com.salty.protos.UserServiceGrpc;

import androidx.annotation.Nullable;
import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.util.Sha256Util;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserApi extends BasicApi {

    private static final String PASSWORD_SALTY = "salty";

    private UserServiceGrpc.UserServiceStub mUserServiceStub;

    public UserApi(ManagedChannel channel, ApiServiceImpl.Adapter adapter) {
        super(adapter);
        mUserServiceStub = UserServiceGrpc.newStub(channel);
    }

    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setTelephone(telephone)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.register(createReq(req), new DefaultStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void registerByEmail(String email, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setEmail(email)
                .build();

        RegisterReq req = RegisterReq.newBuilder()
                .setProfile(profile)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.register(createReq(req), new DefaultStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setTelephone(telephone)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.login(createReq(req), new DefaultStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void loginByEmail(String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setEmail(email)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.login(createReq(req), new DefaultStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void logout(RequestCallback<LogoutResp> callback) {
        LogoutReq req = LogoutReq.newBuilder()
                .build();
        mUserServiceStub.login(createReq(req), new DefaultStreamObserver<>(LogoutResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByTelephoneSMS(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setTelephone(telephone)
                .setVerificationCode(verificationCode)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(createReq(req), new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByEmailSMS(String email, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setEmail(email)
                .setVerificationCode(verificationCode)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(createReq(req), new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByTelephonePassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setTelephone(telephone)
                .setOldPassword(Sha256Util.sha256WithSalt(oldPassword, PASSWORD_SALTY))
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(createReq(req), new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByEmailPassword(String email, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setEmail(email)
                .setOldPassword(Sha256Util.sha256WithSalt(oldPassword, PASSWORD_SALTY))
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(createReq(req), new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        UserProfile profile = UserProfile.newBuilder()
                .setNickname(nickname)
                .setDescription(description)
                .setSex(sex)
                .setBirthday(birthday)
                .setLocation(location)
                .build();

        UpdateUserInfoReq req = UpdateUserInfoReq.newBuilder()
                .setProfile(profile)
                .build();

        mUserServiceStub.updateUserInfo(createReq(req), new DefaultStreamObserver<>(UpdateUserInfoResp.getDefaultInstance(), callback));
    }

    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        GetUserInfoReq req = GetUserInfoReq.newBuilder()
                .setUserId(userId)
                .build();

        mUserServiceStub.getUserInfo(createReq(req), new DefaultStreamObserver<>(GetUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setTelephone(telephone)
                .build();

        mUserServiceStub.queryUserInfo(createReq(req), new DefaultStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setEmail(email)
                .build();

        mUserServiceStub.queryUserInfo(createReq(req), new DefaultStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }
}