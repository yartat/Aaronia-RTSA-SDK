# Aaronia RTSA SDK for Java

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![JNA](https://img.shields.io/badge/JNA-5.14.0-green)](https://github.com/java-native-access/jna)

A Java wrapper for the [Aaronia RTSA](https://aaronia.com/software/rtsa-suite/) (Real-Time Spectrum Analyzer) native API, built with [JNA](https://github.com/java-native-access/jna). Provides a clean, idiomatic Java interface for controlling Aaronia Spectran V6 devices — including device discovery, configuration, IQ data acquisition, and transmission.

## Features

- **Device discovery** — scan and enumerate connected Spectran V6 devices
- **Configuration tree** — navigate and modify the full device config tree (frequency, gain, sample rate, etc.)
- **IQ data streaming** — receive and transmit IQ packets with precise timing
- **Health monitoring** — read device health/status parameters
- **Resource-safe** — all handles implement `AutoCloseable` for use with try-with-resources
- **Error mapping** — native result codes are translated to descriptive `RtsaException` messages

## Prerequisites

- **Java 21** or later
- **Maven 3.9+**
- **Aaronia RTSA Suite** installed (provides the native `aartsaapi` shared library)
- A connected **Aaronia Spectran V6** device (for runtime usage)

## Project Structure

```
├── src/main/java/com/aaronia/rtsa/
│   ├── RtsaApi.java          # Library init, device enumeration & opening
│   ├── RtsaDevice.java       # Device lifecycle, config helpers, packet I/O
│   ├── RtsaConfig.java       # Config tree navigation & value access
│   ├── RtsaException.java    # Exception with native result code
│   ├── ResultCode.java       # Native result code constants
│   ├── ConfigInfo.java       # Config item metadata
│   ├── ConfigType.java       # Config item type enum
│   ├── DeviceInfo.java       # Discovered device descriptor
│   ├── MemorySize.java       # Init memory presets
│   ├── PacketFlags.java      # Data packet flag constants
│   └── native_/              # Low-level JNA structures & library binding
│       ├── AaroniaRtsaLibrary.java
│       ├── AARTSAAPI_Config.java
│       ├── AARTSAAPI_ConfigInfo.java
│       ├── AARTSAAPI_Device.java
│       ├── AARTSAAPI_DeviceInfo.java
│       ├── AARTSAAPI_Handle.java
│       └── AARTSAAPI_Packet.java
├── samples/
│   └── IQTransmitter/        # Frequency-swept IQ transmitter example
└── pom.xml
```

## Building

```bash
# Build the SDK library
mvn clean package

# Build a sample (requires the SDK jar)
mvn package -f samples/IQTransmitter/pom.xml
```

## Quick Start

```java
import com.aaronia.rtsa.*;

try (RtsaApi api = RtsaApi.init(MemorySize.MEDIUM)) {
    api.rescanDevices(2000);

    List<DeviceInfo> devices = api.enumDevices("spectranv6");
    System.out.println("Found: " + devices);

    try (RtsaDevice device = api.openDevice("spectranv6/iqreceiver",
                                             devices.get(0).getSerialNumber())) {
        device.configSetFloat("main/centerfreq", 2440.0e6);
        device.configSetFloat("main/spanfreq", 50.0e6);

        device.connect();
        device.start();

        // Read IQ packets
        AARTSAAPI_Packet pkt = device.getPacket(0, 0);
        if (pkt != null) {
            System.out.println("Got packet, samples: " + pkt.num);
            device.consumePackets(0, 1);
        }
    }
}
```

## Samples

| Sample | Description |
|--------|-------------|
| [IQTransmitter](samples/IQTransmitter) | Generates a frequency-swept IQ signal and transmits it through a Spectran V6 device |

## API Overview

| Class | Purpose |
|-------|---------|
| `RtsaApi` | Library initialization, handle management, device enumeration |
| `RtsaDevice` | Device connection, start/stop, configuration shortcuts, packet I/O |
| `RtsaConfig` | Config tree traversal (`first`, `next`, `find`) and value get/set |
| `ResultCode` | Native return code constants and helpers (`isOk`, `isError`, `isWarning`) |
| `RtsaException` | Runtime exception carrying the native error code |
| `DeviceInfo` | Serial number, ready/boost/superspeed/active flags |
| `ConfigInfo` | Config item name, title, type, min/max/step, unit, options |
| `MemorySize` | `SMALL`, `MEDIUM`, `LARGE`, `LUDICROUS` presets for `RtsaApi.init()` |
| `PacketFlags` | Bit flags for stream/segment start/end, warnings, conditions |

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.