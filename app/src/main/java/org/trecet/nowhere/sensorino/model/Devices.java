package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Devices {

    private static Devices singleton = null;

    Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
    private ArrayList<Device> deviceList = new ArrayList<Device>();

    private static Context context;

    // Private Contructor
    private Devices(){

        load();
    }

//    public static void setContext (Context ctx) {
//        context = ctx;
//    }

    public static Devices getInstance(Context ctx){

        context = ctx;

        if(singleton == null) {
            singleton = new Devices();
        }

        return singleton;

    }

    public boolean persist (){
        //Set the values
        //Gson gson = new Gson();
        //List<String> textList = new ArrayList<String>();
        //textList.addAll(data);
        Gson gson = new Gson();
        String jsonText = gson.toJson(deviceList);
        // Create the SharedPrefs editor handler
        SharedPreferences settings = context.getSharedPreferences("Prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        // And write
        editor.putString("devices", jsonText);
        Log.d("Sensorino", "Persisting devices: "+jsonText);
        editor.commit();

        return true;
    }

    private boolean load() {
        //Load from prefences
        Gson gson = new Gson();
        SharedPreferences settings = context.getSharedPreferences("Prefs", 0);
        String jsonText = settings.getString("devices", "[]");
        Log.d("Sensorino", "Loading devices: "+jsonText);
        deviceList = gson.fromJson(jsonText, listType);

        return true;
    }

    public int add (Device device){
        deviceList.add(device);
        Log.i("Sensorino", "Adding new device: " + device.getLocal_name());

        //Write to persistance
        persist();
        return 0;
    }

    public boolean remove (Device device) {
        this.deviceList.remove(device);
        Log.i("Sensorino", "Removing device: " + device.getLocal_name());
        persist();
        return true;
    }
    public int size(){ return this.deviceList.size(); }

    public Device getDeviceByPosition(int position){
        return this.deviceList.get(position);
    }

}
