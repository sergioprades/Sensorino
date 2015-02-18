package org.trecet.nowhere.sensorino.model;

import java.io.Serializable;

/**
 * Created by pablof on 13/02/15.
 */
public class Device implements Serializable{

    private String local_name;
    private String remote_name;
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
}
