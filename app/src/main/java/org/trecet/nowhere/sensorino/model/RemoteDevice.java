package org.trecet.nowhere.sensorino.model;

import android.util.Log;

import com.google.gson.Gson;

import org.trecet.nowhere.sensorino.message.MessageGetSensorData;
import org.trecet.nowhere.sensorino.message.MessageSensorData;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by edoras on 3/2/15.
 */
public class RemoteDevice {

    private Device device;
    private BluetoothSPP bt;
    private Gson gson = new Gson();

    public RemoteDevice(Device device, BluetoothSPP bt) {
        this.bt = bt;
        this.device = device;
    }

    public void getSensorData() {
        MessageGetSensorData msg_send = new MessageGetSensorData();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String msg_receive) {
                // Do something when data incoming
                Log.i("Sensorino", "Received bytes: " + data.length);
                MessageSensorData message = gson.fromJson(msg_receive, MessageSensorData.class);
                Log.i("Sensorino", "First data: "+message.getSensordata().get(1));

            }
        });

        bt.send(gson.toJson(msg_send),false);
    }
}
