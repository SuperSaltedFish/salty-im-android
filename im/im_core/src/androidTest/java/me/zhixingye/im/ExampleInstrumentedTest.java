package me.zhixingye.im;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.google.protobuf.MessageLite;
import com.salty.protos.RegisterResp;
import com.salty.protos.SMSReq;
import com.salty.protos.SMSResp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.Semaphore;

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

    private static Semaphore sSemaphore = new Semaphore(1);
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
        String account = builder.toString();


//        testSMSReq(account);
        testRegisterReq(account);

        SystemClock.sleep(300000);
    }

    private void testSMSReq(String account) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                account,
                SMSReq.CodeType.REGISTER,
                new DefaultRequestCallback<SMSResp>());
    }


    private void testRegisterReq(String account) {
        IMClient.get().getUserService().registerByTelephone(
                account,
                "yezhixing123",
                "123456",
                new DefaultRequestCallback<RegisterResp>());
    }

    private static class DefaultRequestCallback<T extends MessageLite> implements RequestCallback<T> {

        DefaultRequestCallback() {
            sSemaphore.acquireUninterruptibly();
            assertTrue(isContinue);
        }

        @Override
        public void onCompleted(T response) {
            Logger.e(TAG, response.toString());
            sSemaphore.release();
        }

        @Override
        public void onFailure(ErrorCode code) {
            Logger.e(TAG, code.toString());
            isContinue = false;
            sSemaphore.release();
        }
    }
}
