package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public interface DevicePersistor {

    public boolean persist ();

    public boolean add (Device device);

    public boolean remove (Device device);

    public int size();

    public List<Device> listDevices();

    public Device getDeviceByPosition(int position);

}
