package com.example.gohotel.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelImageForm {

    @SerializedName("name_image")
    @Expose
    private String nameImage;
    @SerializedName("hotel_id")
    @Expose
    private int hotelId;
    @SerializedName("room_id")
    @Expose
    private int roomId;

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
