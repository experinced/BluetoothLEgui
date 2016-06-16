package com.yauping_18hotmail.testproject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by experinced on 6/9/2016.
 */
public class BluetoothLeDeviceListAdapter extends ArrayAdapter<BluetoothLeDevice> {
    private final LayoutInflater mInflator;

    public BluetoothLeDeviceListAdapter(Activity activity, ArrayList<BluetoothLeDevice> LeDevice){
        super(activity,R.layout.device_list,LeDevice);
        mInflator=activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        BluetoothLeDevice LeDevice =getItem(position);
        if(view==null){
            view=mInflator.inflate(R.layout.device_list,null);
        }
        TextView deviceId= (TextView) view.findViewById(R.id.textview_device_id);
        TextView deviceRssi=(TextView) view.findViewById(R.id.textview_device_rssi);
        deviceId.setText(LeDevice.readDeviceId().toString());
        deviceRssi.setText(String.valueOf(LeDevice.readRssi()));
        return view;
    }
}
