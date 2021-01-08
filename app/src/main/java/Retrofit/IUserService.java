package Retrofit;

import Model.ChangeProfileResponeModel;
import Model.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface IUserService {
    @POST("register")
    @FormUrlEncoded
    Call<User> register(@Field("name") String name,
                        @Field("password") String password,
                        @Field("phone") String phone,
                        @Field("address") String address,
                        @Field("description") String description,
                        @Field("gender") String gender,
                        @Field("email") String email);


    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email,
                     @Field("password") String password);


    @PUT("change-profile")
    @FormUrlEncoded
    Call<ChangeProfileResponeModel> change(@Field("name") String name,
                                           @Field("phone") String phone,
                                           @Field("address") String address,
                                           @Field("description") String description,
                                           @Field("gender") String gender,
                                           @Header("auth-token") String token);


    @POST("active-account")
    @FormUrlEncoded
    Call<ResponseBody> active( @Field("email") String email,
                         @Field("activeToken") String activeToken);



    @PUT("change-password")
    @FormUrlEncoded
    Call<ChangeProfileResponeModel> changepassword ( @Field("oldpassword") String oldpass,
                                                     @Field("newpassword") String newpass,
                                                     @Header("auth-token") String authtoken);



    @Multipart
    @PUT("change-avatar")
    Call<ResponseBody> changeavatar(@Part MultipartBody.Part file,
                                    @Header("auth-token") String authtoken);



}
