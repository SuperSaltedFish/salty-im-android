package me.zhixingye.salty;

import android.os.Bundle;
import android.view.View;

import com.salty.protos.SMSReq;
import com.salty.protos.SMSResp;

import java.util.concurrent.CountDownLatch;

import androidx.appcompat.app.AppCompatActivity;
import me.zhixingye.im.IMClient;
import me.zhixingye.im.constant.ErrorCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.Logger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v){
        IMClient.init(this, "111.231.238.209", 9090, "1.0");

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
