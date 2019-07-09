package com.appzone.dhai.services;

import com.appzone.dhai.models.AboutModel;
import com.appzone.dhai.models.AdsDataModel;
import com.appzone.dhai.models.BankAccountDataModel;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.NotificationCount;
import com.appzone.dhai.models.NotificationDataModel;
import com.appzone.dhai.models.OfferDataModel;
import com.appzone.dhai.models.OrderDataModel;
import com.appzone.dhai.models.PackageDataModel;
import com.appzone.dhai.models.ServiceDataModel;
import com.appzone.dhai.models.Terms_ConditionModel;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;

import java.util.List;

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
    @POST("api/login")
    Call<UserModel> SignIn(@Field("email") String phone,
                           @Field("password") String password
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUpWithImage(@Part("name") RequestBody name,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part MultipartBody.Part avatar

    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUpWithoutImage(@Part("name") RequestBody name,
                                       @Part("phone") RequestBody phone,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password
    );

    @GET("api/get-terms-condition")
    Call<Terms_ConditionModel> getTerms();

    @FormUrlEncoded
    @POST("api/fire-base-token")
    Call<ResponseBody> updateToken(@Field("token") String user_token,
                                   @Field("fire_base_token") String fire_base_token);

    @Multipart
    @POST("api/edit-profile")
    Call<UserModel> updateImage(@Part("token") RequestBody user_token,
                                @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("api/edit-profile")
    Call<UserModel> updateCv(@Part("token") RequestBody user_token,
                             @Part List<MultipartBody.Part> image_cv
    );

    @FormUrlEncoded
    @POST("api/edit-profile")
    Call<UserModel> updateName(@Field("token") String user_token,
                               @Field("name") String name
    );
    @FormUrlEncoded
    @POST("api/edit-profile")
    Call<UserModel> deltecv(@Field("token") String user_token,
                               @Field("image_id") int image_id
    );
    @FormUrlEncoded
    @POST("api/edit-profile")
    Call<UserModel> updateEmail(@Field("token") String user_token,
                                @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Field("token") String user_token);

    @GET("api/get-about-us")
    Call<AboutModel> getAboutUS();

    @GET("api/ads")
    Call<AdsDataModel> getAds();

    @GET("api/offers")
    Call<OfferDataModel> getOffers(@Query("page") int page);

    @GET("api/category/{type}/services")
    Call<ServiceDataModel> getServices(@Path("type") int type, @Query("page") int page);

    @GET("api/trainings")
    Call<TrainingDataModel> getTrainings(@Query("token") String user_token, @Query("page") int page);

    @GET("api/packages")
    Call<PackageDataModel> getPackage();

    @Multipart
    @POST("api/reserve-package")
    Call<ResponseBody> reservePackage(@Part("token") RequestBody user_token,
                                      @Part("package_id") RequestBody package_id,
                                      @Part MultipartBody.Part image
    );

    @GET("api/bank-accounts")
    Call<BankAccountDataModel> getBankAccount();

    @FormUrlEncoded
    @POST("api/contacts")
    Call<ResponseBody> sendContacts(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("message") String message
    );


    @GET("api/jobs")
    Call<JobsDataModel> getJobs(@Query("token") String user_token, @Query("page") int page);

    @FormUrlEncoded
    @POST("api/reserve-training")
    Call<ResponseBody> trainingReserve(@Field("token") String user_token,
                                       @Field("training_id") int training_id,
                                       @Field("name") String name,
                                       @Field("phone") String phone,
                                       @Field("email") String email,
                                       @Field("notes") String notes,
                                       @Field("additional_info") String additional_info,
                                       @Field("description") String description

    );

    @FormUrlEncoded
    @POST("api/reserve-service")
    Call<ResponseBody> serviceReserve(@Field("token") String user_token,
                                      @Field("service_id") int service_id,
                                      @Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("email") String email,
                                      @Field("notes") String notes,
                                      @Field("additional_info") String additional_info,
                                      @Field("description") String description

    );

    @FormUrlEncoded
    @POST("api/reserve-service")
    Call<ResponseBody> electronicServiceReserve(@Field("token") String user_token,
                                                @Field("service_id") int service_id,
                                                @Field("name") String name,
                                                @Field("phone") String phone,
                                                @Field("email") String email,
                                                @Field("notes") String notes,
                                                @Field("additional_info") String additional_info,
                                                @Field("description") String description,
                                                @Field("username") String username,
                                                @Field("password") String password

    );

    @FormUrlEncoded
    @POST("api/reserve-job")
    Call<ResponseBody> jobDataReserve(@Field("token") String user_token,
                                      @Field("job_id") int service_id,
                                      @Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("email") String email,
                                      @Field("notes") String notes,
                                      @Field("additional_info") String additional_info,
                                      @Field("description") String description,
                                      @Field("address") String address,
                                      @Field("identity") String identity,
                                      @Field("education") String education


    );

    @Multipart
    @POST("api/reserve-job")
    Call<ResponseBody> jobPDFReserve(@Part("token") RequestBody user_token,
                                     @Part("job_id") RequestBody job_id
    );

    @GET("api/notifications")
    Call<NotificationDataModel> getNotification(@Query("token") String user_token,
                                                @Query("page") int page
    );

    @GET("api/unread-notifications-count")
    Call<NotificationCount> getNotificationCount(@Query("token") String user_token);

    @FormUrlEncoded
    @POST("api/notifications")
    Call<ResponseBody> readNotifications(@Field("token") String token);

    @FormUrlEncoded
    @POST("api/me")
    Call<UserModel> getProfileData(@Field("token") String user_token);

    @GET("api/orders")
    Call<OrderDataModel> getOrders(@Query("token") String token,
                                   @Query("type") String type,
                                   @Query("page") int page
    );

    @FormUrlEncoded
    @POST("api/disable-coupon")
    Call<ResponseBody> chargeCoupon(@Field("code") String code,
                                    @Field("token") String user_token);

    @FormUrlEncoded
    @POST("api/forget")
    Call<ResponseBody> forgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("api/answer-notification")
    Call<ResponseBody> sendAnswer(@Field("answer") String answer, @Field("msg_id") int msg_id);
}
