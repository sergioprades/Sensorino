package org.trecet.nowhere.sensorino.message;

import org.trecet.nowhere.sensorino.model.Message;
import org.trecet.nowhere.sensorino.model.MessageType;

import java.util.Map;
import java.util.Set;

/**
 * Created by edoras on 3/2/15.
 */
public class MessageSensorData extends Message{

    public MessageSensorData(){
        super(MessageType.SENSOR_DATA);
    }

    private Map<String, Float> data;

    public Set<String> getSensorNames(){
        return data.keySet();
    }

    public float getSensorValue(String name){
        return data.get(name);
    }

    // Raw methods, better not use!
    // TODO check if this can be private
    public Map<String, Float> getData() {
        return data;
    }

    public void setData(Map<String, Float> data) {
        this.data = data;
    }
}
