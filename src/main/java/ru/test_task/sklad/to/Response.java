package ru.test_task.sklad.to;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Response {
    private Status status;
    private String message;
    private JsonElement data;

    //constructors
    public Response(Status status) {
        this.status = status;
    }

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Status status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public static String jsonResponse(Status status) {
        return new Gson().toJson(new Response(status));
    }

    public static String jsonResponse(Status status, String message) {
        return new Gson().toJson(new Response(status, message));
    }

    public static String jsonResponse(Object object, Status status) {
        return new Gson().toJson(new Response(status, new Gson().toJsonTree(object)));
    }
}
