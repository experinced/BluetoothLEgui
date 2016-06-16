package com.yauping_18hotmail.testproject;

import android.bluetooth.le.ScanCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by experinced on 6/9/2016.
 */
public class BluetoothLeDeviceList {
    private final Map<String, BluetoothLeDevice> mLeDeviceMap;

    public BluetoothLeDeviceList() {
        mLeDeviceMap = new LinkedHashMap<>();
    }

    public void add(final BluetoothLeDevice device) {
        mLeDeviceMap.put(device.readDeviceId().toString(), device);
    }

    public void clear() {
        mLeDeviceMap.clear();
    }

    public ArrayList<BluetoothLeDevice> list() {
        ArrayList<BluetoothLeDevice> le_device = new ArrayList<>(mLeDeviceMap.values());
        return le_device;
    }
}
