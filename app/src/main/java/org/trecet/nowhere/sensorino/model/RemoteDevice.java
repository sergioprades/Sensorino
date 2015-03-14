package org.trecet.nowhere.sensorino.model;

import android.content.Context;

/**
 * Created by Sergio on 14/03/2015.
 */
public abstract class RemoteDevice {

    public interface Command {
        public void onSuccess();
        public void onFailure();
    }

    public interface Connection {
        public void onConnected();
        public void onDisconnected();
    }

    public interface Reception {
        public void onDataReceived(String message);
    }

    protected Device device;
    protected Context context;
    private int uptime;

    public RemoteDevice(Device device, Context context) {
        this.device = device;
        this.context = context;
    }

    public int getUptime(){
        return uptime;
    }

    public void setUptime(int uptime){
        this.uptime = uptime;
    }

    public Device getDevice(){
        return device;
    }

    public Context getContext(){
        return context;
    }

    public abstract void disconnect();

    public abstract void connect(final Connection connection);

    public abstract void send(String message);

    public abstract void onDataReceived(Reception reception);
}
