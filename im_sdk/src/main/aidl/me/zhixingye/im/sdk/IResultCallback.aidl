// IResultCallback.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.RemoteResultWrap;

interface IResultCallback {

    void onCompleted(in RemoteResultWrap result);

    void onFailure(int code,String error);
}
