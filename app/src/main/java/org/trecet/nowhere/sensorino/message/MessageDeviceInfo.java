package org.trecet.nowhere.sensorino.message;

import org.trecet.nowhere.sensorino.model.Message;
import org.trecet.nowhere.sensorino.model.MessageType;

import java.util.Map;

/**
 * Created by pablof on 4/03/15.
 */
public class MessageDeviceInfo extends Message{

    public MessageDeviceInfo(){
        super(MessageType.DEVICE_INFO);
    }

    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
