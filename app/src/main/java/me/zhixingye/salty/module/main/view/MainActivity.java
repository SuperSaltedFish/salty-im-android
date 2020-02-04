package me.zhixingye.salty.module.main.view;

import android.os.Bundle;
import android.view.View;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import androidx.appcompat.app.AppCompatActivity;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IMClient.init(this, new IMClient.InitCallback() {
            @Override
            public void onCompleted() {

            }
        });
    }

    public void onClick(View v) {

        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType("12312321", ObtainSMSCodeReq.CodeType.REGISTER, new RequestCallback<ObtainSMSCodeResp>() {
            @Override
            public void onCompleted(ObtainSMSCodeResp response) {
                Logger.e("dawdwad","dwadawdwad");
            }

            @Override
            public void onFailure(int code, String error) {
                Logger.e("dawdwad","dwadawdwad");
            }
        });
    }
}
