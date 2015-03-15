package org.trecet.nowhere.sensorino.message;

import org.trecet.nowhere.sensorino.model.Message;
import org.trecet.nowhere.sensorino.model.MessageType;

/**
 * Created by edoras on 3/2/15.
 */
public class MessageGetSensorData extends Message{

    public MessageGetSensorData(){
        super(MessageType.GET_SENSOR_DATA);
    }

}
