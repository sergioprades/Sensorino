package org.trecet.nowhere.sensorino.model;

/**
 * Created by Sergio on 15/03/2015.
 */
public class Message {

    private MessageType type = null;

    public Message(MessageType type){

        this.type = type;
    }

    public MessageType getType(){
        return type;
    }

}
