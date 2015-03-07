package org.trecet.nowhere.sensorino.model;

/**
 * Created by pablof on 7/03/15.
 */
public class SensorData {
    private int t;
    private float v;

    public SensorData(int t, float v) {
        this.t = t;
        this.v = v;
    }

    public int getTimestamp() {
        return t;
    }

    public void setTimestamp(int t) {
        this.t = t;
    }

    public float getValue() {
        return v;
    }

    public void setValue(float v) {
        this.v = v;
    }
}
