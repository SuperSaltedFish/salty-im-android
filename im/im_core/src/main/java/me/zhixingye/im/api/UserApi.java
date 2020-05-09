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

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.Sha256Util;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserApi extends BasicApi {

    private static final String PASSWORD_SALTY = "salty";

    private UserServiceGrpc.UserServiceStub mUserServiceStub;

    @Override
    public void onBindManagedChannel(ManagedChannel channel) {
        mUserServiceStub = UserServiceGrpc.newStub(channel)
                .withDeadlineAfter(30, TimeUnit.SECONDS);
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

        mUserServiceStub.register(
                createReq(req),
                new DefaultStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
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

        mUserServiceStub.register(
                createReq(req),
                new DefaultStreamObserver<>(RegisterResp.getDefaultInstance(), callback));
    }

    public void loginByTelephone(String telephone, String password, String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setTelephone(telephone)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();
        mUserServiceStub.login(
                createReq(req),
                new DefaultStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void loginByEmail(String email, String password, String verificationCode, RequestCallback<LoginResp> callback) {
        LoginReq req = LoginReq.newBuilder()
                .setEmail(email)
                .setPassword(Sha256Util.sha256WithSalt(password, PASSWORD_SALTY))
                .setVerificationCode(verificationCode)
                .build();

        mUserServiceStub.login(
                createReq(req),
                new DefaultStreamObserver<>(LoginResp.getDefaultInstance(), callback));
    }

    public void logout(RequestCallback<LogoutResp> callback) {
        LogoutReq req = LogoutReq.newBuilder()
                .build();
        mUserServiceStub.login(
                createReq(req),
                new DefaultStreamObserver<>(LogoutResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByTelephoneSMS(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setTelephone(telephone)
                .setVerificationCode(verificationCode)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(
                createReq(req),
                new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByEmailSMS(String email, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setEmail(email)
                .setVerificationCode(verificationCode)
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(
                createReq(req),
                new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByTelephoneOldPassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setTelephone(telephone)
                .setOldPassword(Sha256Util.sha256WithSalt(oldPassword, PASSWORD_SALTY))
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(
                createReq(req),
                new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
    }

    public void resetLoginPasswordByEmailOldPassword(String email, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ResetPasswordReq req = ResetPasswordReq.newBuilder()
                .setEmail(email)
                .setOldPassword(Sha256Util.sha256WithSalt(oldPassword, PASSWORD_SALTY))
                .setNewPassword(Sha256Util.sha256WithSalt(newPassword, PASSWORD_SALTY))
                .build();

        mUserServiceStub.resetPassword(
                createReq(req),
                new DefaultStreamObserver<>(ResetPasswordResp.getDefaultInstance(), callback));
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

        mUserServiceStub.updateUserInfo(
                createReq(req),
                new DefaultStreamObserver<>(UpdateUserInfoResp.getDefaultInstance(), callback));
    }

    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        GetUserInfoReq req = GetUserInfoReq.newBuilder()
                .setUserId(userId)
                .build();

        mUserServiceStub.getUserInfo(
                createReq(req),
                new DefaultStreamObserver<>(GetUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setTelephone(telephone)
                .build();

        mUserServiceStub.queryUserInfo(
                createReq(req),
                new DefaultStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }

    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        QueryUserInfoReq req = QueryUserInfoReq.newBuilder()
                .setEmail(email)
                .build();

        mUserServiceStub.queryUserInfo(
                createReq(req),
                new DefaultStreamObserver<>(QueryUserInfoResp.getDefaultInstance(), callback));
    }

}