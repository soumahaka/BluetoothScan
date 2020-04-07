package com.example.discoverbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Button turn_on_button;
    private Button turn_off_button;
    private ArrayList<String> scan_arrayList;
    private Button scan_button;
    private Button shwo_paired;
    ArrayAdapter<String> adapter;
    ListView scan_list;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan_list = (ListView) findViewById(R.id.scan_list);
        scan_button = (Button) findViewById(R.id.scan_button);
        turn_on_button=(Button)findViewById(R.id.on_button);
        turn_off_button=(Button)findViewById(R.id.off_button);

        scan_arrayList=new ArrayList<String>();


        turn_on_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
                } else if (mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Bluetooth already on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        turn_off_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PERMET DE DESACTIVER BLUETOOTH
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), "Bluetooth turning off",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_arrayList.clear();

                if (mBluetoothAdapter.isDiscovering()) {

                    mBluetoothAdapter.cancelDiscovery();
                }
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.startDiscovery();
                    } else if (!mBluetoothAdapter.isEnabled()) {
                        Toast.makeText(getApplicationContext(), "turn on your Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }

        });

        IntentFilter intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,intentFilter);
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_selectable_list_item,scan_arrayList);
        scan_list.setAdapter(adapter);
    }

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    scan_arrayList.add(device.getName());
                    adapter.notifyDataSetChanged();

                }
            }
        };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }
}


