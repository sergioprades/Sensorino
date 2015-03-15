package org.trecet.nowhere.sensorino.message;

import org.trecet.nowhere.sensorino.model.Message;
import org.trecet.nowhere.sensorino.model.MessageType;

/**
 * Created by pablof on 4/03/15.
 */
public class MessageGetSensorInfo extends Message {

        public MessageGetSensorInfo(){
            super(MessageType.GET_SENSOR_INFO);
        }

}
