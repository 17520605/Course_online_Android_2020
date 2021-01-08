package Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Adapter.LessonAdapter;
import Model.LessonModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseJoinedActivity extends AppCompatActivity {

    private Toolbar TBlistlesson;
    private ImageView image;
    private TextView title;
    private ProgressBar progressBar;
    private TextView percent;
    private Button learnBtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_course);
        initUIs();
        Load();
        ActionToolBar();
    }
    private void ActionToolBar() {
        setSupportActionBar(TBlistlesson);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBlistlesson.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBlistlesson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initUIs() {
        image = findViewById(R.id.CourseJoined_Image);
        recyclerView = findViewById(R.id.CourseJoined_RecyclerView);
        title = findViewById(R.id.CourseJoined_Title);
        title.setSelected(true);
        progressBar = findViewById(R.id.CourseJoined_ProgressBar);
        percent = findViewById(R.id.CourseJoined_Percent);
        learnBtn = findViewById(R.id.CourseJoined_LearnBtn);
        TBlistlesson = findViewById(R.id.TBlistlesson);
    }


    private void Load(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        String courseName = getIntent().getStringExtra("COURSE_NAME");
        String courseImage = getIntent().getStringExtra("COURSE_IMAGE");
        Integer courseProgress = getIntent().getIntExtra("COURSE_PROGRESS", 0);

        LoadCourseInfor(courseImage, courseName, courseProgress);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);

        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetAllLessonsOfCourse("/lesson/get-lesson-by-id-course/" + courseId , user.getAuthToken())
                .enqueue(new Callback<List<LessonModel>>() {
                    @Override
                    public void onResponse(Call<List<LessonModel>> call, Response<List<LessonModel>> response) {
                            if(response.body() != null){
                                LessonAdapter adapter = new LessonAdapter(response.body());
                                recyclerView.setAdapter(adapter);
                            }
                    }

                    @Override
                    public void onFailure(Call<List<LessonModel>> call, Throwable t) {

                    }
                });
    }

    private void LoadCourseInfor(String img, String name, Integer progress){
        title.setText(name);
        progressBar.setProgress(progress);
        percent.setText(progress.toString() + "%");
        Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + img)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(image);
    }
}
