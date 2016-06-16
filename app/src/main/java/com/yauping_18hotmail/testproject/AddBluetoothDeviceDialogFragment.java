package com.yauping_18hotmail.testproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by experinced on 6/9/2016.
 */
public class AddBluetoothDeviceDialogFragment extends DialogFragment {



    public static AddBluetoothDeviceDialogFragment newInstance(String title) {
        AddBluetoothDeviceDialogFragment frag = new AddBluetoothDeviceDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }






    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences settings = getActivity().getSharedPreferences("LE_DEVICE_MANAGER", 0);
       final Set<String> set = new HashSet<String>(settings.getStringSet("ACTIVE_DEVICE",new HashSet<String>()));
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setMessage("Do you want to add the device with mac: "+ getArguments().getString("title") +"?")
                .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                @Override
                    public void onClick(DialogInterface arg0, int arg1){
                    if(!set.contains(getArguments().getString("title"))) {
                        set.add(getArguments().getString("title"));
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putStringSet("ACTIVE_DEVICE", set);
                        editor.commit();
                        Toast.makeText(getActivity(), "You clicked yes button", Toast.LENGTH_LONG).show();
                        MainActivity activity =(MainActivity) getActivity();
                        activity.updateActiveDeviceSet(settings);
                    }
                    else{
                        Toast.makeText(getActivity(), "Can't add the device because it is already in the list", Toast.LENGTH_LONG).show();
                    }
            }
        })
                .setNegativeButton("NO",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        Toast.makeText(getActivity(), "You clicked no button", Toast.LENGTH_LONG).show();
                    }
                })
                .create();
    }
}
