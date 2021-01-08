package Retrofit;

import java.util.List;

import Model.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ICategoryService {
    @GET
    Call<List<Category>> GetAllCategories(@Url String url);

}
