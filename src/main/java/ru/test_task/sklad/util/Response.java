package ru.test_task.sklad.util;

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

    public static String jsonResponse(Status status, JsonElement data) {
        return new Gson().toJson(new Response(status, data));
    }

    //getters and setters
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
