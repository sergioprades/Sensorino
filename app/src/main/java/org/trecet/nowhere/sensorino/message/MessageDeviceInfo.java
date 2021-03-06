package org.trecet.nowhere.sensorino.message;

import java.util.Map;

/**
 * Created by pablof on 4/03/15.
 */
public class MessageDeviceInfo {
    private MessageType type = MessageType.DEVICE_INFO;

    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
