package org.trecet.nowhere.sensorino.message;

import java.util.ArrayList;

/**
 * Created by edoras on 3/2/15.
 */
public class MessageSensorData {
    private int type = MessageType.SENSOR_DATA;
    private ArrayList<float> sensordata;

    public ArrayList<float> getSensordata() {
        return sensordata;
    }

    public void setSensordata(ArrayList<float> sensordata) {
        this.sensordata = sensordata;
    }
}
