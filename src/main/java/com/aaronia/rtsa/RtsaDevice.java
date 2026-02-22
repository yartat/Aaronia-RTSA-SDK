package com.aaronia.rtsa;

import com.aaronia.rtsa.native_.*;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

/**
 * High-level wrapper for an Aaronia RTSA device, providing configuration,
 * data acquisition and transmission.
 * <p>
 * Obtain via {@link RtsaApi#openDevice(String, String)}.
 * Must be closed via {@link #close()} or use try-with-resources.
 */
public class RtsaDevice implements AutoCloseable {

    private final AaroniaRtsaLibrary lib = AaroniaRtsaLibrary.INSTANCE;
    private final AARTSAAPI_Handle handle;
    private final AARTSAAPI_Device device;
    private boolean closed = false;

    RtsaDevice(AARTSAAPI_Handle handle, AARTSAAPI_Device device) {
        this.handle = handle;
        this.device = device;
    }

    AARTSAAPI_Device nativeDevice() {
        return device;
    }

    // --- Connection lifecycle ---

    public void connect() {
        check(lib.AARTSAAPI_ConnectDevice(device), "ConnectDevice");
    }

    public void disconnect() {
        lib.AARTSAAPI_DisconnectDevice(device);
    }

    public void start() {
        check(lib.AARTSAAPI_StartDevice(device), "StartDevice");
    }

    public void stop() {
        lib.AARTSAAPI_StopDevice(device);
    }

    public int getDeviceState() {
        return lib.AARTSAAPI_GetDeviceState(device);
    }

    // --- Configuration ---

    /**
     * Get the config root handle for this device.
     */
    public RtsaConfig configRoot() {
        AARTSAAPI_Config config = new AARTSAAPI_Config();
        check(lib.AARTSAAPI_ConfigRoot(device, config), "ConfigRoot");
        return new RtsaConfig(this, config);
    }

    /**
     * Get the health/status config root for this device.
     */
    public RtsaConfig configHealth() {
        AARTSAAPI_Config config = new AARTSAAPI_Config();
        check(lib.AARTSAAPI_ConfigHealth(device, config), "ConfigHealth");
        return new RtsaConfig(this, config);
    }

    /**
     * Find a config item by path from the config root.
     * Path uses '/' separator (e.g. "device/receiverchannel").
     *
     * @return config item, or null if not found
     */
    public RtsaConfig configFind(String path) {
        return configRoot().find(path);
    }

    /**
     * Convenience: set a string config value by path.
     */
    public void configSetString(String path, String value) {
        RtsaConfig cfg = configFind(path);
        if (cfg != null) cfg.setString(value);
    }

    /**
     * Convenience: set a float config value by path.
     */
    public void configSetFloat(String path, double value) {
        RtsaConfig cfg = configFind(path);
        if (cfg != null) cfg.setFloat(value);
    }

    /**
     * Convenience: set an integer config value by path.
     */
    public void configSetInteger(String path, long value) {
        RtsaConfig cfg = configFind(path);
        if (cfg != null) cfg.setInteger(value);
    }

    // --- Data packets ---

    /**
     * Get the number of available packets on a channel.
     */
    public int availablePackets(int channel) {
        IntByReference num = new IntByReference();
        int res = lib.AARTSAAPI_AvailPackets(device, channel, num);
        if (res == ResultCode.OK) return num.getValue();
        return 0;
    }

    /**
     * Get a data packet from a channel's output queue.
     *
     * @param channel output channel index
     * @param index   packet index in the queue
     * @return the packet, or null if queue is empty
     */
    public AARTSAAPI_Packet getPacket(int channel, int index) {
        AARTSAAPI_Packet packet = new AARTSAAPI_Packet();
        int res = lib.AARTSAAPI_GetPacket(device, channel, index, packet);
        if (res == ResultCode.OK) return packet;
        if (res == ResultCode.EMPTY) return null;
        check(res, "GetPacket");
        return null;
    }

    /**
     * Consume (remove) packets from a channel's output queue.
     */
    public void consumePackets(int channel, int num) {
        lib.AARTSAAPI_ConsumePackets(device, channel, num);
    }

    /**
     * Get the current master stream time in seconds since epoch.
     */
    public double getMasterStreamTime() {
        DoubleByReference time = new DoubleByReference();
        lib.AARTSAAPI_GetMasterStreamTime(device, time);
        return time.getValue();
    }

    /**
     * Send a packet to an inbound channel.
     */
    public void sendPacket(int channel, AARTSAAPI_Packet packet) {
        check(lib.AARTSAAPI_SendPacket(device, channel, packet), "SendPacket");
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            lib.AARTSAAPI_CloseDevice(handle, device);
        }
    }

    static void check(int result, String operation) {
        if (ResultCode.isError(result)) {
            throw new RtsaException(operation, result);
        }
    }
}
