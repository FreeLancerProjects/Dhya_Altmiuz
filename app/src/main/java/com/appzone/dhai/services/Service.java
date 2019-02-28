package com.appzone.dhai.services;

import com.appzone.dhai.models.AboutModel;
import com.appzone.dhai.models.AdsDataModel;
import com.appzone.dhai.models.BankAccountDataModel;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.OfferDataModel;
import com.appzone.dhai.models.PackageDataModel;
import com.appzone.dhai.models.ServiceDataModel;
import com.appzone.dhai.models.Terms_ConditionModel;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @FormUrlEncoded
    @POST("/api/login")
    Call<UserModel> SignIn(@Field("phone") String phone
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUp(@Part("name") RequestBody name,
                           @Part("phone") RequestBody phone,
                           @Part("email") RequestBody email,
                           @Part MultipartBody.Part avatar

    );

    @GET("/api/get-terms-condition")
    Call<Terms_ConditionModel> getTerms();

    @FormUrlEncoded
    @POST("/api/fire-base-token")
    Call<ResponseBody> updateToken(@Field("token") String user_token,
                                   @Field("fire_base_token") String fire_base_token);
    @Multipart
    @POST("/api/edit-profile")
    Call<UserModel> updateImage(@Part("token") RequestBody user_token,
                                @Part MultipartBody.Part avatar
    );

    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updateName(@Field("token") String user_token,
                               @Field("name") String name
    );
    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updateEmail(@Field("token") String user_token,
                                @Field("email") String email
    );

    @FormUrlEncoded
    @POST("/api/logout")
    Call<ResponseBody> logout(@Field("token") String user_token);

    @GET("/api/get-about-us")
    Call<AboutModel> getAboutUS();

    @GET("/api/ads")
    Call<AdsDataModel> getAds();

    @GET("/api/offers")
    Call<OfferDataModel> getOffers(@Query("page") int page);

    @GET("/api/category/{type}/services")
    Call<ServiceDataModel> getServices(@Path("type") int type,@Query("page") int page);

    @GET("/api/trainings")
    Call<TrainingDataModel> getTrainings(@Query("token") String user_token, @Query("page") int page);

    @GET("/api/packages")
    Call<PackageDataModel> getPackage();

    @Multipart
    @POST("/api/reserve-package")
    Call<ResponseBody> reservePackage(@Part("token") RequestBody user_token,
                                      @Part("package_id") RequestBody package_id,
                                      @Part MultipartBody.Part image
                                      );

    @GET("/api/bank-accounts")
    Call<BankAccountDataModel> getBankAccount();

    @FormUrlEncoded
    @POST("/api/contacts")
    Call<ResponseBody> sendContacts(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("message") String message
                                    );


    @GET("/api/jobs")
    Call<JobsDataModel> getJobs(@Query("page") int page);

}
