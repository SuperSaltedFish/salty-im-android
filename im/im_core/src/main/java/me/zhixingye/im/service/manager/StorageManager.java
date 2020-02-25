package me.zhixingye.im.service.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.security.Key;

import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.AESUtil;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageManager implements StorageService {

    private static final String TAG = "StorageService";

    private static final String DEFAULT_STORAGE_NAME = "Storage";

    private static final String AES_KET_ALIAS = "AES_Salty";

    private SharedPreferences mConfigurationPreferences;
    private SharedPreferences mUserPreferences;
    private Key mAESKey;

    //初始化过程中传入了storageName
    public StorageManager(Context appContent) {
        this(appContent, DEFAULT_STORAGE_NAME);
    }

    //初始化过程中传入了storageName
    public StorageManager(Context appContent, String storageName) {
        mConfigurationPreferences = appContent.getSharedPreferences(storageName, Context.MODE_PRIVATE);
//        mAESKey = AESUtil.generateAESKeyInAndroidKeyStore(AES_KET_ALIAS, 192);
        mAESKey = AESUtil.generateAESKey(192);
        if (mAESKey == null) {
            Logger.w(TAG, "generateAESKeyInAndroidKeyStore fail");
            mAESKey = AESUtil.generateAESKey(192);
        }
    }

    //初始化用户的Preferences，也就是说每个用户有自己的Preferences
    void initUserPreferences(Context context, String userID) {
        mUserPreferences = context.getSharedPreferences(userID, Context.MODE_PRIVATE);
    }

    //put一个数据到ConfigurationPreferences
    @Override
    public boolean putToConfigurationPreferences(String key, String value) {
//        if (!TextUtils.isEmpty(value) && mAESKey != null) {
//            byte[] data = AESUtil.encrypt(value.getBytes(Charset.defaultCharset()), mAESKey, null);
//            if (data != null && data.length > 0) {
//                value = Base64Util.encodeToString(data);
//            } else {
//                Logger.w(TAG, "Encrypt fail,key=" + key);
//                return false;
//            }
//        }
        return mConfigurationPreferences.edit().putString(key, value).commit();
    }

    //get一个数据，从ConfigurationPreferences
    @Override
    public String getFromConfigurationPreferences(String key) {
        String value = mConfigurationPreferences.getString(key, null);
//        if (!TextUtils.isEmpty(value) && mAESKey != null) {
//            byte[] data = AESUtil.decrypt(Base64Util.decode(value), mAESKey, null);
//            if (data != null && data.length > 0) {
//                value =  new String(data, Charset.defaultCharset());
//            }
//        }
        return value;
    }

    //put一个数据到UserPreferences
    @Override
    public boolean putToUserPreferences(String key, String value) {
//        if (!TextUtils.isEmpty(value) && mAESKey != null) {
//            byte[] data = AESUtil.encrypt(value.getBytes(Charset.defaultCharset()), mAESKey, null);
//            if (data != null && data.length > 0) {
//                value = Base64Util.encodeToString(data);
//            } else {
//                Logger.w(TAG, "Encrypt fail,key=" + key);
//                return false;
//            }
//        }
        return mUserPreferences.edit().putString(key, value).commit();
    }

    //get一个数据，从UserPreferences
    @Override
    public String getFromUserPreferences(String key) {
        if (mUserPreferences == null) {
            return null;
        }
        String value = mUserPreferences.getString(key, null);
//        if (!TextUtils.isEmpty(value) && mAESKey != null) {
//            byte[] data = AESUtil.decrypt(Base64Util.decode(value), mAESKey, null);
//            if (data != null && data.length > 0) {
//                value = new String(data, Charset.defaultCharset());
//            }
//        }
        return value;
    }

    public void destroy() {

    }
}
