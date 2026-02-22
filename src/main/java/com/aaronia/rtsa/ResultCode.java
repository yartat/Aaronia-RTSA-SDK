package com.aaronia.rtsa;

/**
 * Result codes returned by the AARTSAAPI native functions.
 */
public final class ResultCode {

    public static final int OK                      = 0x00000000;
    public static final int EMPTY                   = 0x00000001;
    public static final int RETRY                   = 0x00000002;

    public static final int IDLE                    = 0x10000000;
    public static final int CONNECTING              = 0x10000001;
    public static final int CONNECTED               = 0x10000002;
    public static final int STARTING                = 0x10000003;
    public static final int RUNNING                 = 0x10000004;
    public static final int STOPPING                = 0x10000005;
    public static final int DISCONNECTING           = 0x10000006;

    public static final int WARNING                 = 0x40000000;
    public static final int WARNING_VALUE_ADJUSTED  = 0x40000001;
    public static final int WARNING_VALUE_DISABLED  = 0x40000002;

    public static final int ERROR                   = 0x80000000;
    public static final int ERROR_NOT_INITIALIZED   = 0x80000001;
    public static final int ERROR_NOT_FOUND         = 0x80000002;
    public static final int ERROR_BUSY              = 0x80000003;
    public static final int ERROR_NOT_OPEN          = 0x80000004;
    public static final int ERROR_NOT_CONNECTED     = 0x80000005;
    public static final int ERROR_INVALID_CONFIG    = 0x80000006;
    public static final int ERROR_BUFFER_SIZE       = 0x80000007;
    public static final int ERROR_INVALID_CHANNEL   = 0x80000008;
    public static final int ERROR_INVALID_PARAMETER = 0x80000009;
    public static final int ERROR_INVALID_SIZE      = 0x8000000a;
    public static final int ERROR_MISSING_PATHS_FILE= 0x8000000b;
    public static final int ERROR_VALUE_INVALID     = 0x8000000c;
    public static final int ERROR_VALUE_MALFORMED   = 0x8000000d;

    private ResultCode() {}

    public static boolean isOk(int result) {
        return result == OK;
    }

    public static boolean isError(int result) {
        return (result & ERROR) != 0;
    }

    public static boolean isWarning(int result) {
        return (result & WARNING) != 0 && !isError(result);
    }

    public static String toString(int result) {
        switch (result) {
            case OK:                      return "OK";
            case EMPTY:                   return "EMPTY";
            case RETRY:                   return "RETRY";
            case IDLE:                    return "IDLE";
            case CONNECTING:              return "CONNECTING";
            case CONNECTED:               return "CONNECTED";
            case STARTING:                return "STARTING";
            case RUNNING:                 return "RUNNING";
            case STOPPING:                return "STOPPING";
            case DISCONNECTING:           return "DISCONNECTING";
            case WARNING:                 return "WARNING";
            case WARNING_VALUE_ADJUSTED:  return "WARNING_VALUE_ADJUSTED";
            case WARNING_VALUE_DISABLED:  return "WARNING_VALUE_DISABLED";
            case ERROR:                   return "ERROR";
            case ERROR_NOT_INITIALIZED:   return "ERROR_NOT_INITIALIZED";
            case ERROR_NOT_FOUND:         return "ERROR_NOT_FOUND";
            case ERROR_BUSY:              return "ERROR_BUSY";
            case ERROR_NOT_OPEN:          return "ERROR_NOT_OPEN";
            case ERROR_NOT_CONNECTED:     return "ERROR_NOT_CONNECTED";
            case ERROR_INVALID_CONFIG:    return "ERROR_INVALID_CONFIG";
            case ERROR_BUFFER_SIZE:       return "ERROR_BUFFER_SIZE";
            case ERROR_INVALID_CHANNEL:   return "ERROR_INVALID_CHANNEL";
            case ERROR_INVALID_PARAMETER: return "ERROR_INVALID_PARAMETER";
            case ERROR_INVALID_SIZE:      return "ERROR_INVALID_SIZE";
            case ERROR_MISSING_PATHS_FILE:return "ERROR_MISSING_PATHS_FILE";
            case ERROR_VALUE_INVALID:     return "ERROR_VALUE_INVALID";
            case ERROR_VALUE_MALFORMED:   return "ERROR_VALUE_MALFORMED";
            default:                      return "UNKNOWN(0x%08x)".formatted(result);
        }
    }
}
