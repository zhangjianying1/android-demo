package com.example.caiyue.androidstuiodemo.http;

import com.example.caiyue.androidstuiodemo.model.AdModel;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.ChannelModel;
import com.example.caiyue.androidstuiodemo.model.IndexBannerModel;
import com.example.caiyue.androidstuiodemo.model.PersonInfo;
import com.example.caiyue.androidstuiodemo.model.SecurityModel;
import com.example.caiyue.androidstuiodemo.model.SpecialModel;
import com.example.caiyue.androidstuiodemo.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyServices {

    @Headers("Content-Type: application/json")
    @POST("ad/getAdvGameAndInfo")
    Call<BaseResponse<AdModel>> getAdInfo(@Body RequestBody os);

    @Headers("Content-Type: application/json")
    @POST("personCenter/getExtentList")
    Call<BaseResponse<List<IndexBannerModel>>> getExtentList(@Body RequestBody os);

    @GET("document/getNewestSpecial")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<SpecialModel>> getNewsetSpecial();

    @GET("personCenter/getPersonInfo")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<PersonInfo>> getPersonInfo();

    @POST("user/authCodeLogin")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<UserModel>> login(@Body RequestBody param);

    @POST("user/sendOtpMessage")
    @Headers("Content-Type: application/json")
    Call<BaseResponse> sendOtpMessage(@Body RequestBody param);

    @POST("security/getSecInfo")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<SecurityModel>> getSecInfo(@Body RequestBody param);

    @POST("user/queryUser")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<UserModel>> queryUser(@Body RequestBody param);

    @GET("document/getDocumenClassList")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<List<ChannelModel>>> getChannelList( );

    @GET("document/getFollowClassList")
    @Headers("Content-Type: application/json")
    Call<BaseResponse<List<ChannelModel>>> getFollowChannelList();
}
