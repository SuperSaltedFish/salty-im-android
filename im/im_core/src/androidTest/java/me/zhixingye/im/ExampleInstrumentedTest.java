package me.zhixingye.im;

import android.content.Context;

import com.salty.protos.SMSReq;
import com.salty.protos.SMSResp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import me.zhixingye.im.constant.ErrorCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("me.zhixingye.im.test", appContext.getPackageName());

        IMClient.init(appContext, "111.231.238.209", 9090, "1.0");

        final CountDownLatch latch = new CountDownLatch(1);

        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType("13631232530", SMSReq.CodeType.REGISTER, new RequestCallback<SMSResp>() {
            @Override
            public void onCompleted(SMSResp response) {
                Logger.e(TAG, response.toString());
            }

            @Override
            public void onFailure(ErrorCode code) {
                Logger.e(TAG, code.toString());
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
