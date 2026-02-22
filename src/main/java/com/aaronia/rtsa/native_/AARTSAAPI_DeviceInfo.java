package com.aaronia.rtsa.native_;

import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_DeviceInfo.
 * <p>
 * Uses char[] for serialNumber since the native type is wchar_t[120].
 */
@Structure.FieldOrder({"cbsize", "serialNumber", "ready", "boost", "superspeed", "active"})
public class AARTSAAPI_DeviceInfo extends Structure {
    public long cbsize;
    public char[] serialNumber = new char[120];
    public boolean ready;
    public boolean boost;
    public boolean superspeed;
    public boolean active;

    public AARTSAAPI_DeviceInfo() {
        super(ALIGN_DEFAULT);
        cbsize = size();
    }

    public String getSerialNumber() {
        return new String(serialNumber).trim().replace("\0", "");
    }

    public static class ByReference extends AARTSAAPI_DeviceInfo implements Structure.ByReference {}
}
