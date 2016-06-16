package com.yauping_18hotmail.testproject;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by experinced on 6/8/2016.
 */
public class BluetoothLeDevice implements Parcelable {
    private int mRSSI;
    private BluetoothDevice mDeviceID;
    private static final String PARCEL_BLE_RSSI = "ble_rssi";
    private static final String PARCEL_BLE_ID="ble_id";

    public BluetoothLeDevice(int callbackType, ScanResult result ){
        mRSSI=result.getRssi();
        mDeviceID=result.getDevice();
    }

    public BluetoothLeDevice(final BluetoothLeDevice device){
        mRSSI=device.readRssi();
        mDeviceID=device.readDeviceId();
    }

    protected BluetoothLeDevice(Parcel parcel){
        Bundle bundle=parcel.readBundle(getClass().getClassLoader());
        mRSSI=bundle.getInt(PARCEL_BLE_RSSI,0);
        mDeviceID=bundle.getParcelable(PARCEL_BLE_ID);
    }



    public static final Parcelable.Creator<BluetoothLeDevice> CREATOR = new Parcelable.Creator<BluetoothLeDevice>() {
        public BluetoothLeDevice createFromParcel(final Parcel parcel) {
            return new BluetoothLeDevice(parcel);
        }
        public BluetoothLeDevice[] newArray(final int size) {
            return new BluetoothLeDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle =new Bundle(getClass().getClassLoader());
        bundle.putInt(PARCEL_BLE_RSSI,mRSSI);
        bundle.putParcelable(PARCEL_BLE_ID,mDeviceID);
        dest.writeBundle(bundle);
    }

    public int readRssi(){
        return mRSSI;
    }

    public BluetoothDevice readDeviceId(){
        return mDeviceID;
    }
}
