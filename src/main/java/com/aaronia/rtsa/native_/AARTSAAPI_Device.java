package com.aaronia.rtsa.native_;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_Device (opaque pointer wrapper).
 */
@Structure.FieldOrder({"d"})
public class AARTSAAPI_Device extends Structure {
    public Pointer d;

    public AARTSAAPI_Device() {
        super();
    }

    public static class ByReference extends AARTSAAPI_Device implements Structure.ByReference {}
}
