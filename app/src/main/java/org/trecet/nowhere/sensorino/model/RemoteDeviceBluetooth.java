package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by pablof on 8/03/15.
 */
public class RemoteDeviceBluetooth extends RemoteDevice {

    private BluetoothSPP bt;

    public RemoteDeviceBluetooth(Device device, Context context) {
        super(device,context);
    }


    public void connect(final Connection connection) {
        // TODO When we connect, we probably should empty the receive queue (in case something was queued before)
        bt = new BluetoothSPP(context);

        // TODO Need to check if BT address is legitimate, otherwise it crashes.
        if (bt.isBluetoothEnabled() && bt.isBluetoothAvailable()) {
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
//                    Command command = new Command("info");
//                    bt.send(command.getSerial(),false);


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
            Log.i("Sensorino", "Starting BT service");
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
