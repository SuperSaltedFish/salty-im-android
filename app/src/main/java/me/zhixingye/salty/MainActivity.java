package me.zhixingye.salty;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import me.zhixingye.im.sdk.IMClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v) {
        IMClient.init(this, new IMClient.InitCallback() {
            @Override
            public void onCompleted() {
            }
        });
    }
}
