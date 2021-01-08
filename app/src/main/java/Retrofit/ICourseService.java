package Retrofit;

import androidx.annotation.Nullable;

import java.util.List;

import Model.Category;
import Model.CategorySummaryModel;
import Model.CommentModel;
import Model.CourseModel;
import Model.CourseJoinedModel;
import Model.CourseProgressModel;
import Model.LessonModel;
import Model.LessonTestResponeModel;
import Model.ListCourseItemModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ICourseService {
    @GET
    Call<List<Category>> GetAllCategories(@Url String url);

    @GET
    Call<List<CategorySummaryModel>> GetAllCategorySummaries(@Url String url);

    @GET
    Call<List<ListCourseItemModel>> GetAllCourses(@Url String url);

    @GET
    Call<CourseModel> GetCourseById(@Url String url);

    @GET
    Call<List<CourseJoinedModel>> GetJoinedCourses(@Url String url);

    @GET
    Call<CourseModel> GetCoursesByUser(@Url String url);

    @GET
    Call<List<ListCourseItemModel>> GetCoursesByCategory(@Url String url);

    @GET("course/get-free")
    Call<List<ListCourseItemModel>> GetFreeCourses();

    @GET("course/get-top")
    Call<List<ListCourseItemModel>> GetTopCourses();

    @GET("course/get-all")
    Call<List<ListCourseItemModel>> GetAllCourses();

    @POST("/payment/pay")
    Call<Response<String>> pay (@Body RequestBody body);

    @GET
    Call<List<LessonModel>> GetAllLessonsOfCourse(@Url String url, @Header("auth-token") String authToken);

    @GET
    Call<LessonModel> GetLessonsById(@Url String url, @Header("auth-token") String authToken);

    @PUT
    Call<CourseProgressModel> UpdateLessonProgress (@Url String url);

    @GET
    Call<List<ListCourseItemModel>> GetCoursesCreatedByUser(@Url String url);

    @Multipart
    @POST("course/create")
    Call<ResponseBody> CreateCourse(@Part("name") RequestBody name,
                                    @Part("goal") RequestBody goal,
                                    @Part("description") RequestBody description,
                                    @Part("category") RequestBody category,
                                    @Part("price") RequestBody price,
                                    @Part("discount") RequestBody discount,
                                    @Part MultipartBody.Part image,
                                    @Header("auth-token") String authToken);

    @Multipart
    @PUT
    Call<ResponseBody>  UpdateCourse(   @Url String url,
                                        @Part("name") RequestBody name,
                                        @Part("goal") RequestBody goal,
                                        @Part("description") RequestBody description,
                                        @Part("category") RequestBody category,
                                        @Part("price") RequestBody price,
                                        @Part("discount") RequestBody discount,
                                        @Part @Nullable MultipartBody.Part file,
                                        @Header("auth-token") String authToken);

    @Multipart
    @POST("lesson/create-lesson")
    Call<ResponseBody> CreateLesson(@Part("title") String title,
                                    @Part("order") String order,
                                    @Part("idCourse") String courseid,
                                    @Part("videos") MultipartBody.Part videos,
                                    @Part("docs") MultipartBody.Part docs,
                                    @Header("auth-token") String authToken);
    @PUT
    @FormUrlEncoded
    Call<ResponseBody>  UpdateLesson(   @Url String urlGet,
                                         @Field("idCourse") String idcourse,
                                         @Field("order") String order,
                                         @Field("title") String title,
                                         @Header("auth-token") String authToken);

    @POST("forgot-password")
    @FormUrlEncoded
    Call<ResponseBody>  ForgotPassword(@Field("email") String email);

    @POST("reset-password")
    @FormUrlEncoded
    Call<ResponseBody>  RecoveryPassword(@Field("email") String email,
                                         @Field("token") String token,
                                         @Field("password") String password);

    @POST("join/create-join")
    @FormUrlEncoded
    Call<ResponseBody>  JoinCourse( @Field("idUser") String userId,
                                    @Field("idCourse") String courseId);

    @POST("rate/create-rate")
    @FormUrlEncoded
    Call<ResponseBody>  RateCourse( @Field("idUser") String userId,
                                    @Field("idCourse") String courseId,
                                    @Field("content") String content,
                                    @Field("numStar") float star,
                                    @Header("auth-token") String authToken);

    @GET
    Call<List<CommentModel>> GetALLCommentOfCourse(@Url String url);

    @POST("lesson/upload-image-multiple-choice")
    @Multipart
    Call<ResponseBody> UploadTestImage(   @Part MultipartBody.Part image,
                                          @Header("auth-token") String authToken);

    @PUT
    @FormUrlEncoded
    Call<ResponseBody>  CreateTest( @Url String url,
                                    @Field("multipleChoices") String multipleChoices,
                                    @Header("auth-token") String authToken);

    @PUT
    Call<ResponseBody> UpdateTest (     @Url String url,
                                            @Body RequestBody body,
                                            @Header("auth-token") String authToken);

    @DELETE
    Call<ResponseBody> DeleteLesson(@Url String url, @Header("auth-token") String authToken);

    @DELETE
    Call<ResponseBody> DeleteCourse(@Url String url, @Header("auth-token") String authToken);

    @GET
    Call<List<LessonTestResponeModel>> GetTestOfLesson(@Url String url  );


}
