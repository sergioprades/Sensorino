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
    private RemoteDeviceType remote_type;
    private String processor;
    private int frequency;
    private HashMap<String,Sensor> sensors;

    public Device() {
        this.remote_address = "";
        this.processor = "";
        this.frequency = 0;
        this.sensors = new HashMap<String, Sensor>();
        this.remote_name = "";
        this.local_name = "NULL";
        this.remote_type = RemoteDeviceType.BLUETOOTH; // by default TODO remove later
    }

    //FIXME Una vez un Sensor está creado tiene sentido que se le pueda cambiar el tipo?
    //O planteado de otra forma ¿el tipo será un atributo no inherente al device desde su creación?
    //Aquellos atributos que sean inmutables deben ser declarados con final
    // private final RemoteDeviceType remote_type;
    // Y los valores seteados en el constructor a través de parámetros
    //     public Device(RemoteDeviceType remote_type)
    //     this.remote_type = remote_type;
    // los getters y setters no se crean todos por defecto sino cuando los necesitas.

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

    public RemoteDeviceType getRemote_type() {
        return remote_type;
    }

    public void setRemote_type(RemoteDeviceType remote_type) {
        this.remote_type = remote_type;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    //FIXME ¿Esta relación con Devices por qué? Device es un objeto indepediente, esta lógica de
    // negocio se debe hacer desde el sitio que lo necesite. No acoples los objetos si no lo
    // necesitan

    // Persistence (provided by Devices
 //   public void persist(Context context){
 //       Devices devices = Devices.getInstance(context);
 //       devices.persist();
 //   }

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
