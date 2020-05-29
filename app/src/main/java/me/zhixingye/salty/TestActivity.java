package me.zhixingye.salty;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;

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

        final View view = findViewById(R.id.ssssss);
        view.post(new Runnable() {
            @Override
            public void run() {
                String s = "s";
                Log.e("dawdawd", view.toString() + s);
            }
        });

        //方法1
        view.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        //方法2
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });

        //方法3
        view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                return false;
            }
        });

        File s = new File(getApplicationInfo().nativeLibraryDir, "libImSDK.so");
        try {
            s.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
