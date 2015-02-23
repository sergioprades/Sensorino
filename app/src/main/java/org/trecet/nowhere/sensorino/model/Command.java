package org.trecet.nowhere.sensorino.model;

import com.google.gson.Gson;

public class Command {
    private String type = "command";  // It's the type of message
    private String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSerial() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(this);
        return jsonText;
    }
}
