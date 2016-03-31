package com.skplanet.ocb.security;

import java.io.UnsupportedEncodingException;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Base64;

// SEED/ECB/X.923
public class SEEDCrypto extends Crypto {
    private static final int BLOCK_SIZE = 16;
    private static final int KEY_LENGTH = 32;

    SEEDCrypto(Method method) {
        super(method);
    }

    private void validateKey() throws CryptoException {
        if (_key == null) {            
            throw new CryptoException();
        }
    }

    @Override
    public byte[] encrypt(byte[] plainMessage) throws CryptoException {        
        final int[] roundedKey = new int[KEY_LENGTH];
        
        validateKey();
        SEED_KISA.SeedRoundKey(roundedKey, _key.getEncodingCipherKey(_method.getName()));
        return _encrypt(plainMessage, roundedKey);
    }

    @Override
    public String encryptBase64(byte[] plainMessage) throws CryptoException {
        int[] roundedKey = new int[KEY_LENGTH];

        validateKey();
        SEED_KISA.SeedRoundKey(roundedKey, _key.getEncodingCipherKey(_method.getName()));

        try {
			return new String(Base64.encode(_encrypt(plainMessage, roundedKey),
			        Base64.NO_WRAP),  _method.getEncoding());
		} catch (UnsupportedEncodingException e) {
			return new String(Base64.encode(_encrypt(plainMessage, roundedKey),
			        Base64.NO_WRAP));
		}
    }

    @Override
    public byte[] decrypt(byte[] encryptedMessage) throws CryptoException {
        int[] roundedKey = new int[KEY_LENGTH];
        validateKey();
        SEED_KISA.SeedRoundKey(roundedKey, _key.getDecodingCipherKey(_method.getName()));

        return _decrypt(encryptedMessage, roundedKey);
    }

    @Override
    public String decryptBase64(byte[] encryptedMessage) throws CryptoException {
        try {
            int[] roundedKey = new int[KEY_LENGTH];
            validateKey();
            SEED_KISA.SeedRoundKey(roundedKey, _key.getDecodingCipherKey(_method.getName()));

            return new String(
                    _decrypt(com.cnt.encoder.Base64.decode(encryptedMessage), roundedKey),
                    _method.getEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] _encrypt(byte[] data, int[] roundedKey) {
        int nrounds = data.length / BLOCK_SIZE + 1;

        ByteArrayBuffer buf = new ByteArrayBuffer(nrounds * BLOCK_SIZE);

        int idx = 0;
        int paddings = 0;
        byte[] d = new byte[BLOCK_SIZE];
        byte[] out = new byte[BLOCK_SIZE];
        for (int i = 0; i < nrounds; ++i) {
            for (int j = 0; j < BLOCK_SIZE; ++j) {
                if (idx < data.length) {
                    d[j] = data[idx++];
                } else {
                    ++paddings;

                    if (j == BLOCK_SIZE - 1)
                        d[j] = (byte) paddings;
                    else
                        d[j] = 0x00;
                }
            }

            SEED_KISA.SeedEncrypt(d, roundedKey, out);
            buf.append(out, 0, BLOCK_SIZE);
        }

        return buf.toByteArray();
    }

    private byte[] _decrypt(byte[] data, int[] roundedKey) {
        if (data.length % BLOCK_SIZE != 0)
            return null;

        int nrounds = data.length / BLOCK_SIZE;

        ByteArrayBuffer buf = new ByteArrayBuffer(nrounds * BLOCK_SIZE);

        int idx = 0;
        byte[] d = new byte[BLOCK_SIZE];
        byte[] out = new byte[BLOCK_SIZE];
        for (int i = 0; i < nrounds; ++i) {
            for (int j = 0; j < BLOCK_SIZE; ++j)
                d[j] = data[idx++];

            SEED_KISA.SeedDecrypt(d, roundedKey, out);
            buf.append(out, 0, BLOCK_SIZE);
        }

        byte[] f = buf.toByteArray();
        int padlen = f[f.length - 1];

        byte[] finalout = new byte[f.length - padlen];
        for (int i = 0; i < finalout.length; ++i)
            finalout[i] = f[i];

        return finalout;
    }
}
