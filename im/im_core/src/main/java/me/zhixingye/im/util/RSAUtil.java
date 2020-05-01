package me.zhixingye.im.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

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
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.security.auth.x500.X500Principal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import me.zhixingye.im.tool.Logger;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

//一下封装了对rsa的各种操作
public class RSAUtil {

    private static final String TAG = "RSAUtil";

    private final static String RSA = "RSA";
    private final static String PROVIDER = "AndroidKeyStore";
    private final static String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    private final static int DEFAULT_KEY_SIZE = 2048;

    private static KeyStore sKeyStore;

    @Nullable
    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(DEFAULT_KEY_SIZE);
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            Logger.d(TAG, e.toString(), e);
            return null;
        }
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static KeyPair generateRSAKeyPairInAndroidKeyStore(Context context, String keyAlias) {
        return generateRSAKeyPairInAndroidKeyStore(context, keyAlias, DEFAULT_KEY_SIZE);
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static KeyPair generateRSAKeyPairInAndroidKeyStore(Context context, String keyAlias, int keySize) {
        if (sKeyStore == null && !initKeyStore()) {
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
            return generateRSAKeyPairInAndroidKeyStoreApi23(keyAlias, keySize);
        } else {
            return generateRSAKeyPairInAndroidKeyStoreApi18(context, keyAlias, keySize);
        }
    }

    @Nullable
    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static KeyPair generateRSAKeyPairInAndroidKeyStoreApi18(Context context, String keyAlias, int keySize) {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.YEAR, 10);
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA, PROVIDER);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setKeySize(keySize)
                    .setSubject(new X500Principal("CN=" + keyAlias))
                    .setAlias(keyAlias)
                    .setKeyType(RSA)
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
    private static KeyPair generateRSAKeyPairInAndroidKeyStoreApi23(String keyAlias, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, PROVIDER);
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                    .setCertificateSubject(new X500Principal("CN=" + keyAlias))
                    .setKeySize(keySize)
                    .build();
            keyPairGenerator.initialize(spec);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) {
        return encryptByPublicKey(data, publicKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey, int keySize) {
        if (data == null || publicKey == null) {
            return null;
        }
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return encryptByPublicKey(data, kf.generatePublic(new X509EncodedKeySpec(publicKey)), keySize);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) {
        return encryptByPublicKey(data, publicKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey, int keySize) {
        if (data == null || publicKey == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return analysis(cipher, data, getMaxEncryptLength(keySize), getDataLengthAfterEncryption(keySize));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | ShortBufferException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) {
        return encryptByPrivateKey(data, privateKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey, int keySize) {
        if (data == null || privateKey == null) {
            return null;
        }
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return encryptByPrivateKey(data, kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey)), keySize);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    //AndroidStoreKey生成的PrivateKey是不能用于加密的
    @Nullable
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) {
        return encryptByPrivateKey(data, privateKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey, int keySize) {
        if (data == null || privateKey == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            return analysis(cipher, data, getMaxEncryptLength(keySize), getDataLengthAfterEncryption(keySize));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | ShortBufferException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) {
        return decryptByPublicKey(data, publicKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey, int keySize) {
        if (data == null || publicKey == null) {
            return null;
        }
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return decryptByPublicKey(data, kf.generatePublic(new X509EncodedKeySpec(publicKey)), keySize);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey) {
        return decryptByPublicKey(data, publicKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey, int keySize) {
        if (data == null || publicKey == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return analysis(cipher, data, getMaxDecryptLength(keySize), getMaxDecryptLength(keySize));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | ShortBufferException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) {
        return decryptByPrivateKey(data, privateKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey, int keySize) {
        if (data == null || privateKey == null) {
            return null;
        }
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return decryptByPrivateKey(data, kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey)), keySize);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) {
        return decryptByPrivateKey(data, privateKey, DEFAULT_KEY_SIZE);
    }

    @Nullable
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey, int keySize) {
        if (data == null || privateKey == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return analysis(cipher, data, getMaxDecryptLength(keySize), getMaxDecryptLength(keySize));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | ShortBufferException e) {
            Logger.d(TAG, e.toString(), e);
        }
        return null;
    }

    public static PublicKey loadPublicKey(byte[] publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
            return null;
        }
    }


    public static PrivateKey loadPrivateKey(byte[] privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.d(TAG, e.toString(), e);
            return null;
        }
    }

    private static boolean initKeyStore() {
        try {
            sKeyStore = KeyStore.getInstance(PROVIDER);
            sKeyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Logger.d(TAG, e.toString(), e);
            sKeyStore = null;
            return false;
        }
        return true;
    }


    private static int getMaxEncryptLength(int keySize) {
        return keySize / 8 - 11;
    }

    private static int getMaxDecryptLength(int keySize) {
        return keySize / 8;
    }

    private static int getDataLengthAfterEncryption(int keySize) {
        return keySize / 8;
    }

    private static byte[] analysis(Cipher cipher, byte[] data, int maxAnalysisLength, int lengthAfterAnalysis) throws BadPaddingException, ShortBufferException, IllegalBlockSizeException {
        int alreadyAnalysisLength = 0;
        int needAnalysisLength = data.length;
        int analysisNumber = (int) Math.ceil((double) needAnalysisLength / maxAnalysisLength);
        byte[] buff = new byte[lengthAfterAnalysis * analysisNumber];
        int buffOffset = 0;
        while (needAnalysisLength > 0) {
            if (needAnalysisLength >= maxAnalysisLength) {
                buffOffset += cipher.doFinal(data, alreadyAnalysisLength, maxAnalysisLength, buff, buffOffset);
                alreadyAnalysisLength += maxAnalysisLength;
                needAnalysisLength -= maxAnalysisLength;
            } else {
                buffOffset += cipher.doFinal(data, alreadyAnalysisLength, needAnalysisLength, buff, buffOffset);
                needAnalysisLength = 0;
            }
        }
        return Arrays.copyOf(buff, buffOffset);
    }

}
