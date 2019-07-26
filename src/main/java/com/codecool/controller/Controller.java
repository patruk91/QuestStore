package com.codecool.controller;

import com.codecool.server.Server;

import java.io.IOException;

public class Controller {
    private Server server;


    public Controller(Server server) {
        this.server = server;
    }

    public void run() {
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
