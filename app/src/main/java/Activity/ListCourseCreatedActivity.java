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

import Adapter.CourseCreatedAdapter;
import Model.ListCourseItemModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCourseCreatedActivity extends AppCompatActivity {

    private Toolbar TBcreatecourse;
    private Button create_new_course_btn;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_courses);
        initUIs();
        initEvents();
        Load();
        ActionToolBar();
    }

    private void initUIs() {
        TBcreatecourse = findViewById(R.id.TBcreatecourse);
        create_new_course_btn = findViewById(R.id.create_new_course_btn);
        recyclerView = findViewById(R.id.ListCourseCreated_RecyclerView);
    }

    private void initEvents(){
        create_new_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListCourseCreatedActivity.this,CreateCourseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
            }
        });
    }

    public void Load(){
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        service.GetCoursesCreatedByUser("/course/getby-iduser/" + user.get_id())
                .enqueue(new Callback<List<ListCourseItemModel>>() {
                    @Override
                    public void onResponse(Call<List<ListCourseItemModel>> call, Response<List<ListCourseItemModel>> response) {
                        if(response.isSuccessful()){
                            CourseCreatedAdapter adapter = new CourseCreatedAdapter(response.body());
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListCourseItemModel>> call, Throwable t) {

                    }
                });

    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatecourse);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatecourse.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TBcreatecourse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
