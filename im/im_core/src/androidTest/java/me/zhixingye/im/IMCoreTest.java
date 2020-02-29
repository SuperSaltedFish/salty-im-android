package me.zhixingye.im;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import me.zhixingye.im.listener.RequestCallback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IMCoreTest {

    private static final String TAG = "IMCoreTest";

    private static boolean isContinue = true;
    private volatile static boolean isLock = false;
    private static final Object sLock = new Object();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        IMCore.tryInit(appContext, "111.231.238.209", 9090, "1.0");

        startTest();
        Log.e(TAG, "测试结束");
    }

    private String mAccount;
    private String mPassword;

    private void startTest() {

        testRegisterReq();
        testResetLoginPasswordByVerificationCode();
        testResetLoginPasswordByOldPassword();
        testLoginReq();

        SystemClock.sleep(1000);

        testUpdateUserInfoReq();

        while (isLock) {
            synchronized (sLock) {
                try {
                    sLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void testRegisterReq() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder("1");
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        mAccount = builder.toString();
        IMCore.get().obtainTelephoneSMSCode(
                mAccount,
                ObtainSMSCodeReq.CodeType.REGISTER,
                new LockRequestCallback<Void>() {
                    @Override
                    public void onSuccessful(Void resp) {


                    }
                });

        mPassword = "123";
        IMCore.get().registerByTelephone(
                mAccount,
                mPassword,
                "123456",
                new LockRequestCallback<RegisterResp>() {
                    @Override
                    public void onSuccessful(RegisterResp resp) {

                    }
                });
    }

    private void testResetLoginPasswordByOldPassword() {
        String newPassword = "141";
        IMCore.get().resetLoginPasswordByTelephonePassword(mAccount, mPassword, newPassword, new LockRequestCallback<ResetPasswordResp>() {
            @Override
            void onSuccessful(ResetPasswordResp resp) {
                mPassword = newPassword;
            }
        });
        mPassword = newPassword;
    }

    private void testResetLoginPasswordByVerificationCode() {
        IMCore.get().obtainTelephoneSMSCode(
                mAccount,
                ObtainSMSCodeReq.CodeType.RESET_PASSWORD,
                new LockRequestCallback<Void>() {
                    @Override
                    public void onSuccessful(Void resp) {


                    }
                });

        mPassword = "yezhixing";
        IMCore.get().resetLoginPasswordByTelephoneSMS(
                mAccount,
                "112233",
                mPassword,
                new LockRequestCallback<ResetPasswordResp>() {
                    @Override
                    void onSuccessful(ResetPasswordResp resp) {

                    }
                });
    }

    private void testLoginReq() {
        IMCore.get().loginByTelephone(
                mAccount,
                mPassword,
                "",
                new LockRequestCallback<LoginResp>() {
                    @Override
                    public void onSuccessful(LoginResp resp) {
                        assertTrue(resp.hasProfile());
                        assertNotNull(resp.getProfile().getUserId());
                        assertEquals(resp.getProfile().getTelephone(), mAccount);
                    }
                });
    }

    private void testUpdateUserInfoReq() {
        String nickname = "zhixingye";
        String description = "天才星";
        UserProfile.Sex sex = UserProfile.Sex.FEMALE;
        long birthday = 10086;
        String location = "地球";
        IMCore.get().getUserManager().updateUserInfo(nickname, description, sex, birthday, location, new LockRequestCallback<UpdateUserInfoResp>() {
            @Override
            void onSuccessful(UpdateUserInfoResp resp) {

            }
        });

        UserProfile profile = IMCore.get().getUserManager().getUserProfile();
        IMCore.get().getUserManager().getUserInfoByUserId(profile.getUserId(), new LockRequestCallback<GetUserInfoResp>() {
            @Override
            void onSuccessful(GetUserInfoResp resp) {
                UserProfile profile = resp.getProfile();
                assertEquals(profile.getNickname(), nickname);
                assertEquals(profile.getDescription(), description);
                assertEquals(profile.getSexValue(), sex.getNumber());
                assertEquals(profile.getBirthday(), birthday);
                assertEquals(profile.getLocation(), location);
            }
        });

        IMCore.get().getUserManager().queryUserInfoByTelephone(profile.getTelephone(), new LockRequestCallback<QueryUserInfoResp>() {
            @Override
            void onSuccessful(QueryUserInfoResp resp) {
                UserProfile profile = resp.getProfile();
                assertEquals(profile.getNickname(), nickname);
                assertEquals(profile.getDescription(), description);
                assertEquals(profile.getSexValue(), sex.getNumber());
                assertEquals(profile.getBirthday(), birthday);
                assertEquals(profile.getLocation(), location);
            }
        });
    }

    private static abstract class LockRequestCallback<T> implements RequestCallback<T> {

        abstract void onSuccessful(T resp);

        LockRequestCallback() {
            while (isLock) {
                synchronized (sLock) {
                    try {
                        sLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            assertTrue(isContinue);
            isLock = true;
        }

        @Override
        public void onCompleted(T response) {
            onSuccessful(response);
            synchronized (sLock) {
                isLock = false;
                sLock.notify();
            }
        }

        @Override
        public void onFailure(int code, String error) {
            isContinue = false;
            synchronized (sLock) {
                isLock = false;
                sLock.notify();
            }
        }
    }

}
