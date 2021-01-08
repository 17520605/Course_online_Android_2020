package Retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface IMyService {
    @POST("login")
    @FormUrlEncoded
    Observable<Response<String>> loginUser(@Field("email") String email, @Field("password") String actToken);

    @POST("active-account")
    @FormUrlEncoded
    Observable<String>  activeAccUser(@Field("email") String email,
                                      @Field("activeToken") String actToken);

    //UserInfo Activity
    @PUT("change-profile")
    @FormUrlEncoded
    Observable<Response<String>>  changeProfile(@Field("name") String oldPass,
                                                @Field("phone") String phone,
                                                @Field("address") String address,
                                                @Field("description") String description,
                                                @Field("gender") String gender,
                                                @Header("auth-token") String authToken);


    @Multipart
    @PUT("change-avatar")
    Observable<Response<String>>  changeAva(@Part MultipartBody.Part file,
                                            @Header("auth-token") String authToken);



    @POST("forgot-password")
    @FormUrlEncoded
    Observable<String>  forgotPass(@Field("email") String email);


    @POST("reset-password")
    @FormUrlEncoded
    Observable<String>  resetPass(@Field("email") String email,
                                  @Field("token") String token,
                                  @Field("password") String pass);
}
