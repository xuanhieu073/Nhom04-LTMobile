package com.example.gohotel.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookRes {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private int result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
