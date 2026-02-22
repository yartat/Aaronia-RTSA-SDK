package com.aaronia.rtsa;

import com.aaronia.rtsa.native_.*;
import com.sun.jna.WString;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * Wrapper around a configuration item in the device's config tree.
 * Provides navigation (first, next, find) and value access (get/set).
 */
public class RtsaConfig {

    private final RtsaDevice device;
    private final AARTSAAPI_Config nativeConfig;
    private final AaroniaRtsaLibrary lib = AaroniaRtsaLibrary.INSTANCE;

    RtsaConfig(RtsaDevice device, AARTSAAPI_Config nativeConfig) {
        this.device = device;
        this.nativeConfig = nativeConfig;
    }

    AARTSAAPI_Config nativeConfig() {
        return nativeConfig;
    }

    /**
     * Get the first child config item of this group.
     *
     * @return first child, or null if no children
     */
    public RtsaConfig first() {
        AARTSAAPI_Config child = new AARTSAAPI_Config();
        int res = lib.AARTSAAPI_ConfigFirst(device.nativeDevice(), nativeConfig, child);
        return res == ResultCode.OK ? new RtsaConfig(device, child) : null;
    }

    /**
     * Advance to the next sibling config item.
     *
     * @return next sibling, or null if no more
     */
    public RtsaConfig next() {
        AARTSAAPI_Config sibling = new AARTSAAPI_Config();
        int res = lib.AARTSAAPI_ConfigNext(device.nativeDevice(), nativeConfig, sibling);
        return res == ResultCode.OK ? new RtsaConfig(device, sibling) : null;
    }

    /**
     * Find a config item by path from this node.
     *
     * @param path slash-separated path (e.g. "device/receiverchannel")
     * @return found config item, or null if not found
     */
    public RtsaConfig find(String path) {
        AARTSAAPI_Config found = new AARTSAAPI_Config();
        int res = lib.AARTSAAPI_ConfigFind(device.nativeDevice(), nativeConfig, found, new WString(path));
        return res == ResultCode.OK ? new RtsaConfig(device, found) : null;
    }

    /**
     * Get the internal name of this config item.
     */
    public String getName() {
        char[] name = new char[80];
        lib.AARTSAAPI_ConfigGetName(device.nativeDevice(), nativeConfig, name);
        return new String(name).trim().replace("\0", "");
    }

    /**
     * Get full metadata for this config item.
     */
    public ConfigInfo getInfo() {
        AARTSAAPI_ConfigInfo info = new AARTSAAPI_ConfigInfo();
        lib.AARTSAAPI_ConfigGetInfo(device.nativeDevice(), nativeConfig, info);
        return new ConfigInfo(
            info.getName(), info.getTitle(),
            ConfigType.fromValue(info.type),
            info.minValue, info.maxValue, info.stepValue,
            info.getUnit(), info.getOptions(), info.disabledOptions
        );
    }

    /**
     * Get the config type directly.
     */
    public ConfigType getType() {
        return getInfo().getType();
    }

    // --- Value access ---

    public void setFloat(double value) {
        lib.AARTSAAPI_ConfigSetFloat(device.nativeDevice(), nativeConfig, value);
    }

    public double getFloat() {
        DoubleByReference ref = new DoubleByReference();
        lib.AARTSAAPI_ConfigGetFloat(device.nativeDevice(), nativeConfig, ref);
        return ref.getValue();
    }

    public void setString(String value) {
        lib.AARTSAAPI_ConfigSetString(device.nativeDevice(), nativeConfig, new WString(value));
    }

    public String getString() {
        char[] buf = new char[1024];
        LongByReference size = new LongByReference(buf.length);
        lib.AARTSAAPI_ConfigGetString(device.nativeDevice(), nativeConfig, buf, size);
        return new String(buf, 0, (int) Math.min(size.getValue(), buf.length)).trim().replace("\0", "");
    }

    public void setInteger(long value) {
        lib.AARTSAAPI_ConfigSetInteger(device.nativeDevice(), nativeConfig, value);
    }

    public long getInteger() {
        LongByReference ref = new LongByReference();
        lib.AARTSAAPI_ConfigGetInteger(device.nativeDevice(), nativeConfig, ref);
        return ref.getValue();
    }
}
