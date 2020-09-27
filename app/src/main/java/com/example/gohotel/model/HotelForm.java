package com.example.gohotel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

<!--Create by: Tan-->
<!--Description: HotelForm-->
public class HotelForm {
    @SerializedName("id_hotel")
    @Expose
    private int idHotel;
    @SerializedName("name_hotel")
    @Expose
    private String nameHotel;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("price_room_per_day")
    @Expose
    private int priceRoomPerDay;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("link_image")
    @Expose
    private String linkImage;
    @SerializedName("hotel_id")
    @Expose
    private int hotelId;
    @SerializedName("room_id")
    @Expose
    private int roomId;
    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("check_in")
    @Expose
    private String checkIn;

    @SerializedName("check_out")
    @Expose
    private String checkOut;

    @SerializedName("count_star")
    @Expose
    private double countStar;

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getNameHotel() {
        if (nameHotel == null) return "";
        return nameHotel;
    }

    public void setNameHotel(String nameHotel) {
        this.nameHotel = nameHotel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getPriceRoomPerDay() {
        return priceRoomPerDay;
    }

    public void setPriceRoomPerDay(int priceRoomPerDay) {
        this.priceRoomPerDay = priceRoomPerDay;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNameImage() {
        if (linkImage == null) return "";
        return linkImage;
    }

    public void setNameImage(String nameImage) {
        this.linkImage = nameImage;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public double getCountStar() {
        return countStar;
    }

    public void setCountStar(double countStar) {
        this.countStar = countStar;
    }
}
