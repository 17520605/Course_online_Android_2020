package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.LessonCreatedAdapter;
import Model.LessonModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLessonCreatedActivity extends AppCompatActivity {

    private Toolbar TBcreatelesson;
    private Button createBtn;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);
        initUIs();
        initEvents();
        Load();
        ActionToolBar();
    }

    private void initUIs() {
        TBcreatelesson = findViewById(R.id.TBcreatelesson);
        createBtn = findViewById(R.id.ListLessonCreated_CreateBtn);
        recyclerView = findViewById(R.id.ListLessonCreated_RecyclerView);
    }

    private void initEvents(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListLessonCreatedActivity.this, CreateLessonActivity.class);
                intent.putExtra("COURSE_ID", getIntent().getStringExtra("COURSE_ID"));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
            }
        });
    }

    public void Load(){
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        String courseId = getIntent().getStringExtra("COURSE_ID");
        service.GetAllLessonsOfCourse("lesson/get-lesson-by-id-course/" +courseId, user.getAuthToken())
                .enqueue(new Callback<List<LessonModel>>() {
                    @Override
                    public void onResponse(Call<List<LessonModel>> call, Response<List<LessonModel>> response) {
                        if(response.isSuccessful()){
                            LessonCreatedAdapter adapter = new LessonCreatedAdapter(response.body(), courseId);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LessonModel>> call, Throwable t) {

                    }
                });

    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatelesson);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatelesson.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TBcreatelesson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
