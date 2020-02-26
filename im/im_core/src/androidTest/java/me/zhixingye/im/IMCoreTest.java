package me.zhixingye.im;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.MessageLite;
import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

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
        IMCore.get().getSMSManager().obtainVerificationCodeForTelephoneType(
                mAccount,
                ObtainSMSCodeReq.CodeType.REGISTER,
                new LockRequestCallback<ObtainSMSCodeResp>() {
                    @Override
                    public void onSuccessful(ObtainSMSCodeResp resp) {


                    }
                });

        mPassword = "123";
        IMCore.get().getUserManager().registerByTelephone(
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
        IMCore.get().getUserManager().resetLoginPassword(mAccount, mPassword, newPassword, new LockRequestCallback<ResetPasswordResp>() {
            @Override
            void onSuccessful(ResetPasswordResp resp) {
                mPassword = newPassword;
            }
        });
        mPassword = newPassword;
    }

    private void testResetLoginPasswordByVerificationCode() {
        IMCore.get().getSMSManager().obtainVerificationCodeForTelephoneType(
                mAccount,
                ObtainSMSCodeReq.CodeType.RESET_PASSWORD,
                new LockRequestCallback<ObtainSMSCodeResp>() {
                    @Override
                    public void onSuccessful(ObtainSMSCodeResp resp) {


                    }
                });

        mPassword = "yezhixing";
        IMCore.get().getUserManager().resetLoginPasswordByTelephone(
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
        IMCore.get().getUserManager().loginByTelephone(
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

    private void testUpdateUserInfoReq(){

    }

    private static abstract class LockRequestCallback<T extends MessageLite> implements RequestCallback<T> {

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
