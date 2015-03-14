package org.trecet.nowhere.sensorino.model.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.DevicePersistor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPreferencesDevicePersistor implements DevicePersistor    {

    Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
    private ArrayList<Device> deviceList = new ArrayList<Device>();

    private SharedPreferences settings;

    public SharedPreferencesDevicePersistor(SharedPreferences preferences){

        this.settings = preferences;

        load();
    }

    @Override
    public boolean persist (){
        //Set the values
        //Gson gson = new Gson();
        //List<String> textList = new ArrayList<String>();
        //textList.addAll(data);
        Gson gson = new Gson();
        String jsonText = gson.toJson(deviceList);
        // Create the SharedPrefs editor handler

        SharedPreferences.Editor editor = settings.edit();
        // And write
        editor.putString("devices", jsonText);
        Log.d("Sensorino", "Persisting devices: "+jsonText);
        return editor.commit();

        //FIXME Si el propio editor.commit devuelve un boolean, no enmascares su valor con un return
        // true, te estás anulando la posibilidad de hacer un futuro control de errores en el commit
        //return true;
    }

    private boolean load() {
        //Load from prefences
        Gson gson = new Gson();

        String jsonText = settings.getString("devices", "[]");
        Log.d("Sensorino", "Loading devices: "+jsonText);
        deviceList = gson.fromJson(jsonText, listType);

        return true;
    }

    @Override
    public boolean add (Device device){
        deviceList.add(device);
        Log.i("Sensorino", "Adding new device: " + device.getLocal_name());

        //Write to persistance
        return persist();

        //FIXME devolvemos directamente el resultado de persist
        //return 0;
    }

    @Override
    public boolean remove (Device device) {
        this.deviceList.remove(device);
        Log.i("Sensorino", "Removing device: " + device.getLocal_name());
        return persist();

        //FIXME devolvemos directamente el resultado de persist
        //return true;
    }

    @Override
    public int size(){ return this.deviceList.size(); }

    @Override
    public Device getDeviceByPosition(int position){
        return this.deviceList.get(position);
    }

    @Override
    public List<Device> listDevices(){
        return this.deviceList;
    }

}
