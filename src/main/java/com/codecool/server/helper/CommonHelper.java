package com.codecool.server.helper;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class CommonHelper {

    public void redirectToUserPage(HttpExchange httpExchange, String s) throws IOException {
        httpExchange.getResponseHeaders().set("Location", s);
        httpExchange.sendResponseHeaders(303, -1);
    }

    public void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    public Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    public Map<String, String> parseURI (String uri) {
        Map<String, String> parsedURI = new HashMap<>();
        String[] uriParts = uri.split("/");
        String action;
        String data;
        if(uriParts.length > 3) {
            action = uriParts[2];
            data = uriParts[3];
            parsedURI.put(action, data);

        } else if(uriParts.length > 2) {
            action = uriParts[2];
            data = "0";
            parsedURI.put(action, data);
        }

        return parsedURI;
    }

}
