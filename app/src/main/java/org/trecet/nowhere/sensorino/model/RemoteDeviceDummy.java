package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.trecet.nowhere.sensorino.message.MessageType;

import java.util.Random;

/**
 * Created by pablof on 8/03/15.
 */
public class RemoteDeviceDummy extends RemoteDevice {

    MessageType lastMessage;

    public RemoteDeviceDummy(Device device, Context context) {
        super(device,context);
    }

    public void connect(final Connection connection) {
        Log.i("Sensorino", "Connecting to Dummy Device");
        // We're connected by default
        connection.onConnected();
    }

    public void disconnect() {
        Log.i("Sensorino", "Disconnected from Dummy Device");
    }

    public void send(String message) {
        try {
            JSONObject json_msg = new JSONObject(message);
            MessageType type = MessageType.valueOf(json_msg.getString("type").toUpperCase());
            lastMessage = type;
        } catch (JSONException e) {
            Log.e("Sensorino", "Invalid JSON string: " + message, e);
        }
    }

    public void onDataReceived(final Reception reception){
        // TODO check this... I don't even know how it works!
        // (a normal thread thows an exception when updating the UI
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Thread(new Runnable() {
            @Override
            public void run() {
                // This is the delay
                try {
                    // Need to leave some time for the message to arrive
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // This will run on a background thread
                // TODO we should create the right message types, not the string
                switch (lastMessage) {
                    case GET_DEVICE_INFO:
                        // {"type":"device_info","data":{"model":"Arduino Pro Mini",
                        //   "processor":"Atmega328P-AU","uptime_s":15},
                        reception.onDataReceived("{\"type\":\"device_info\",\"data\":{\"model\":\"Dummy device\",\"processor\":\"Dummy\",\"uptime_s\":15}}");
                        break;
                    case GET_SENSOR_INFO:
                        // {"type":"sensor_info","data":{"sensor_1":"temperature_C","sensor_2":"humidity_p100"}}
                        reception.onDataReceived("{\"type\":\"sensor_info\",\"data\":{\"sensor_1\":\"temperature_C\",\"sensor_2\":\"humidity_p100\"}}");
                        break;
                    case GET_SENSOR_DATA:
                        Random r = new Random();
                        int temp = r.nextInt(35 - 20) + 20;  // between 20 and 35
                        int hum = r.nextInt(70 - 40) + 40;  // Between 40 and 70
                        // {"type":"sensor_data","data":{"sensor_1":25.0,"sensor_2":45.0}}
                        reception.onDataReceived("{\"type\":\"sensor_data\",\"data\":{\"sensor_1\":"+temp+".0,\"sensor_2\":"+hum+".0}}");
                        break;
                    default:
                        break;
                }
            }
        }));
    }
}
