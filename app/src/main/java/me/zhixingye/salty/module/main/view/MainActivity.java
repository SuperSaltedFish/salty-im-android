package me.zhixingye.salty.module.main.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import me.zhixingye.im.sdk.IMClient;
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

    }
}
