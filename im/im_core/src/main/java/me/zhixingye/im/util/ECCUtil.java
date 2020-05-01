package me.zhixingye.im.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.security.auth.x500.X500Principal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import me.zhixingye.im.tool.Logger;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ECCUtil {

    private static final String TAG = "ECCUtil";

    private static final String ECC = Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? "EC" : KeyProperties.KEY_ALGORITHM_EC;
    private static final String DEFAULT_DIGEST_ALGORITHM = "SHA1withECDSA";
    private static final String PROVIDER_ANDROID_KEY_STORE = "AndroidKeyStore";
    private static KeyStore sKeyStore;

    @Nullable
    public static KeyPair generateECCKeyPair(String ellipticCurve) {
        return generateECCKeyPair(ellipticCurve, null);
    }

    @Nullable
    public static KeyPair generateECCKeyPair(String ellipticCurve, String provider) {
        try {
            KeyPairGenerator keyPairGenerator;
            if (TextUtils.isEmpty(provider)) {
                keyPairGenerator = KeyPairGenerator.getInstance(ECC);
            } else {
                keyPairGenerator = KeyPairGenerator.getInstance(ECC, provider);
            }
            keyPairGenerator.initialize(new ECGenParameterSpec(ellipticCurve));
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            Logger.d(TAG, e.toString(), e);
            return null;
        }
    }

    private static boolean initAndroidKeyStore() {
        try {
            sKeyStore = KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE);
            sKeyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Logger.d(TAG, e.toString(), e);
            sKeyStore = null;
            return false;
        }
        return true;
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static KeyPair generateECCKeyPairInAndroidKeyStore(Context context, String keyAlias, String ellipticCurve) {
        if (sKeyStore == null && !initAndroidKeyStore()) {
            return null;
        }
        try {
            Certificate mCertificate = sKeyStore.getCertificate(keyAlias);
            PrivateKey privateKey = (PrivateKey) sKeyStore.getKey(keyAlias, null);
            if (mCertificate != null && privateKey != null) {
                return new KeyPair(mCertificate.getPublicKey(), privateKey);
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            Logger.d(TAG, e.toString(), e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return generateECCKeyPairApi23(keyAlias, ellipticCurve);
        } else {
            return generateECCKeyPairApi18(context, keyAlias, ellipticCurve);
        }
    }

    @Nullable
    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static KeyPair generateECCKeyPairApi18(Context context, String keyAlias, String ellipticCurve) {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.YEAR, 10);
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECC, PROVIDER_ANDROID_KEY_STORE);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec(ellipticCurve))
                    .setSubject(new X500Principal("CN=" + keyAlias))
                    .setAlias(keyAlias)
                    .setKeyType(ECC)
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(startTime.getTime())
                    .setEndDate(endTime.getTime())
                    .build();
            keyPairGenerator.initialize(spec);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static KeyPair generateECCKeyPairApi23(String keyAlias, String ellipticCurve) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECC, PROVIDER_ANDROID_KEY_STORE);
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + keyAlias))
                    .setDigests(KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA256)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec(ellipticCurve))
                    .build();
            keyPairGenerator.initialize(spec);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    public static PublicKey loadECPublicKey(byte[] publicKey) {
        return loadECPublicKey(publicKey, null);
    }

    public static PrivateKey loadECPrivateKey(byte[] privateKey) {
        return loadECPrivateKey(privateKey, null);
    }

    public static PublicKey loadECPublicKey(byte[] publicKey, String provider) {
        if (publicKey == null) {
            return null;
        }
        try {
            KeyFactory kf;
            if (TextUtils.isEmpty(provider)) {
                kf = KeyFactory.getInstance(ECC);
            } else {
                kf = KeyFactory.getInstance(ECC, provider);
            }
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
            return kf.generatePublic(x509KeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    public static PrivateKey loadECPrivateKey(byte[] privateKey, String provider) {
        if (privateKey == null) {
            return null;
        }
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            KeyFactory kf;
            if (TextUtils.isEmpty(provider)) {
                kf = KeyFactory.getInstance(ECC);
            } else {
                kf = KeyFactory.getInstance(ECC, provider);
            }
            return kf.generatePrivate(pkcs8KeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] encryptByPublicKey(byte[] data, ECPublicKey publicKey) {
        if (data == null || publicKey == null) {
            return null;
        }
        Cipher cipher = new NullCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, publicKey.getParams());
            return cipher.doFinal(data);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    @Nullable
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) {
        if (data == null || !(privateKey instanceof ECKey)) {
            return null;
        }
        Cipher cipher = new NullCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, privateKey, ((ECKey) privateKey).getParams());
            return cipher.doFinal(data);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    @Nullable
    public static byte[] decryptByPublicKey(byte[] data, ECPublicKey publicKey) {
        if (data == null || publicKey == null) {
            return null;
        }
        Cipher cipher = new NullCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, publicKey, publicKey.getParams());
            return cipher.doFinal(data);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    @Nullable
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) {
        if (data == null || !(privateKey instanceof ECKey)) {
            return null;
        }
        Cipher cipher = new NullCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ((ECKey) privateKey).getParams());
            return cipher.doFinal(data);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    public static Signature loadSignature(PrivateKey privateKey) {
        return loadSignature(privateKey, null);
    }

    public static Signature loadSignature(PrivateKey privateKey, String provider) {
        Signature signature;
        try {
            if (TextUtils.isEmpty(provider)) {
                signature = Signature.getInstance(DEFAULT_DIGEST_ALGORITHM);
            } else {
                signature = Signature.getInstance(DEFAULT_DIGEST_ALGORITHM, provider);
            }
            signature.initSign(privateKey);
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    public static Signature loadSignature(PublicKey publicKey) {
        return loadSignature(publicKey, null);
    }

    public static Signature loadSignature(PublicKey publicKey, String provider) {
        Signature signature;
        try {
            if (TextUtils.isEmpty(provider)) {
                signature = Signature.getInstance(DEFAULT_DIGEST_ALGORITHM);
            } else {
                signature = Signature.getInstance(DEFAULT_DIGEST_ALGORITHM, provider);
            }
            signature.initVerify(publicKey);
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }


    public static byte[] sign(Signature signature, byte[] data) {
        if (signature == null || data == null || data.length == 0) {
            return null;
        }
        try {
            signature.update(data);
            return signature.sign();
        } catch (SignatureException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    public static boolean verify(Signature signature, byte[] data, byte[] sign) {
        if (signature == null || data == null || sign == null || data.length == 0 || sign.length == 0) {
            return false;
        }
        try {
            signature.update(data);
            return signature.verify(sign);
        } catch (SignatureException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return false;
    }

}
