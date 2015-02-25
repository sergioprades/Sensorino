package org.trecet.nowhere.sensorino.model;

import java.io.Serializable;

public class Device implements Serializable{

    private String local_name;
    private String remote_name;
    private String remote_address;
    private String processor;
    private int frequency;

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
}
