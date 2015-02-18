package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;


public class ViewDeviceActivity extends Activity {
    Device device;
    Devices devices;
    int deviceID;

    BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensorino_view_device);

        // Get the Device as a parameter
        devices = Devices.getInstance(this);
        deviceID = getIntent().getIntExtra("DeviceID",-1);
        device = devices.getDeviceByPosition(deviceID);
        //device = (Device)getIntent().getSerializableExtra("Device");

        // Get the BT handler
        bt = new BluetoothSPP(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensorino_view_device, menu);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(bt.isBluetoothAvailable()) {
            // Do something if bluetooth is already enable
            Log.i("Sensorino", "Starting BT service");
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);

            bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                public void onDeviceConnected(String name, String address) {
                    // Do something when successfully connected
                    Toast.makeText(ViewDeviceActivity.this, "Bluetooth connection succeeded", Toast.LENGTH_SHORT).show();
                    bt.send("Hi!",true);
                }

                public void onDeviceDisconnected() {
                    // Do something when connection was disconnected
                    Toast.makeText(ViewDeviceActivity.this, "Bluetooth device disconnected", Toast.LENGTH_SHORT).show();
                }

                public void onDeviceConnectionFailed() {
                    // Do something when connection failed
                    Toast.makeText(ViewDeviceActivity.this, "Bluetooth connection failed", Toast.LENGTH_SHORT).show();
                }
            });
            bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
                public void onDataReceived(byte[] data, String message) {
                    // Do something when data incoming
                    Log.i("Sensorino", "Received bytes: "+data.length);
                    TextView t = (TextView)findViewById(R.id.txt_viewDevice);
                    t.append(message);
                }
            });

            Log.i("Sensorino", "Connecting to " + device.getRemote_address());
            bt.connect(device.getRemote_address());
        } else {
            Toast.makeText(this, "Bluetooth adapter is not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStop(){
        super.onStop();

        //Stop BT Service
        bt.stopService();

    }
    @Override
    public void onResume() {
        super.onResume();

        setTitle("Device: " + device.getLocal_name());

        TextView t = (TextView)findViewById(R.id.txt_viewDevice);
        t.setText(
                "Local name: " + device.getLocal_name() + "\n" +
                "Remote name: " + device.getRemote_name() + "\n" +
                "Remote address: " + device.getRemote_address() + "\n" +
                "Frequency: " + device.getFrequency() + "\n"

        );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_device) {
            Intent action_new_device= new Intent(this, NewDeviceActivity.class);
            // Pass the device as parameter
            action_new_device.putExtra("DeviceID", deviceID);
            startActivity(action_new_device);
            return true;
        }
//TODO Rebuild chat activity
//        if (id == R.id.action_chat) {
//            Intent action_chat= new Intent(this, ChatMainActivity.class);
//            startActivity(action_chat);
//            return true;
//        }
        if (id == R.id.action_remove_device) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.remove_device))
                    .setMessage(getString(R.string.remove_device_confirmation))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            devices.remove(device);
                            Toast.makeText(ViewDeviceActivity.this,
                                    "Device " + device.getLocal_name() + " removed",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

/*
// Constants that indicate the current connection state
public static final int STATE_NONE = 0;             // we're doing nothing
public static final int STATE_LISTEN = 1;           // now listening for incoming connections
public static final int STATE_CONNECTING = 2;       // now initiating an outgoing connection
public static final int STATE_CONNECTED = 3;        // now connected to a remote device
public static final int STATE_NULL = -1;            // now service is null

// Message types sent from the BluetoothChatService Handler
public static final int MESSAGE_STATE_CHANGE = 1;
public static final int MESSAGE_READ = 2;
public static final int MESSAGE_WRITE = 3;
public static final int MESSAGE_DEVICE_NAME = 4;
public static final int MESSAGE_TOAST = 5;
*/