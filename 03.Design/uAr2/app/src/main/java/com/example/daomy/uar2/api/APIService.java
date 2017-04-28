package com.example.daomy.uar2.api;

import com.example.daomy.uar2.model.MSG;
import com.example.daomy.uar2.model.School;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by User on 4/6/2017.
 */

public interface APIService {

// success và message
    @FormUrlEncoded
    @POST("login/views/signup.php")
    Call<MSG> userSignUp(@Field("email") String email,
                         @Field("password") String password,
                         @Field("first_name") String first_name,
                         @Field("last_name") String last_name,
                         @Field("date_of_birth") String date_of_birth,
<<<<<<< HEAD
                         @Field("id_school") int id_school);
    // success và message
=======
                         @Field("id_school") int id_school,
                         @Field("profile_picture") String profile_picture,
                         @Field("token") String token);

>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register
    @FormUrlEncoded
    @POST("login/views/login.php")
    Call<MSG> userLogIn(@Field("email") String email,
                        @Field("password") String password);
    // success và message
    @FormUrlEncoded
    @POST("login/views/checktoken.php")
    Call<MSG> checkToken(@Field("token") String token);
    // success và message
    @FormUrlEncoded
    @POST("login/views/reminder.php")
    Call<MSG> reminder(@Field("email") String email);

    @FormUrlEncoded
    @POST("login/views/returndata.php")
    Call<MSG> returnData(@Field("token") String token);

    @FormUrlEncoded
    @POST("login/views/editinfo")
    Call<MSG> editInfo(@Field("first_name") String first_name,
                       @Field("last_name") String last_name,
                       @Field("date_of_birth") String date_of_birth,
                       @Field("id_school") int id_school,
                       @Field("id_school2") int id_school2,
                       @Field("password") String password,
                       @Field("token") String token);
    @FormUrlEncoded
    @POST("login/user/connection.php")
    Call<MSG> setImage(@Field("token") String token,
                       @Field("encoded_string")String encoded_string,
                       @Field("image_name")String image_name);



}
