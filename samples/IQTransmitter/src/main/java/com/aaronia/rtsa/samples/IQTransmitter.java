package com.aaronia.rtsa.samples;

import java.util.List;

import com.aaronia.rtsa.DeviceInfo;
import com.aaronia.rtsa.MemorySize;
import com.aaronia.rtsa.PacketFlags;
import com.aaronia.rtsa.ResultCode;
import com.aaronia.rtsa.RtsaApi;
import com.aaronia.rtsa.RtsaDevice;
import com.aaronia.rtsa.RtsaException;
import com.aaronia.rtsa.native_.AARTSAAPI_Packet;
import com.sun.jna.Memory;

/**
 * Java port of the C++ IQTransmitter sample.
 * <p>
 * Generates a frequency-swept IQ signal and transmits it via a Spectran V6 device.
 */
public class IQTransmitter {

    private static final double PI = Math.PI;
    private static final double ZERO_DBM = Math.sqrt(1.0 / 20.0);
    private static final int NUM_SAMPLES = 16384;

    public static void main(String[] args) {
        try (RtsaApi api = RtsaApi.init(MemorySize.MEDIUM)) {
            api.rescanDevices(2000);

            List<DeviceInfo> devices = api.enumDevices("spectranv6");
            if (devices.isEmpty()) {
                System.err.println("No Spectran V6 device found");
                return;
            }

            System.out.println("Found device: " + devices.get(0));

            try (RtsaDevice device = api.openDevice("spectranv6/iqtransmitter", devices.get(0).getSerialNumber())) {
                // Configure transmitter
                device.configSetFloat("main/centerfreq", 2440.0e6);
                device.configSetFloat("main/spanfreq", 50.0e6);
                device.configSetFloat("main/transgain", 0.0);

                // Connect and start
                device.connect();
                device.start();

                // Wait for device to be running
                while (device.getDeviceState() != ResultCode.RUNNING) {
                    System.out.print(".");
                    System.out.flush();
                    Thread.sleep(100);
                }
                System.out.println();

                // Stream IQ data
                streamIQ(device);

                // Cleanup
                device.disconnect();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted");
        } catch (RtsaException e) {
            System.err.println("RTSA error: " + e.getMessage());
        }
    }

    private static void streamIQ(RtsaDevice device) throws InterruptedException {
        // Prepare IQ buffers with a frequency sweep
        float[] iqBuffer = new float[NUM_SAMPLES * 2];
        float[] riqBuffer = new float[NUM_SAMPLES * 2];

        double w = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            double phi = ((double) i / NUM_SAMPLES * 2 - 1) * PI;
            w += phi;

            iqBuffer[2 * i + 1] = (float) (Math.cos(w) * ZERO_DBM);
            iqBuffer[2 * i + 0] = (float) (Math.sin(w) * ZERO_DBM);

            riqBuffer[2 * i + 1] = (float) (Math.cos(-w) * ZERO_DBM);
            riqBuffer[2 * i + 0] = (float) (Math.sin(-w) * ZERO_DBM);
        }

        // Allocate native memory for both buffers
        long bufferBytes = (long) NUM_SAMPLES * 2 * Float.BYTES;
        Memory nativeIqBuffer = new Memory(bufferBytes);
        Memory nativeRiqBuffer = new Memory(bufferBytes);
        nativeIqBuffer.write(0, iqBuffer, 0, iqBuffer.length);
        nativeRiqBuffer.write(0, riqBuffer, 0, riqBuffer.length);

        // Prepare output packet
        AARTSAAPI_Packet packet = new AARTSAAPI_Packet();
        packet.startFrequency = 2430.0e6;
        packet.stepFrequency = 1.0e6;

        // Get current system time and schedule first packet 200ms ahead
        double startTime = device.getMasterStreamTime();
        packet.startTime = startTime + 0.2;
        packet.size = 2;
        packet.stride = 2;
        packet.fp32 = nativeIqBuffer;
        packet.num = NUM_SAMPLES;

        int numPackets = 100;

        for (int i = 0; i < numPackets; i++) {
            // Calculate end time based on number of samples and sample rate
            packet.endTime = packet.startTime + (double) packet.num / packet.stepFrequency;

            // Alternate sweep direction
            packet.fp32 = (i % 2 != 0) ? nativeRiqBuffer : nativeIqBuffer;

            // Wait for max queue fill level of 45ms
            startTime = device.getMasterStreamTime();
            while (startTime + 0.05 < packet.startTime) {
                long sleepMs = (long) (1000 * (packet.startTime - startTime - 0.045));
                if (sleepMs > 0) {
                    Thread.sleep(sleepMs);
                }
                startTime = device.getMasterStreamTime();
            }

            // Set start and end flags
            if (i == 0) {
                packet.flags = PacketFlags.SEGMENT_START | PacketFlags.STREAM_START;
            } else if (i + 1 == numPackets) {
                packet.flags = PacketFlags.SEGMENT_END | PacketFlags.STREAM_END;
            } else {
                packet.flags = 0;
            }

            // Send the packet
            packet.write();
            device.sendPacket(0, packet);

            // Advance packet time
            packet.startTime = packet.endTime;
        }

        // Wait for the last packet to finish
        startTime = device.getMasterStreamTime();
        while (startTime < packet.startTime) {
            long sleepMs = (long) (1000 * (packet.startTime - startTime));
            if (sleepMs > 0) {
                Thread.sleep(sleepMs);
            }
            startTime = device.getMasterStreamTime();
        }
    }
}
