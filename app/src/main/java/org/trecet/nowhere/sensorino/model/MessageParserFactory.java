package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.trecet.nowhere.sensorino.model.impl.JsonMessageParser;
import org.trecet.nowhere.sensorino.model.impl.SharedPreferencesDevicePersistor;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Sergio on 14/03/2015.
 */
public class MessageParserFactory {

    private static MessageParserFactory singleton = null;

    private MessageParser parser = null;

    // Private Constructor
    private MessageParserFactory(){

        parser = new JsonMessageParser();

    }

    public synchronized static MessageParserFactory getInstance(){

        if(singleton == null) {
            singleton = new MessageParserFactory();
        }

        return singleton;
    }

    public MessageParser getParser() {

        return parser;
    }
}
