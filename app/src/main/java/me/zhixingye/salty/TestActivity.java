package me.zhixingye.salty;

import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
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
