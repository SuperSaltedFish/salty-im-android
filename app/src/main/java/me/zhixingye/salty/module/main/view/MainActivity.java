package me.zhixingye.salty.module.main.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import androidx.appcompat.app.AppCompatActivity;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.R;
import me.zhixingye.salty.tool.AppUpdateHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpgradeInfo info = Beta.getUpgradeInfo();
        if (info != null) {
            showUpdateDialog(info);
        }
    }

    private void showUpdateDialog(UpgradeInfo upgradeInfo) {
        AppUpdateHelper.showUpdateDialog(this, upgradeInfo);
    }

    public void onClick(View v) {
        IMClient.get().getAccountService().logout();
        finish();
    }
}
