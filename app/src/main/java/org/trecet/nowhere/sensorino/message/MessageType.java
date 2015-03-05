package org.trecet.nowhere.sensorino.message;

/**
 * Created by edoras on 3/2/15.
 */

import com.google.gson.annotations.SerializedName;

public enum MessageType {
    @SerializedName("get_device_info")
    GET_DEVICE_INFO,
    @SerializedName("device_info")
    DEVICE_INFO,
    @SerializedName("get_sensor_info")
    GET_SENSOR_INFO,
    @SerializedName("sensor_info")
    SENSOR_INFO,
    @SerializedName("get_sensor_data")
    GET_SENSOR_DATA,
    @SerializedName("sensor_data")
    SENSOR_DATA,
    ;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
