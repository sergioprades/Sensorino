package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.util.Predicate;
import com.google.gson.Gson;

import org.trecet.nowhere.sensorino.message.MessageDeviceInfo;
import org.trecet.nowhere.sensorino.message.MessageGetDeviceInfo;
import org.trecet.nowhere.sensorino.message.MessageGetSensorData;
import org.trecet.nowhere.sensorino.message.MessageGetSensorInfo;
import org.trecet.nowhere.sensorino.message.MessageSensorData;
import org.trecet.nowhere.sensorino.message.MessageSensorInfo;

import java.util.Map;
import java.util.concurrent.Callable;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by edoras on 3/2/15.
 */
public abstract class RemoteDevice {

    protected Device device;
    protected Context context;

    protected Gson gson = new Gson();

    private int uptime;

    public RemoteDevice(Device device, Context context) {
        this.device = device;
        this.context = context;
    }

    public int getUptime() {
        return uptime;
    }

    public interface Command {
        public void onSuccess();
        public void onFailure();
    }

    public interface Connection {
        public void onConnected();
        public void onDisconnected();
    }

    public interface Reception {
        public void onDataReceived(String message);
    }

    public abstract void disconnect();

    public abstract void connect(final Connection connection);

    public abstract void send(String message);

    public abstract void onDataReceived(Reception reception);


    // TODO we probably need to get all the rest out of this class
    public void getSensorData(final Command command) {
        // Expected receive:
        // {"type":"sensor_data","data":{"sensor_1":25.0,"sensor_2":45.0}}
        onDataReceived(new Reception() {
            @Override
            public void onDataReceived(String msg_receive){
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                // TODO check message is of the right type (exception?)
                MessageSensorData message = gson.fromJson(msg_receive, MessageSensorData.class);
                Log.i("Sensorino", "Received data: "+ message.getData().toString());

                // TODO Separate this... we may not want to insert it into the historical data
                for (String name: message.getSensorNames()) {
                    //TODO need to check the sensor actually exists
                    SensorData sensorData = new SensorData((int) (System.currentTimeMillis()/1000L),
                            message.getSensorValue(name));
                    device.getSensor(name).addSensorData(sensorData);
                }
                device.persist(context);

                // And finally call the callback before ending
                command.onSuccess();
            }
        });

        // Send { "type": "get_sensor_data" }
        MessageGetSensorData msg_send = new MessageGetSensorData();
        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        send(msg_send_string);
    }

    public void getSensorInfo(final Command command) {
        // Expected receive:
        // {"type":"sensor_info","data":{"sensor_1":"temperature_C","sensor_2":"humidity_p100"}}
        onDataReceived(new Reception() {
            @Override
            public void onDataReceived(String msg_receive){
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                //TODO check message is of the right type (exception?)
                MessageSensorInfo message = gson.fromJson(msg_receive, MessageSensorInfo.class);

                for (String name: message.getSensorNames()) {
                    if (! device.getSensorNames().contains(name)) {
                        // Create a new sensor
                        Log.i ("Sensorino", "Found new device "+name);
                        Sensor sensor = new Sensor(name, message.getSensorType(name));
                        device.setSensor(name, sensor);
                        device.persist(context);

                    }
                    // TODO check if the device existed but with a different type
                }
                command.onSuccess();
            }
        });

        // Send { "type": "get_sensor_info" }
        MessageGetSensorInfo msg_send = new MessageGetSensorInfo();
        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        send(msg_send_string);
    }

    public void getDeviceInfo(final Command command) {
        // Expected receive:
        // {"type":"device_info","data":{"model":"Arduino Pro Mini",
        //   "processor":"Atmega328P-AU","uptime_s":15},
        onDataReceived(new Reception() {
            @Override
            public void onDataReceived(String msg_receive){
                // Do something when data incoming
                Log.d("Sensorino", "Received: " + msg_receive);
                // TODO check message is of the right type (exception?)
                MessageDeviceInfo message = gson.fromJson(msg_receive, MessageDeviceInfo.class);
                // TODO make the getData private here as well if possible (like MessageSensorInfo)
                RemoteDevice.this.uptime = Integer.parseInt(message.getData().get("uptime_s"));

                command.onSuccess();
            }
        });

        // Send { "type": "get_device_info" }
        MessageGetDeviceInfo msg_send = new MessageGetDeviceInfo();
        String msg_send_string = gson.toJson(msg_send);
        Log.d("Sensorino", "Sent: " + msg_send_string);
        send(msg_send_string);
    }

}
