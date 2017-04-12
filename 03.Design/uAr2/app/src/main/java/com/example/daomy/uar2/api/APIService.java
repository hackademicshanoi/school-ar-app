package com.example.daomy.uar2.api;

import com.example.daomy.uar2.model.MSG;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 4/6/2017.
 */

public interface APIService {


    @FormUrlEncoded
    @POST("login/views/signup.php")
    Call<MSG> userSignUp(@Field("email") String email,
                         @Field("password") String password,
                         @Field("first_name") String first_name,
                         @Field("last_name") String last_name,
                         @Field("date_of_birth") String date_of_birth,
                         @Field("id_school") int id_school,
                         @Field("profile_picture") String profile_picture,
                         @Field("token") String token);

    @FormUrlEncoded
    @POST("login/views/login.php")
    Call<MSG> userLogIn(@Field("email") String email,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("login/views/checktoken.php")
    Call<MSG> checkToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("login/views/reminder.php")
    Call<MSG> reminder(@Field("email") String email);




}
