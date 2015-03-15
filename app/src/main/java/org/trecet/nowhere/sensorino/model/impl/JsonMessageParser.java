package org.trecet.nowhere.sensorino.model.impl;

import org.trecet.nowhere.sensorino.model.Message;
import org.trecet.nowhere.sensorino.model.MessageParser;
import com.google.gson.Gson;

/**
 * Created by Sergio on 15/03/2015.
 */
public class JsonMessageParser implements MessageParser {

    private static Gson gson = new Gson();

    @Override
    public <T extends Message> String marshal(T message) {
        return null;



    }

    @Override
    public <T extends Message> T unmarshal(Class<T> messageClass, String message) {

        return gson.fromJson(message, messageClass);

    }
}
