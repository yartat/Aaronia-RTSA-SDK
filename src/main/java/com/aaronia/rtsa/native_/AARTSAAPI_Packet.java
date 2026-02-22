package com.aaronia.rtsa.native_;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_Packet.
 * <p>
 * <p>
 * The cbsize field must be set to the structure size before use.
 */
@Structure.FieldOrder({
    "cbsize", "streamID", "flags",
    "startTime", "endTime", "startFrequency", "stepFrequency",
    "spanFrequency", "rbwFrequency",
    "num", "total", "size", "stride", "fp32", "interleave"
})
public class AARTSAAPI_Packet extends Structure {
    public long cbsize;
    public long streamID;
    public long flags;

    public double startTime;
    public double endTime;
    public double startFrequency;
    public double stepFrequency;
    public double spanFrequency;
    public double rbwFrequency;

    public long num;
    public long total;
    public long size;
    public long stride;
    public Pointer fp32;

    public long interleave;

    public AARTSAAPI_Packet() {
        super();
        cbsize = size();
    }

    /**
     * Read float samples from the native fp32 pointer.
     *
     * @param offset sample offset (in floats)
     * @param count  number of floats to read
     * @return float array
     */
    public float[] readFloats(int offset, int count) {
        if (fp32 == null) return new float[0];
        return fp32.getFloatArray((long) offset * Float.BYTES, count);
    }

    public static class ByReference extends AARTSAAPI_Packet implements Structure.ByReference {}
}
