// IRemoteCallback.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IRemoteCallback {

    void onCompleted(in byte[] protoData);

    void onFailure(int code,String error);
}
