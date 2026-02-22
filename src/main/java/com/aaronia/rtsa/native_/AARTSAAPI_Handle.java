package com.aaronia.rtsa.native_;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_Handle (opaque pointer wrapper).
 */
@Structure.FieldOrder({"d"})
public class AARTSAAPI_Handle extends Structure {
    public Pointer d;

    public AARTSAAPI_Handle() {
        super();
    }

    public static class ByReference extends AARTSAAPI_Handle implements Structure.ByReference {}
}
