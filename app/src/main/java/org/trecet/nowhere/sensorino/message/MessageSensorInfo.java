package org.trecet.nowhere.sensorino.message;

import org.trecet.nowhere.sensorino.model.Sensor;

import java.util.Map;
import java.util.Set;

/**
 * Created by pablof on 4/03/15.
 */
public class MessageSensorInfo {
    private MessageType type = MessageType.SENSOR_INFO;

    private Map<String, String> data;

    public Set<String> getSensorNames(){
        return data.keySet();
    }

    public Sensor.SensorType getSensorType(String name){
        // TODO check if sensor type does not exist (maybe use the UNKNOWN type?)
        return Sensor.SensorType.valueOf(data.get(name).toUpperCase());

    }
    // Raw methods, better not use!
    // TODO check if this can be private
    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
