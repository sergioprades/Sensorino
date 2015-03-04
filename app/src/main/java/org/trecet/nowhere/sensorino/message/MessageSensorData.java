package org.trecet.nowhere.sensorino.message;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by edoras on 3/2/15.
 */
public class MessageSensorData {
    private MessageType type = MessageType.SENSOR_DATA;

    private Map<String, Float> data;

    public Map<String, Float> getData() {
        return data;
    }

    public void setData(Map<String, Float> data) {
        this.data = data;
    }
}
