package com.aaronia.rtsa;

import com.aaronia.rtsa.native_.*;
import com.sun.jna.WString;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for the Aaronia RTSA API.
 * <p>
 * Manages library initialization, handle lifecycle, and device discovery.
 * Use try-with-resources for automatic cleanup:
 * <pre>
 * try (RtsaApi api = RtsaApi.init(MemorySize.MEDIUM)) {
 *     List&lt;DeviceInfo&gt; devices = api.enumDevices("spectranv6");
 *     try (RtsaDevice device = api.openDevice("spectranv6/iqreceiver", devices.get(0).getSerialNumber())) {
 *         // configure and use device
 *     }
 * }
 * </pre>
 */
public class RtsaApi implements AutoCloseable {

    private final AaroniaRtsaLibrary lib = AaroniaRtsaLibrary.INSTANCE;
    private final AARTSAAPI_Handle handle;
    private boolean closed = false;

    private RtsaApi(AARTSAAPI_Handle handle) {
        this.handle = handle;
    }

    /**
     * Initialize the library and open an API handle.
     *
     * @param memorySize one of {@link MemorySize} constants
     * @return initialized API instance
     */
    public static RtsaApi init(int memorySize) {
        AaroniaRtsaLibrary lib = AaroniaRtsaLibrary.INSTANCE;
        int res = lib.AARTSAAPI_Init(memorySize);
        RtsaDevice.check(res, "AARTSAAPI_Init");

        AARTSAAPI_Handle handle = new AARTSAAPI_Handle();
        res = lib.AARTSAAPI_Open(handle);
        if (ResultCode.isError(res)) {
            lib.AARTSAAPI_Shutdown();
            throw new RtsaException("AARTSAAPI_Open", res);
        }
        return new RtsaApi(handle);
    }

    /**
     * Initialize the library with an XML path and open an API handle.
     *
     * @param memorySize       one of {@link MemorySize} constants
     * @param xmlLookupDir     path to XML configuration directory
     * @return initialized API instance
     */
    public static RtsaApi initWithPath(int memorySize, String xmlLookupDir) {
        AaroniaRtsaLibrary lib = AaroniaRtsaLibrary.INSTANCE;
        int res = lib.AARTSAAPI_Init_With_Path(memorySize, new WString(xmlLookupDir));
        RtsaDevice.check(res, "AARTSAAPI_Init_With_Path");

        AARTSAAPI_Handle handle = new AARTSAAPI_Handle();
        res = lib.AARTSAAPI_Open(handle);
        if (ResultCode.isError(res)) {
            lib.AARTSAAPI_Shutdown();
            throw new RtsaException("AARTSAAPI_Open", res);
        }
        return new RtsaApi(handle);
    }

    /**
     * Get the library version. Upper 16 bits = version, lower 16 bits = revision.
     */
    public int getVersion() {
        return lib.AARTSAAPI_Version();
    }

    /**
     * Rescan for devices. Should be called after USB device changes.
     *
     * @param timeoutMs timeout in milliseconds
     */
    public void rescanDevices(int timeoutMs) {
        RtsaDevice.check(lib.AARTSAAPI_RescanDevices(handle, timeoutMs), "RescanDevices");
    }

    /**
     * Reset all unused devices.
     */
    public void resetDevices() {
        lib.AARTSAAPI_ResetDevices(handle);
    }

    /**
     * Enumerate all devices of a given type.
     *
     * @param type device type, e.g. "spectranv6"
     * @return list of discovered devices
     */
    public List<DeviceInfo> enumDevices(String type) {
        List<DeviceInfo> devices = new ArrayList<>();
        WString wType = new WString(type);
        int index = 0;
        while (true) {
            AARTSAAPI_DeviceInfo dinfo = new AARTSAAPI_DeviceInfo();
            int res = lib.AARTSAAPI_EnumDevice(handle, wType, index, dinfo);
            if (res != ResultCode.OK) break;
            devices.add(new DeviceInfo(
                dinfo.getSerialNumber(),
                dinfo.ready, dinfo.boost,
                dinfo.superspeed, dinfo.active
            ));
            index++;
        }
        return devices;
    }

    /**
     * Open a device for exclusive use.
     *
     * @param type         device type with mode, e.g. "spectranv6/iqreceiver", "spectranv6/raw"
     * @param serialNumber serial number from {@link DeviceInfo#getSerialNumber()}
     * @return device handle
     */
    public RtsaDevice openDevice(String type, String serialNumber) {
        AARTSAAPI_Device device = new AARTSAAPI_Device();
        int res = lib.AARTSAAPI_OpenDevice(handle, device, new WString(type), new WString(serialNumber));
        RtsaDevice.check(res, "OpenDevice");
        return new RtsaDevice(handle, device);
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            lib.AARTSAAPI_Close(handle);
            lib.AARTSAAPI_Shutdown();
        }
    }
}
