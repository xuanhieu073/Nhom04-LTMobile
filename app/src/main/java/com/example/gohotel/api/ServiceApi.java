package com.example.gohotel.api;

import com.example.gohotel.model.HotelForm;
import com.example.gohotel.model.SearchForm;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.model.api.CityForm;
import com.example.gohotel.model.api.DistrictForm;
import com.example.gohotel.model.api.HotelImageForm;
import com.example.gohotel.model.api.ResponseUserCreate;
import com.example.gohotel.model.api.RoomTypeForm;
import com.example.gohotel.model.api.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {


    @POST("/API_GoHotel/user/create.php")
    @FormUrlEncoded
    Call<ResponseUserCreate> createUser(@Field("phone") String phone, @Field("password") String password
            , @Field("birthday") String birthday, @Field("mail") String mail, @Field("gender") String gender, @Field("device_id") String device_id
            , @Header("token") String token);

    @POST("/API_GoHotel/user/checkEqualPhone.php")
    @FormUrlEncoded
    Call<ResponseUserCreate> checkEqualPhone(@Field("phone") String phone, @Header("token") String token);

    @POST("/API_GoHotel/user/login.php")
    @FormUrlEncoded
    Call<UserInfo> login(@Field("phone") String phone, @Field("password") String pasword);


    @GET("/API_GoHotel/city/get.php")
    Call<List<CityForm>> getCity();

    @POST("/API_GoHotel/district/accordingToCityId.php")
    @FormUrlEncoded
    Call<List<DistrictForm>> accordingToCityId(@Field("city_id") int city_id);

    @GET("/API_GoHotel/hotel/search.php")
    Call<List<HotelForm>> search(@Query("key") String key);

    @GET("/API_GoHotel/hotel/get.php")
    Call<List<HotelForm>> getHotelDetail(@Query("id") int id);

    @GET("/API_GoHotel/room/get.php")
    Call<List<RoomTypeForm>> getRoomTypeHotel(@Query("hotel") int id);

    @POST("/API_GoHotel/image/get.php")
    @FormUrlEncoded
    Call<List<HotelImageForm>> getImageHotel(@Field("hotel") int hotel);


    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeDistance(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("district_id") int district_id, @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_distance ") String sort_distance);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeDistance(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_distance ") String sort_distance);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeDistance(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_distance ") String sort_distance);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeStar(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("district_id") int district_id, @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_star") String sort_star);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeStar(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_star") String sort_star);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHomeStar(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_star") String sort_star);


    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHome(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_price") String sort_price);

    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHome(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_price") String sort_price);


    @POST("/API_GoHotel/hotel/getHotelHome.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelHome(@Field("latitude") String latitude
            , @Field("longitude") String longitude, @Field("limitfrom") int limitfrom
            , @Field("limitcount") int limitcount, @Field("city_id") int city_id
            , @Field("district_id") int district_id, @Field("price_start") int price_start, @Field("price_end") int price_end
            , @Field("sort_price") String sort_price);


    @POST("/API_GoHotel/hotel/getHotelMap.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelMap(@Field("latitude") String latitude, @Field("longitude") String longitude, @Field("radius") double radius);

    @POST("/API_GoHotel/hotel/getHotelMap.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelMap(@Field("latitude") String latitude, @Field("longitude") String longitude, @Field("radius") double radius, @Field("city_id") int city_id);

    @POST("/API_GoHotel/hotel/getHotelMap.php")
    @FormUrlEncoded
    Call<List<HotelForm>> getHotelMap(@Field("latitude") String latitude, @Field("longitude") String longitude, @Field("radius") double radius, @Field("city_id") int city_id, @Field("district_id") int district_id);

    @POST("/API_GoHotel/keyword/get.php")
    Call<List<SearchForm>> getKeySearch();

    @POST("/API_GoHotel/book/create.php")
    @FormUrlEncoded
    Call<BookRes> bookRoom(@Field("device_id") String device_id,@Field("hotel_id") int hotel_id, @Field("room_id") int room_id, @Field("date_start") String date_start, @Field("date_end") String date_end, @Field("price") int price, @Field("time_book") String time_book, @Field("phone") String phone, @Field("info_user") String info_user, @Header("token") String token);

    @POST("/API_GoHotel/book/get.php")
    @FormUrlEncoded
    Call<List<BookingUserForm>> getBookingDetail(@Field("id") int bookingId);

    @POST("/API_GoHotel/book/get.php")
    @FormUrlEncoded
    Call<List<BookingUserForm>> getBookingDetailByStatus(@Field("device_id ") String device_id,@Field("status") int status, @Field("date_end") String date_end);

    @POST("/API_GoHotel/book/getAccordingToToken.php")
    Call<List<BookingUserForm>> getMyBooking(@Header("token") String token);

    @POST("/API_GoHotel/book/update.php")
    @FormUrlEncoded
    Call<BookRes> updateBookingDetail(@Field("id") int bookingId, @Field("status") int status);


    @POST("/API_GoHotel/user/update.php")
    @FormUrlEncoded
    Call<BookRes> updateUser(@Header("token") String token, @Field("phone") String phone, @Field("pass") String pass, @Field("gender") String gender, @Field("mail") String mail, @Field("birthday") String birthday);

    @POST("/API_GoHotel/user/changePassword.php")
    @FormUrlEncoded
    Call<BookRes> changePassword(@Header("token") String token, @Field("phone") String phone, @Field("password_old") String password_old, @Field("password_new") String password_new);

    @POST("/API_GoHotel/review/create.php")
    @FormUrlEncoded
    Call<BookRes> reviewHotel(@Header("token") String token,@Field("hotel_id") int hotel_id, @Field("room_id") int room_id, @Field("user") String user, @Field("comment") String comment, @Field("star") int star);

}
