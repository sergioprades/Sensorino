package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.trecet.nowhere.sensorino.message.MessageDeviceInfo;
import org.trecet.nowhere.sensorino.message.MessageGetSensorData;
import org.trecet.nowhere.sensorino.message.MessageGetSensorInfo;
import org.trecet.nowhere.sensorino.message.MessageSensorData;
import org.trecet.nowhere.sensorino.message.MessageSensorInfo;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by edoras on 3/2/15.
 */
public class RemoteDevice {

    private Device device;
    private BluetoothSPP bt;
    private Context context;

    private Gson gson = new Gson();

    private int uptime;

    public RemoteDevice(Device device, Context context) {
        this.device = device;
        this.context = context;
    }

    public void disconnect() {
        bt.stopService();

    }
    public void connect() {
        bt = new BluetoothSPP(context);
        if (bt.isBluetoothEnabled() && bt.isBluetoothAvailable()) {
            // Do something if bluetooth is already enable

            // Set up listeners before setting up the service
            bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                public void onDeviceConnected(String name, String address) {
                    // Do something when successfully connected
                    Toast.makeText(context, "Bluetooth connection succeeded",
                            Toast.LENGTH_SHORT).show();

//                    Command command = new Command("info");
//                    bt.send(command.getSerial(),false);


                }

                public void onDeviceDisconnected() {
                    // Do something when connection was disconnected
                    Toast.makeText(context, "Bluetooth device disconnected",
                            Toast.LENGTH_SHORT).show();
                }

                public void onDeviceConnectionFailed() {
                    // Do something when connection failed
                    Toast.makeText(context, "Bluetooth connection failed",
                            Toast.LENGTH_SHORT).show();
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
        }

    }

    public void getSensorData() {
        MessageGetSensorData msg_send = new MessageGetSensorData();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String msg_receive) {
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                MessageSensorData message = gson.fromJson(msg_receive, MessageSensorData.class);
                Log.i("Sensorino", "First data: "+ message.getData().get("sensor_1"));

            }
        });

        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        bt.send(msg_send_string,false);
    }

    public void getSensorInfo() {
        // Send { "type": "get_sensor_info" }

        // Expected receive:
        // {"type":"sensor_info","data":{"sensor_1":"temperature_C","sensor_2":"humidity_%"}}
        MessageGetSensorInfo msg_send = new MessageGetSensorInfo();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String msg_receive) {
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                MessageSensorInfo message = gson.fromJson(msg_receive, MessageSensorInfo.class);

            }
        });

        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        bt.send(msg_send_string,false);
    }

    public void getDeviceInfo() {
        // Send { "type": "get_device_info" }

        // Expected receive:
        // {"type":"device_info","data":{"model":"Arduino Pro Mini",
        //   "processor":"Atmega328P-AU","uptime_s":15},
        //

        MessageGetSensorInfo msg_send = new MessageGetSensorInfo();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String msg_receive) {
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                MessageDeviceInfo message = gson.fromJson(msg_receive, MessageDeviceInfo.class);

                RemoteDevice.this.uptime = Integer.parseInt(message.getData().get("uptime_s"));
            }
        });

        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        bt.send(msg_send_string,false);
    }

    public int getUptime() {
        return uptime;
    }
}
