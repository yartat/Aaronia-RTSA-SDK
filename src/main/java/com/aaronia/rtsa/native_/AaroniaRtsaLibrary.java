package com.aaronia.rtsa.native_;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * JNA interface mapping all functions from the Aaronia RTSA API native library.
 * <p>
 * The library is loaded from the system library path. On Windows this is typically
 * {@code AaroniaRTSAAPI.dll}, on Linux {@code libAaroniaRTSAAPI.so}.
 * <p>
 * Set system property {@code aaronia.rtsa.library} to override the library name,
 * or {@code jna.library.path} to add the directory containing the library.
 */
public interface AaroniaRtsaLibrary extends Library {

    String LIB_NAME = System.getProperty("aaronia.rtsa.library", "AaroniaRTSAAPI");

    AaroniaRtsaLibrary INSTANCE = Native.load(LIB_NAME, AaroniaRtsaLibrary.class);

    // --- Lifecycle ---

    int AARTSAAPI_Init(int memory);

    int AARTSAAPI_Init_With_Path(int memory, WString pathXmlLocation);

    int AARTSAAPI_Shutdown();

    int AARTSAAPI_Version();

    // --- Handle management ---

    int AARTSAAPI_Open(AARTSAAPI_Handle handle);

    int AARTSAAPI_Close(AARTSAAPI_Handle handle);

    // --- Device discovery ---

    int AARTSAAPI_RescanDevices(AARTSAAPI_Handle handle, int timeout);

    int AARTSAAPI_ResetDevices(AARTSAAPI_Handle handle);

    int AARTSAAPI_EnumDevice(AARTSAAPI_Handle handle, WString type, int index, AARTSAAPI_DeviceInfo dinfo);

    // --- Device management ---

    int AARTSAAPI_OpenDevice(AARTSAAPI_Handle handle, AARTSAAPI_Device dhandle, WString type, WString serialNumber);

    int AARTSAAPI_CloseDevice(AARTSAAPI_Handle handle, AARTSAAPI_Device dhandle);

    int AARTSAAPI_ConnectDevice(AARTSAAPI_Device dhandle);

    int AARTSAAPI_DisconnectDevice(AARTSAAPI_Device dhandle);

    int AARTSAAPI_StartDevice(AARTSAAPI_Device dhandle);

    int AARTSAAPI_StopDevice(AARTSAAPI_Device dhandle);

    int AARTSAAPI_GetDeviceState(AARTSAAPI_Device dhandle);

    // --- Data packets ---

    int AARTSAAPI_AvailPackets(AARTSAAPI_Device dhandle, int channel, IntByReference num);

    int AARTSAAPI_GetPacket(AARTSAAPI_Device dhandle, int channel, int index, AARTSAAPI_Packet packet);

    int AARTSAAPI_ConsumePackets(AARTSAAPI_Device dhandle, int channel, int num);

    int AARTSAAPI_GetMasterStreamTime(AARTSAAPI_Device dhandle, DoubleByReference stime);

    int AARTSAAPI_SendPacket(AARTSAAPI_Device dhandle, int channel, AARTSAAPI_Packet packet);

    // --- Configuration ---

    int AARTSAAPI_ConfigRoot(AARTSAAPI_Device dhandle, AARTSAAPI_Config config);

    int AARTSAAPI_ConfigHealth(AARTSAAPI_Device dhandle, AARTSAAPI_Config config);

    int AARTSAAPI_ConfigFirst(AARTSAAPI_Device dhandle, AARTSAAPI_Config group, AARTSAAPI_Config config);

    int AARTSAAPI_ConfigNext(AARTSAAPI_Device dhandle, AARTSAAPI_Config group, AARTSAAPI_Config config);

    int AARTSAAPI_ConfigFind(AARTSAAPI_Device dhandle, AARTSAAPI_Config group, AARTSAAPI_Config config, WString name);

    int AARTSAAPI_ConfigGetName(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, char[] name);

    int AARTSAAPI_ConfigGetInfo(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, AARTSAAPI_ConfigInfo cinfo);

    int AARTSAAPI_ConfigSetFloat(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, double value);

    int AARTSAAPI_ConfigGetFloat(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, DoubleByReference value);

    int AARTSAAPI_ConfigSetString(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, WString value);

    int AARTSAAPI_ConfigGetString(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, char[] value, LongByReference size);

    int AARTSAAPI_ConfigSetInteger(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, long value);

    int AARTSAAPI_ConfigGetInteger(AARTSAAPI_Device dhandle, AARTSAAPI_Config config, LongByReference value);
}
