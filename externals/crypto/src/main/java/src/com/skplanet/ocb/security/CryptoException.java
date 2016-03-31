package com.skplanet.ocb.security;

public class CryptoException extends Exception {
    private static final long serialVersionUID = 1L;
    private int _code;

    public CryptoException() {
    }

    public CryptoException(int code, String message) {
        super(message);
        this._code = code;
    }

    public int getCode() {
        return _code;
    }
}
