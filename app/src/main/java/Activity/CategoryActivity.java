package Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.CourseOfCategoryAdapter;
import Model.ListCourseItemModel;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryActivity extends AppCompatActivity {

    Toolbar TBresulcategory;
    public RecyclerView courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setUIReference();
        ActionToolBar();
        Load();

    }
    private void setUIReference() {
        courses = findViewById(R.id.category_courses);
        TBresulcategory = findViewById(R.id.TBresulcategory);
    }

    private void ActionToolBar() {
        setSupportActionBar(TBresulcategory);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBresulcategory.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBresulcategory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Load(){
        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetCoursesByCategory("course/getby-category/" + categoryId)
                .enqueue(new Callback<List<ListCourseItemModel>>() {
                    @Override
                    public void onResponse(Call<List<ListCourseItemModel>> call, Response<List<ListCourseItemModel>> response) {
                        CourseOfCategoryAdapter adapter = new CourseOfCategoryAdapter(response.body());
                        courses.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<ListCourseItemModel>> call, Throwable t) {
                        Toasty.warning(CategoryActivity.this, "Danh mục khóa học bị lỗi .", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}