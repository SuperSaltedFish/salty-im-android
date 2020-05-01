package me.zhixingye.im.util;

import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

import javax.crypto.KeyAgreement;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
