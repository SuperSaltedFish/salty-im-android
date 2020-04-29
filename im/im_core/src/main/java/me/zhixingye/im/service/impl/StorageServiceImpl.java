package me.zhixingye.im.service.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.nio.charset.Charset;
import java.security.Key;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.AESUtil;
import me.zhixingye.im.util.Base64Util;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceImpl implements StorageService {

    private static final String TAG = StorageServiceImpl.class.getName() + ".StorageService";

    private static final String AES_KET_ALIAS = "AES_Salty";

    private SharedPreferences mConfigurationPreferences;
    private Key mAESKey;

    //初始化过程中传入了storageName
    public StorageServiceImpl() {
        mConfigurationPreferences = IMCore.get().getAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mAESKey = AESUtil.generateAESKeyInAndroidKeyStore(AES_KET_ALIAS, 192);
        if (mAESKey == null) {
            Logger.w(TAG, "generateAESKeyInAndroidKeyStore fail");
            mAESKey = AESUtil.generateAESKey(192);
        }
    }


    //put一个数据到ConfigurationPreferences
    public boolean putToConfigurationPreferences(String key, String value) {
        if (!TextUtils.isEmpty(value) && mAESKey != null) {
            byte[] data = AESUtil.encrypt(value.getBytes(Charset.defaultCharset()), mAESKey, null);
            if (data != null && data.length > 0) {
                value = Base64Util.encodeToString(data);
            } else {
                Logger.w(TAG, "Encrypt fail,key=" + key);
                return false;
            }
        }
        return mConfigurationPreferences.edit().putString(key, value).commit();
    }

    //get一个数据，从ConfigurationPreferences
    public String getFromConfigurationPreferences(String key) {
        String value = mConfigurationPreferences.getString(key, null);
        if (!TextUtils.isEmpty(value) && mAESKey != null) {
            byte[] data = AESUtil.decrypt(Base64Util.decode(value), mAESKey, null);
            if (data != null && data.length > 0) {
                value = new String(data, Charset.defaultCharset());
            }
        }
        return value;
    }

    //put一个数据到UserPreferences
    public boolean putToUserPreferences(String key, String value) {
        String userId = ServiceAccessor.get(AccountService.class).getCurrentUserId();
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        SharedPreferences p = IMCore.get().getAppContext().getSharedPreferences(userId, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(value) && mAESKey != null) {
            byte[] data = AESUtil.encrypt(value.getBytes(Charset.defaultCharset()), mAESKey, null);
            if (data != null && data.length > 0) {
                value = Base64Util.encodeToString(data);
            } else {
                Logger.w(TAG, "Encrypt fail,key=" + key);
                return false;
            }
        }
        return p.edit().putString(key, value).commit();
    }

    //get一个数据，从UserPreferences
    public String getFromUserPreferences(String key) {
        String userId = ServiceAccessor.get(AccountService.class).getCurrentUserId();
        if (TextUtils.isEmpty(userId)) {
            return "";
        }
        SharedPreferences p = IMCore.get().getAppContext().getSharedPreferences(userId, Context.MODE_PRIVATE);
        String value = p.getString(key, null);
        if (!TextUtils.isEmpty(value) && mAESKey != null) {
            byte[] data = AESUtil.decrypt(Base64Util.decode(value), mAESKey, null);
            if (data != null && data.length > 0) {
                value = new String(data, Charset.defaultCharset());
            }
        }
        return value;
    }

    public void clear() {

    }
}
