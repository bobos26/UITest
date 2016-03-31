package com.skplanet.ocb.security;

import java.util.HashMap;
import java.util.Map;

public abstract class Crypto {
    public static final class Method {
        public static final String TYPE_SEED = "SEED";
        public static final String TYPE_MSEED = "MSEED";

        private final String name;
        private final String encoding;

        private Method(String name, String encoding) {
            this.name = name;
            this.encoding = encoding;
        }

        public String getName() {
            return name;
        }
        
        public String getEncoding() {
            return encoding;
        }
        
        @Override
        public String toString() {
            return name + ":" + encoding;
        }
    }

    public static final Method SEED_EUCKR;
    public static final Method SEED_UTF8;
    public static final Method MSEED_EUCKR;
    public static final Method MSEED_UTF8;

    private static final Map<Method, Crypto> _crypto;

    public abstract byte[] encrypt(byte[] plainMessage) throws CryptoException;

    public abstract String encryptBase64(byte[] plainMessage)
            throws CryptoException;

    public abstract byte[] decrypt(byte[] encryptedMessage)
            throws CryptoException;

    public abstract String decryptBase64(byte[] encryptedMessage)
            throws CryptoException;

    static {
        SEED_EUCKR = new Method(Method.TYPE_SEED, CharacterSets.EUC_KR);
        SEED_UTF8 = new Method(Method.TYPE_SEED, CharacterSets.UTF_8);
        MSEED_EUCKR = new Method(Method.TYPE_MSEED, CharacterSets.EUC_KR);
        MSEED_UTF8 = new Method(Method.TYPE_MSEED, CharacterSets.UTF_8);

        _crypto = new HashMap<Method, Crypto>(4);
        _crypto.put(SEED_EUCKR, new SEEDCrypto(SEED_EUCKR));
        _crypto.put(SEED_UTF8, new SEEDCrypto(SEED_UTF8));
        _crypto.put(MSEED_EUCKR, new SEEDCrypto(MSEED_EUCKR));
        _crypto.put(MSEED_UTF8, new SEEDCrypto(MSEED_UTF8));
    }
    
    protected static final String CRYPTO_KEY_IMPL = "com.skplanet.ocb.security.impl.CryptoKeyImpl";

    public static final Crypto getCrypto(final Method m) {
        return _crypto.get(m);
    }

    public static final Crypto getSeedCrypto(final String enc) {
        if (CharacterSets.EUC_KR.equalsIgnoreCase(enc)) {
            return _crypto.get(SEED_EUCKR);
        } else if (CharacterSets.UTF_8.equalsIgnoreCase(enc)) {
            return _crypto.get(SEED_UTF8);
        } else {
            return null;
        }
    }

    public static final Crypto getMobileCrypto(final Method m) {
        return _crypto.get(m);
    }

    public static final Crypto getMobileSeedCrypto(final String enc) {
        if (CharacterSets.EUC_KR.equalsIgnoreCase(enc)) {
            return _crypto.get(MSEED_EUCKR);
        } else if (CharacterSets.UTF_8.equalsIgnoreCase(enc)) {
            return _crypto.get(MSEED_UTF8);
        } else {
            return null;
        }
    }
    
    private static CryptoKey getCryptoKeyImpl() {
        Class<?> clazz = null;
        CryptoKey cryptoKey = null;
        try {
            clazz = Class.forName(CRYPTO_KEY_IMPL);
            if (clazz != null) {
                cryptoKey = (CryptoKey) clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptoKey;
    }
    
    protected Method _method;    
    protected CryptoKey _key;
    
    Crypto(Method method) {
        _method = method;
        _key = getCryptoKeyImpl();
    }
}
