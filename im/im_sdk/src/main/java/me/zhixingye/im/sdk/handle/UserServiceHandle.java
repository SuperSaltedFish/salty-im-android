package me.zhixingye.im.sdk.handle;


import android.os.RemoteException;

import me.zhixingye.im.sdk.IRemoteCallback;
import me.zhixingye.im.sdk.IUserServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceHandle extends IUserServiceHandle.Stub {
    @Override
    public void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IRemoteCallback callback)  {

    }

    @Override
    public void getUserInfoByUserId(String userId, IRemoteCallback callback)  {

    }

    @Override
    public void queryUserInfoByTelephone(String telephone, IRemoteCallback callback)  {

    }

    @Override
    public void queryUserInfoByEmail(String email, IRemoteCallback callback)  {

    }
}
