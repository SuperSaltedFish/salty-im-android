package me.zhixingye.im.util;

import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

import javax.crypto.KeyAgreement;

/**
 * Created by YZX on 2018年12月13日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ECDHUtil {

    private static final String ECDH = "ECDH";

    public static byte[] ecdh(PrivateKey localKey, Key remoteKey) {
        return ecdh(localKey, remoteKey,null);
    }

    public static byte[] ecdh(PrivateKey localKey, Key remoteKey, String provider) {
        if (localKey == null || remoteKey == null) {
            return null;
        }
        KeyAgreement agreement;
        try {
            if (TextUtils.isEmpty(provider)) {
                agreement = KeyAgreement.getInstance(ECDH);
            } else {
                agreement = KeyAgreement.getInstance(ECDH, provider);
            }
            agreement.init(localKey);
            agreement.doPhase(remoteKey, true);
            return agreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }
}
