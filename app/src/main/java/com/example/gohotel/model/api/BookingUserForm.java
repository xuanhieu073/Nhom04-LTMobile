package com.example.gohotel.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingUserForm {

    @SerializedName("id_book")
    @Expose
    private int idBook;
    @SerializedName("name_hotel")
    @Expose
    private String nameHotel;
    @SerializedName("name_room")
    @Expose
    private String nameRoom;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("date_start")
    @Expose
    private String dateStart;
    @SerializedName("date_end")
    @Expose
    private String dateEnd;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("time_book")
    @Expose
    private String timeBook;
    @SerializedName("link_image")
    @Expose
    private String linkImage;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("info_user_booked")
    @Expose
    private String infoUserBooked;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("hotel_id")
    @Expose
    private int hotelId;
    @SerializedName("room_id")
    @Expose
    private int roomId;
    @SerializedName("reviewed")
    @Expose
    private String reviewed;
    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getNameHotel() {
        return nameHotel;
    }

    public void setNameHotel(String nameHotel) {
        this.nameHotel = nameHotel;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTimeBook() {
        return timeBook;
    }

    public void setTimeBook(String timeBook) {
        this.timeBook = timeBook;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfoUserBooked() {
        return infoUserBooked;
    }

    public void setInfoUserBooked(String infoUserBooked) {
        this.infoUserBooked = infoUserBooked;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
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

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }
}
