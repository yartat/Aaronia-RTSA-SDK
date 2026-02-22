package com.aaronia.rtsa;

/**
 * Information about a discovered Aaronia device.
 */
public class DeviceInfo {

    private final String serialNumber;
    private final boolean ready;
    private final boolean boost;
    private final boolean superspeed;
    private final boolean active;

    public DeviceInfo(String serialNumber, boolean ready, boolean boost, boolean superspeed, boolean active) {
        this.serialNumber = serialNumber;
        this.ready = ready;
        this.boost = boost;
        this.superspeed = superspeed;
        this.active = active;
    }

    public String getSerialNumber() { return serialNumber; }
    public boolean isReady()        { return ready; }
    public boolean isBoost()        { return boost; }
    public boolean isSuperspeed()   { return superspeed; }
    public boolean isActive()       { return active; }

    @Override
    public String toString() {
        return "DeviceInfo{serial=" + serialNumber +
               ", ready=" + ready +
               ", boost=" + boost +
               ", superspeed=" + superspeed +
               ", active=" + active + "}";
    }
}
