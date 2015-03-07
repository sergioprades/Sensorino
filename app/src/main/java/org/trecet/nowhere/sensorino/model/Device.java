package org.trecet.nowhere.sensorino.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Device {

    private String local_name;
    private String remote_name;
    private String remote_address;
    private String processor;
    private int frequency;
    private HashMap<String,Sensor> sensors;

    public Device() {
        this.remote_address = "";
        this.processor = "";
        this.frequency = 0;
        this.sensors = new HashMap<>();
        this.remote_name = "";
        this.local_name = "NULL";
    }

    public String getLocal_name() {
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
    }

    public String getRemote_name() {
        return remote_name;
    }

    public void setRemote_name(String remote_name) {
        this.remote_name = remote_name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getRemote_address() { return remote_address; }

    public void setRemote_address(String remote_address) { this.remote_address = remote_address; }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    // Persistence (provided by Devices
    public void persist(Context context){
        Devices devices = Devices.getInstance(context);
        devices.persist();
    }

    // Sensor stuff
    public Set<String> getSensorNames() {
        return sensors.keySet();
    }

    public Sensor getSensor(String name) {
        return sensors.get(name);
    }

    public void setSensor(String name, Sensor sensor) {
        this.sensors.put(name, sensor);
    }


}
