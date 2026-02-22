package com.aaronia.rtsa;

/**
 * Packet flag constants for data packets.
 */
public final class PacketFlags {
    public static final long STREAM_START       = 0x0000000000000001L;
    public static final long STREAM_END         = 0x0000000000000002L;
    public static final long SEGMENT_START      = 0x0000000000000004L;
    public static final long SEGMENT_END        = 0x0000000000000008L;

    public static final long PUSH               = 0x0000000000008000L;

    public static final long WARN_OVERFLOW      = 0x0000000000000100L;
    public static final long WARN_DROPPED       = 0x0000000000000200L;
    public static final long WARN_INACCURATE    = 0x0000000000000400L;
    public static final long WARN_RESAMPLED     = 0x0000000000000800L;

    public static final long TIME_DISCONTINUITY = 0x0000000000010000L;
    public static final long WARN_DIRECTION     = 0x0000000000020000L;

    public static final long CONDITION_0        = 0x0000000010000000L;
    public static final long CONDITION_1        = 0x0000000020000000L;
    public static final long CONDITION_2        = 0x0000000040000000L;
    public static final long CONDITION_3        = 0x0000000080000000L;

    private PacketFlags() {}
}
