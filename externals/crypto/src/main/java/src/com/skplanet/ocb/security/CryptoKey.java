package com.skplanet.ocb.security;

public interface CryptoKey {
    
    /**
     * @param type Crypto.Method.TYPE_SEED , Crypto.Method.TYPE_MSEED
     * @return
     */
    public byte[] getEncodingCipherKey(String type);

    /**
     * @param type Crypto.Method.TYPE_SEED , Crypto.Method.TYPE_MSEED
     * @return
     */
    public byte[] getDecodingCipherKey(String type);
}
