package Retrofit;

import java.util.List;

import Model.CourseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ISearchService {
    @GET
    Call<List<CourseModel>> SearchCourses(@Url String url);
}
