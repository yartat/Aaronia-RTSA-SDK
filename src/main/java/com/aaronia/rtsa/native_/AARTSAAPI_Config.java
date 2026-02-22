package com.aaronia.rtsa.native_;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_Config (opaque pointer wrapper).
 */
@Structure.FieldOrder({"d"})
public class AARTSAAPI_Config extends Structure {
    public Pointer d;

    public AARTSAAPI_Config() {
        super();
    }

    public static class ByReference extends AARTSAAPI_Config implements Structure.ByReference {}
}
