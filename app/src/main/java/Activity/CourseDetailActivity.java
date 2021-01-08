package Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import Adapter.CourseOfCategoryAdapter;
import Model.CategorySummaryModel;
import Model.CourseModel;
import Model.ListCourseItemModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourseDetailActivity extends AppCompatActivity {
    private CourseModel _model;
    Toolbar TBcoursedetail;
    private Button upCourseDetail;
    private Button deleteBtn;
    private ImageView image;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setUIReference();
        initEvents();
        ActionToolBar();
        load();
    }
    private void setUIReference() {
        upCourseDetail = findViewById(R.id.Detailcourse_updatecourseBtn);
        deleteBtn = findViewById(R.id.Detailcourse_deletecourseBtn);
        TBcoursedetail = findViewById(R.id.TBcoursedetail);
        image = findViewById(R.id.DetailCourse_Image);
        name = findViewById(R.id.DetailCourse_name);
    }
    private void ActionToolBar() {
        setSupportActionBar(TBcoursedetail);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcoursedetail.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcoursedetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initEvents(){
        upCourseDetail.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailActivity.this, UpdateCourseActivity.class);
            intent.putExtra("COURSE_ID", getIntent().getStringExtra("COURSE_ID"));
            startActivity(intent);
            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            User user =  Ultitities.GetUser(pref);
            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.DeleteCourse("course/delete/" + getIntent().getStringExtra("COURSE_ID"), user.getAuthToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Toasty.success(context, "Xóa khóa học thành công", Toast.LENGTH_SHORT, true).show();
                                Context context = v.getContext();
                                Intent intent=new Intent(context, ListCourseCreatedActivity.class);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        });
    }

    private void load(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        // Laod thong tin khoa hoc
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetCourseById("course/getbyid/" + courseId).enqueue(new Callback<CourseModel>() {
            @Override
            public void onResponse(Call<CourseModel> call, Response<CourseModel> response) {
                if(response.isSuccessful()){
                    CourseModel course = response.body();
                    Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.getImage())
                            .placeholder(R.drawable.loading2)
                            .error(R.drawable.loading2)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(image);
                    name.setText(course.getName());
                    name.setSelected(true);
                }
            }
            @Override
            public void onFailure(Call<CourseModel> call, Throwable t) {
                Toasty.warning(CourseDetailActivity.this, "Lỗi hệ thống máy chủ !", Toast.LENGTH_SHORT, true).show();
            }
        });

    }


}