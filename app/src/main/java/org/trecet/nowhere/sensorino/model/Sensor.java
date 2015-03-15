package org.trecet.nowhere.sensorino.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pablof on 7/03/15.
 */
public class Sensor {
    private String name;
    private SensorType type;
    private ArrayList<SensorData> data;   //timestamp, value

    public enum SensorType {
        @SerializedName("temperature_c")
        TEMPERATURE_C,
        @SerializedName("temperature_f")
        TEMPERATURE_F,
        @SerializedName("humidity_p100")
        HUMIDITY_P100,
        @SerializedName("unknown")
        UNKNOWN,
        ;

        /* This is the same as valueOf
        public SensorType fromString(String string){
            switch (string){
                case "temperature_c": return TEMPERATURE_C;
                case "temperature_f": return TEMPERATURE_F;
                case "humidity_p100": return HUMIDITY_P100;
                default: return UNKNOWN;
            }
        }
        */
    }

    public Sensor(String name, SensorType type) {
        this.name = name;
        this.type = type;
        this.data = new ArrayList<SensorData>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public void addSensorData(SensorData item){
        data.add(item);
    }

    // Override SensorData type creation with this:
    public void addSensorData(int timestamp, float value){
        data.add(new SensorData(timestamp,value));


    }

    public ArrayList<SensorData> getData (){
        return data;
    }

}