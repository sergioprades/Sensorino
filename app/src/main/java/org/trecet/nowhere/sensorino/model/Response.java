package org.trecet.nowhere.sensorino.model;

import com.google.gson.Gson;

/**
 * Created by edoras on 2/22/15.
 */
public class Response {
    private String raw_response;
    private String type;

    public Response(String raw_response) {
        this.raw_response = raw_response;
        Gson gson = new Gson();
        type = gson.fromJson(raw_response, Response.class).type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
