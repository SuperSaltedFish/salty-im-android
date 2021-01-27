package me.zhixingye.salty.manager;

import android.content.Context;
import android.content.Intent;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.service.LoginService;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.SaltyApp;
import me.zhixingye.salty.module.login.view.LoginActivity;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月26日.
 */
public class LoginManager {

    private static final String TAG = "LoginManager";

    public static void observeLoginState() {
        IMClient.get().getLoginService().addOnLoginListener(new LoginService.OnLoginListener() {
            @Override
            public void onLoggedOut() {
                Logger.i(TAG,"onLoggedOut");
                Context context = SaltyApp.getAppContext();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onLoggedIn() {
                Logger.i(TAG,"onLoggedIn");
            }

            @Override
            public void onLoginExpired() {
                Logger.i(TAG,"onLoginExpired");
                onLoggedOut();
            }
        });
    }
}
