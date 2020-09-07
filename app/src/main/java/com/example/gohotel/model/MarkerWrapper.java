package com.example.gohotel.model;

import com.google.android.gms.maps.model.Marker;

public class MarkerWrapper {

    private HotelForm hotelForm;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public HotelForm getHotelForm() {
        return hotelForm;
    }

    public void setHotelForm(HotelForm hotelForm) {
        this.hotelForm = hotelForm;
    }

    private Marker marker;

    public MarkerWrapper(HotelForm hotelForm, Marker marker){
        this.hotelForm = hotelForm;
        this.marker = marker;
    }
}
