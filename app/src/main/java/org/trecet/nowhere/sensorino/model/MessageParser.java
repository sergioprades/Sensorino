package org.trecet.nowhere.sensorino.model;

/**
 * Created by Sergio on 15/03/2015.
 */
public interface MessageParser {

    public <T extends Message> String marshal(T message);

    public <T extends Message> T unmarshal(final Class<T> messageClass, String message);

}
