package com.addukkan.services;

import com.addukkan.models.ALLProductDataModel;
import com.addukkan.models.CountryDataModel;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.PlaceGeocodeData;
import com.addukkan.models.PlaceMapDetailsData;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.SliderDataModel;
import com.addukkan.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone,
                          @Field("password") String password);


    @FormUrlEncoded
    @POST("api/client-register")
    Call<UserModel> signUp(@Field("name") String name,
                           @Field("phone_code") String phone_code,
                           @Field("phone") String phone,
                           @Field("password") String password,
                           @Field("software_type") String software_type,
                           @Field("country_code") String country_code

    );

    @FormUrlEncoded
    @POST("api/update-client-profile")
    Call<UserModel> updateProfile(@Header("Authorization") String bearer_token,
                                  @Field("user_id") int user_id,
                                  @Field("name") String name,
                                  @Field("phone_code") String phone_code,
                                  @Field("phone") String phone,
                                  @Field("password") String password,
                                  @Field("software_type") String software_type,
                                  @Field("country_code") String country_code
    );

    @GET("api/setting-country")
    Call<CountryDataModel> getCountries(@Header("lang") String lang);


    @FormUrlEncoded
    @POST("api/update-firebase")
    Call<ResponseModel> updateFirebaseToken(@Header("Authorization") String token,
                                            @Field("user_id") int user_id,
                                            @Field("phone_token") String phone_token,
                                            @Field("software_type") String software_type

    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseModel> logout(@Header("Authorization") String token,
                               @Field("user_id") int user_id,
                               @Field("phone_token") String phone_token,
                               @Field("software_type") String software_type

    );

    @GET("api/slider")
    Call<SliderDataModel> get_slider(
            @Header("lang") String lang,
            @Query("type") String type,
            @Query("country_code") String country_code);

    @GET("api/main-departments")
    Call<MainCategoryDataModel> get_category(
            @Header("lang") String lang

    );

    @GET("api/main-department-sub-dep-product")
    Call<MainCategoryDataModel> get_categorySubProduct(
            @Header("lang") String lang,
            @Query("user_id") String user_id,
            @Query("country_code") String country_code

    );

    @GET("api/recently-arrived")
    Call<ALLProductDataModel> getRecentlyArrived(
            @Header("lang") String lang,
            @Query("user_id") String user_id,
            @Query("country_code") String country_code,
            @Query("pagination") String pagination

    );

    @GET("api/best-seller")
    Call<ALLProductDataModel> getMostSell(
            @Header("lang") String lang,
            @Query("user_id") String user_id,
            @Query("country_code") String country_code,
            @Query("pagination") String pagination

    );

    @GET("api/side-menu")
    Call<MainCategoryDataModel> getSideMenu(
            @Header("lang") String lang

    );

    @GET("api/offers")
    Call<ALLProductDataModel> getOffers(
            @Header("lang") String lang,
            @Query("user_id") String user_id,
            @Query("country_code") String country_code,
            @Query("pagination") String pagination

    );

    @GET("api/my-favorites")
    Call<ALLProductDataModel> getFavoutite(
            @Header("Authorization") String Authorization,
            @Header("lang") String lang,
            @Query("user_id") String user_id,
            @Query("country_code") String country_code,
            @Query("pagination") String pagination

    );

    @FormUrlEncoded
    @POST("api/action-favorite")
    Call<ResponseModel> addFavoriteProduct(
            @Header("Authorization") String Authorization,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id)


            ;
}