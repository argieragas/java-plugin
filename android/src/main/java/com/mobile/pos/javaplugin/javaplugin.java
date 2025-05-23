package com.mobile.pos.javaplugin;

import android.util.Log;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.getcapacitor.JSObject;

import java.util.UUID;


@SuppressLint("MissingPermission")
public class javaplugin {

    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothAdapter bluetoothAdapter;
    public DeviceConnection deviceConnection;
    public BluetoothSocket socket;
    public javaplugin() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    
    public JSObject connectToDevice(String deviceAddress) {
        JSObject object = new JSObject();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        deviceConnection = new BluetoothConnection(device);
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (Exception e) {
            object.put("error", e.getMessage());
        }
        return object;
    }

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
