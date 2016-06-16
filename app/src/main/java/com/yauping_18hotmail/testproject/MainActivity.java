package com.yauping_18hotmail.testproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD=30000;
   private  BluetoothLeScanner mLEScanner;
   private  ScanSettings settings;
    private BluetoothLeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothLeDeviceList LedeviceList;
    private List<ScanFilter> filters;
    private ListView DeviceListView;
    private ArrayList<BluetoothLeDevice>LeDeviceListView;
    private Set<String> active_device_set;

    // LeDeviceListAdapter mLeDeviceListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DeviceListView = (ListView) findViewById(R.id.detected_device_listview);
        mHandler = new Handler();
        BluetoothManager bluetoothManager = (BluetoothManager)this.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        filters = new ArrayList<ScanFilter>();
        LedeviceList = new BluetoothLeDeviceList();
        LeDeviceListView = new ArrayList<>();
        mLeDeviceListAdapter = new BluetoothLeDeviceListAdapter(this,LeDeviceListView);
        DeviceListView.setAdapter(mLeDeviceListAdapter);
        final SharedPreferences user_settings = getSharedPreferences("LE_DEVICE_MANAGER",MODE_PRIVATE);
        updateActiveDeviceSet(user_settings);
        DeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment newFragment = AddBluetoothDeviceDialogFragment.newInstance(LedeviceList.list().get(position).readDeviceId().toString());
                newFragment.show(getFragmentManager(), "dialog");
            }
        });
        scanLeDevice(true);
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            LeDeviceListView.clear();
            BluetoothLeDevice detectedDevice = new BluetoothLeDevice(callbackType,result);
            LedeviceList.add(detectedDevice);
            LeDeviceListView.addAll(LedeviceList.list());
            mLeDeviceListAdapter.notifyDataSetChanged();
            TextView textview = (TextView) findViewById(R.id.textview_device_numbers);
            textview.setText(String.valueOf(active_device_set.size()));
           final int device_rssi=detectedDevice.readRssi();
            final String device_name=detectedDevice.readDeviceId().toString();
            if (active_device_set.contains(device_name) && device_rssi < -90) {
                Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                long[] thrice = {0, 100, 400, 100, 400, 100};
                v.vibrate(thrice, -1);
            }
        }
    };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
                LedeviceList.clear();
            LeDeviceListView.clear();
                Toast.makeText(this, "BLE Scan started",Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Toast.makeText(getApplicationContext(), "BLE Scan Stopped",Toast.LENGTH_SHORT).show();
                            mLEScanner.stopScan(mScanCallback);
                    }
                }, SCAN_PERIOD);
                mLEScanner.startScan(filters, settings, mScanCallback);

            } else {
                    mLEScanner.stopScan(mScanCallback);
            }
        }

    public void updateActiveDeviceSet(SharedPreferences user_settings){
        active_device_set = new HashSet<String>(user_settings.getStringSet("ACTIVE_DEVICE",new HashSet<String>()));
    }

}



