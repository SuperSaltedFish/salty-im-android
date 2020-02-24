package me.zhixingye.im;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.MessageLite;
import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.RegisterResp;

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

    private static final String TAG = "ExampleInstrumentedTest";

    private static boolean isContinue = true;
    private volatile static boolean isLock = false;
    private static final Object sLock = new Object();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        IMCore.init(appContext, "111.231.238.209", 9090, "1.0");

        startTest();
        Log.e(TAG, "测试结束");
    }


    private void startTest() {

        testSMSReq();
        testRegisterReq();
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

    private static String mAccount;

    private void testSMSReq() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder("1");
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        mAccount = builder.toString();
        IMCore.get().getSMSService().obtainVerificationCodeForTelephoneType(
                mAccount,
                ObtainSMSCodeReq.CodeType.REGISTER,
                new LockRequestCallback<ObtainSMSCodeResp>() {
                    @Override
                    public void onSuccessful(ObtainSMSCodeResp resp) {


                    }
                });
    }

    private String mPassword;

    private void testRegisterReq() {
        mPassword = "123";
        IMCore.get().getUserService().registerByTelephone(
                mAccount,
                mPassword,
                "123456",
                new LockRequestCallback<RegisterResp>() {
                    @Override
                    public void onSuccessful(RegisterResp resp) {

                    }
                });
    }

    private void testLoginReq() {
        IMCore.get().getUserService().loginByTelephone(
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