package com.sembiyan.app.network;

import java.util.ArrayList;


public class Password extends AbstractGrant {

    @Override
    public String getName() {
        return "password";
    }

    public ArrayList<String> getRequiredRequestParameters() {
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("username");
        parameters.add("password");
        parameters.add("client_id");
        parameters.add("client_secret");
        return parameters;
    }
}