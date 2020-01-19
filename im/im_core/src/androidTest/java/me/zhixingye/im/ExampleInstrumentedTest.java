package me.zhixingye.im;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.protobuf.MessageLite;
import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.SMSReq;
import com.salty.protos.SMSResp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import me.zhixingye.im.constant.ErrorCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.Logger;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    private static CountDownLatch sLatch = new CountDownLatch(1);
    private static boolean isContinue = true;

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        IMClient.init(appContext, "111.231.238.209", 9090, "1.0");

        startTest();
        Log.e(TAG, "测试结束");
    }


    private void startTest() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("1");
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        sAccount = builder.toString();


        testSMSReq();
        testRegisterReq();
        testLoginReq();
    }

    private static String sAccount;

    private void testSMSReq() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("1");
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        sAccount = builder.toString();
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                sAccount,
                SMSReq.CodeType.REGISTER,
                new LockRequestCallback<>(new SimpleRequestCallback<SMSResp>() {
                    @Override
                    public void onCompleted(SMSResp response) {

                    }
                }));
    }

    private String sPassword;

    private void testRegisterReq() {
        sPassword = "yezhixing123";
        IMClient.get().getUserService().registerByTelephone(
                sAccount,
                sPassword,
                "123456",
                new LockRequestCallback<>(new SimpleRequestCallback<RegisterResp>() {
                    @Override
                    public void onCompleted(RegisterResp response) {

                    }
                }));
    }

    private void testLoginReq() {
        IMClient.get().getUserService().loginByTelephone(
                sAccount,
                sPassword,
                "",
                new LockRequestCallback<LoginResp>(new SimpleRequestCallback<LoginResp>() {
                    @Override
                    public void onCompleted(LoginResp response) {

                    }
                }));
    }

    private static class LockRequestCallback<T extends MessageLite> implements RequestCallback<T> {

        private static Handler mHandler = new Handler(Looper.getMainLooper());

        private RequestCallback<T> mCallback;

        LockRequestCallback(RequestCallback<T> callback) {
            assertTrue(isContinue);
            mCallback = callback;
            sLatch = new CountDownLatch(1);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        sLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onCompleted(T response) {
            Logger.e(TAG, response.toString());
            if (mCallback != null) {
                mCallback.onCompleted(response);
            }
            sLatch.countDown();
        }

        @Override
        public void onFailure(ErrorCode code) {
            Logger.e(TAG, code.toString());
            isContinue = false;
            if (mCallback != null) {
                mCallback.onFailure(code);
            }
            sLatch.countDown();

        }
    }

    private static abstract class SimpleRequestCallback<T extends MessageLite> implements RequestCallback<T> {
        @Override
        public void onFailure(ErrorCode code) {

        }
    }
}
