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
    Call<MSG> userSignUp(@Field("name") String name,
                         @Field("email") String email,
                         @Field("password") String password);

    @FormUrlEncoded
    @POST("login/views/login.php")
    Call<MSG> userLogIn(@Field("email") String email,
                        @Field("password") String password);



}
