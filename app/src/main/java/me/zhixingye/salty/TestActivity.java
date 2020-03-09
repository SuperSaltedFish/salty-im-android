package me.zhixingye.salty;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextInputLayout s = findViewById(R.id.sss);
        s.setEnabled(false);
    }
}
