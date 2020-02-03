// IResultCallback.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

interface IResultCallback {

    void onCompleted(in byte[] protoData);

    void onFailure(int code,String error);
}
