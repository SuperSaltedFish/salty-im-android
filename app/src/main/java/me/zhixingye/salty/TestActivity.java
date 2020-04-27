package me.zhixingye.salty;

import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        File s = new File(getApplicationInfo().nativeLibraryDir,"libImSDK.so");
        try {
            s.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
