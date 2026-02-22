package com.aaronia.rtsa;

/**
 * Exception thrown when an Aaronia RTSA API call returns an error.
 */
public class RtsaException extends RuntimeException {

    private final int resultCode;

    public RtsaException(int resultCode) {
        super("AARTSAAPI error: " + ResultCode.toString(resultCode));
        this.resultCode = resultCode;
    }

    public RtsaException(String message, int resultCode) {
        super(message + ": " + ResultCode.toString(resultCode));
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
