package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by pablof on 8/03/15.
 */
public class RemoteDeviceBluetooth extends RemoteDevice {

    private BluetoothSPP bt;

    public RemoteDeviceBluetooth(Device device, Context context) {
        super(device,context);
        bt = new BluetoothSPP(context);
    }

    public void connect(final Connection connection) {
        // TODO When we connect, we probably should empty the receive queue (in case something was queued before)

        // Need to check if BT address is legitimate, otherwise it crashes.
        Pattern p = Pattern.compile("..:..:..:..:..:..");
        Matcher m = p.matcher(device.getRemote_address());
        if (m.matches() && bt.isBluetoothEnabled() && bt.isBluetoothAvailable()) {
            // Do something if bluetooth is already enable

            // This is not working...
//            // Wipe the receive buffer
//            bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
//                public void onDataReceived(byte[] data, String msg_receive) {
//                }
//            });

            // Set up listeners before setting up the service
            bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                public void onDeviceConnected(String name, String address) {
                    // Do something when successfully connected
                    Toast.makeText(context, "Bluetooth connection succeeded",
                            Toast.LENGTH_SHORT).show();

                    // Callback to make progeess
                    connection.onConnected();
                }

                public void onDeviceDisconnected() {
                    // Do something when connection was disconnected
                    Toast.makeText(context, "Bluetooth device disconnected",
                            Toast.LENGTH_SHORT).show();

                    connection.onDisconnected();
                }

                public void onDeviceConnectionFailed() {
                    // Do something when connection failed
                    Toast.makeText(context, "Bluetooth connection failed",
                            Toast.LENGTH_SHORT).show();
                    connection.onDisconnected();
                }
            });

            // Start the BT Service
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);

            Log.i("Sensorino", "Connecting to " + device.getRemote_address());
            bt.connect(device.getRemote_address());
        } else {
            Toast.makeText(context, "Bluetooth adapter is not available", Toast.LENGTH_SHORT).show();
            connection.onDisconnected();
        }
    }


    public void disconnect() {
        bt.stopService();
    }

    public void send(String message) {
        bt.send(message + "\n",false);
    }

    public void onDataReceived(final Reception reception){
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                reception.onDataReceived(message);
            }
        });
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