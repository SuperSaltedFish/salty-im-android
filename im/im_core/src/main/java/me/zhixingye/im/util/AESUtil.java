package me.zhixingye.im.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import me.zhixingye.im.service.LogService;

/**
 * Created by YZX on 2018年12月13日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */

public class AESUtil {

    private static final String TAG = "AESUtil";

    private final static String AES;
    private final static String BLOCK_MODE;
    private final static String ENCRYPTION_PADDING;
    private final static String PROVIDER;
    private final static int DEFAULT_KEY_SIZE;
    private final static String ALGORITHM;

    static {
        PROVIDER = "AndroidKeyStore";
        DEFAULT_KEY_SIZE = 192;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AES = "AES";
            BLOCK_MODE = "CBC";
            ENCRYPTION_PADDING = "PKCS7Padding";
        } else {
            AES = KeyProperties.KEY_ALGORITHM_AES;
            BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
            ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        }
        ALGORITHM = String.format("%s/%s/%s", AES, BLOCK_MODE, ENCRYPTION_PADDING);
    }

    private static KeyStore sKeyStore;

    public static Key generateAESKey() {
        return generateAESKey(DEFAULT_KEY_SIZE);
    }

    public static Key generateAESKey(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
        }
        return null;
    }


    @Nullable
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Key generateAESKeyInAndroidKeyStore(String keyAlias) {
        return generateAESKeyInAndroidKeyStore(keyAlias, DEFAULT_KEY_SIZE);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Key generateAESKeyInAndroidKeyStore(String keyAlias, int keySize) {
        if (sKeyStore == null && !initKeyStore()) {
            return null;
        }
        try {
            Key key = sKeyStore.getKey(keyAlias, null);
            if (key == null) {
                KeyGenerator generator = KeyGenerator.getInstance(AES, PROVIDER);
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                        keyAlias,
                        KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT);
                builder.setBlockModes(BLOCK_MODE);
                builder.setEncryptionPaddings(ENCRYPTION_PADDING);
                builder.setKeySize(keySize);
                builder.setCertificateSubject(new X500Principal("CN=" + keyAlias));
                generator.init(builder.build());
                key = generator.generateKey();
            }
            return key;
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | UnrecoverableKeyException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
        }
        return null;
    }

    public static SecretKey loadKey(byte[] keyBytes) {
        try {
            return new SecretKeySpec(keyBytes, AES);
        } catch (IllegalArgumentException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] encrypt(byte[] content, byte[] keyBytes, @NonNull byte[] iv) {
        return encrypt(content, loadKey(keyBytes), iv);
    }

    @Nullable
    public static byte[] encrypt(byte[] content, Key key, @NonNull byte[] iv) {
        if (content == null || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | BadPaddingException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
        }
        return null;
    }

    @Nullable
    public static byte[] decrypt(byte[] content, byte[] keyBytes, @NonNull byte[] iv) {
        return decrypt(content, loadKey(keyBytes), iv);
    }

    @Nullable
    public static byte[] decrypt(byte[] content, Key key, @NonNull byte[] iv) {
        if (content == null || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IllegalBlockSizeException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | BadPaddingException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
        }
        return null;
    }


    private static boolean initKeyStore() {
        try {
            sKeyStore = KeyStore.getInstance(PROVIDER);
            sKeyStore.load(null);
        } catch (KeyStoreException
                | CertificateException
                | NoSuchAlgorithmException
                | IOException e) {
            LogService.getLogger().d(TAG, e.toString(), e);
            sKeyStore = null;
            return false;
        }
        return true;
    }

}
