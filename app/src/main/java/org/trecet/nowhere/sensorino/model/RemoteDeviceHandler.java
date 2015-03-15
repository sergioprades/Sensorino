package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.util.Log;

import org.trecet.nowhere.sensorino.message.MessageDeviceInfo;
import org.trecet.nowhere.sensorino.message.MessageGetDeviceInfo;
import org.trecet.nowhere.sensorino.message.MessageGetSensorData;
import org.trecet.nowhere.sensorino.message.MessageGetSensorInfo;
import org.trecet.nowhere.sensorino.message.MessageSensorData;
import org.trecet.nowhere.sensorino.message.MessageSensorInfo;

import org.trecet.nowhere.sensorino.model.RemoteDevice;

/**
 * Created by edoras on 3/2/15.
 */
public class RemoteDeviceHandler {

        // TODO we probably need to get all the rest out of this class
        public static void getSensorData(final RemoteDevice remoteDevice, final RemoteDevice.Command command) {
            // Expected receive:
            // {"type":"sensor_data","data":{"sensor_1":25.0,"sensor_2":45.0}}
            remoteDevice.onDataReceived(new RemoteDevice.Reception() {
                @Override
                public void onDataReceived(String msg_receive) {
                    // Do something when data incoming
                    Log.d("Sensorino", "Received: " + msg_receive);
                    // TODO check message is of the right type (exception?)
                    MessageSensorData message = MessageParserFactory.getInstance().getParser().unmarshal(MessageSensorData.class, msg_receive);

                    Log.i("Sensorino", "Received data: " + message.getData().toString());

                    // TODO Separate this... we may not want to insert it into the historical data
                    for (String name : message.getSensorNames()) {
                        //TODO need to check the sensor actually exists
                        SensorData sensorData = new SensorData((int) (System.currentTimeMillis() / 1000L),
                                message.getSensorValue(name));
                        remoteDevice.getDevice().getSensor(name).addSensorData(sensorData);
                    }

                    //FIXME En vez de llamar al device.persist invocamos al devices.persist y desacoplamos
                    DevicePersistor devices = DevicePersistorFactory.getInstance().getDevicePersistor(remoteDevice.getContext());
                    devices.persist();

                    // And finally call the callback before ending
                    command.onSuccess();
                }
            });

            // Send { "type": "get_sensor_data" }
            MessageGetSensorData msg_send = new MessageGetSensorData();
            String msg_send_string = MessageParserFactory.getInstance().getParser().marshal(msg_send);

            Log.d("Sensorino", "Sent: " + msg_send_string);
            remoteDevice.send(msg_send_string);
        }

        public static void getSensorInfo(final RemoteDevice remoteDevice, final RemoteDevice.Command command) {
            // Expected receive:
            // {"type":"sensor_info","data":{"sensor_1":"temperature_C","sensor_2":"humidity_p100"}}
            remoteDevice.onDataReceived(new RemoteDevice.Reception() {
                @Override
                public void onDataReceived(String msg_receive) {
                    // Do something when data incoming
                    Log.d("Sensorino", "Received: " + msg_receive);
                    //TODO check message is of the right type (exception?)
                    MessageSensorInfo message = MessageParserFactory.getInstance().getParser().unmarshal(MessageSensorInfo.class, msg_receive);

                    for (String name : message.getSensorNames()) {
                        if (!remoteDevice.getDevice().getSensorNames().contains(name)) {
                            // Create a new sensor
                            Log.i("Sensorino", "Found new device " + name);
                            Sensor sensor = new Sensor(name, message.getSensorType(name));
                            remoteDevice.getDevice().setSensor(name, sensor);

                            DevicePersistor devices = DevicePersistorFactory.getInstance().getDevicePersistor(remoteDevice.getContext());
                            devices.persist();

                        }
                        // TODO check if the device existed but with a different type
                    }
                    command.onSuccess();
                }
            });

            // Send { "type": "get_sensor_info" }
            MessageGetSensorInfo msg_send = new MessageGetSensorInfo();
            String msg_send_string = MessageParserFactory.getInstance().getParser().marshal(msg_send);
            Log.d("Sensorino", "Sent: " + msg_send_string);
            remoteDevice.send(msg_send_string);
        }

        public static void getDeviceInfo(final RemoteDevice remoteDevice, final RemoteDevice.Command command) {
            // Expected receive:
            // {"type":"device_info","data":{"model":"Arduino Pro Mini",
            //   "processor":"Atmega328P-AU","uptime_s":15},
            remoteDevice.onDataReceived(new RemoteDevice.Reception() {
                @Override
                public void onDataReceived(String msg_receive) {
                    // Do something when data incoming
                    Log.d("Sensorino", "Received: " + msg_receive);
                    // TODO check message is of the right type (exception?)
                    MessageDeviceInfo message = MessageParserFactory.getInstance().getParser().unmarshal(MessageDeviceInfo.class, msg_receive);
                    // TODO make the getData private here as well if possible (like MessageSensorInfo)
                    remoteDevice.setUptime(Integer.parseInt(message.getData().get("uptime_s")));

                    command.onSuccess();
                }
            });

            // Send { "type": "get_device_info" }
            MessageGetDeviceInfo msg_send = new MessageGetDeviceInfo();
            String msg_send_string = MessageParserFactory.getInstance().getParser().marshal(msg_send);
            Log.d("Sensorino", "Sent: " + msg_send_string);
            remoteDevice.send(msg_send_string);
        }

    }
