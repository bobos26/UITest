
package com.skplanet.trunk.carowner.common;


import android.os.Build;

import com.cnt.encoder.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class CipherUtils {
    private static final String TAG = CipherUtils.class.getName();

    private static final byte[] makesKy() {
        return new String(Constants.B + Build.SERIAL + Constants.A + Build.MODEL + Constants.O).getBytes();
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String _md5(byte[] text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(text);
            byte[] md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (Exception e) {
            return "";
        }
    }

    private static final String AES = "AES";
    private static final byte[] sKy = makesKy();
    private static final byte[] sKy128 = _md5(sKy).getBytes();

    public static String encrypt(String to_encrypt) {
        try {
            SecretKeySpec aesKeySpec = new SecretKeySpec(sKy128, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
            return new String(Base64.encode(cipher.doFinal(to_encrypt.getBytes())));
        } catch (Exception e) {
            LLog.d(TAG, "", e);
            return null;
        }
    }

    public static String decrypt(String to_decrypt) {
        try {
            SecretKeySpec desKeySpec = new SecretKeySpec(sKy128, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, desKeySpec);
            byte[] rr = Base64.decode(to_decrypt);
            return new String(cipher.doFinal(rr));
        } catch (Exception e) {
            LLog.d(TAG, "", e);
            return null;
        }
    }

}
