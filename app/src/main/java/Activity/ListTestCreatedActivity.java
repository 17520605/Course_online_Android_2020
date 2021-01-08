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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import Adapter.TestCreatedAdapter;
import Model.LessonModel;
import Model.MutipleChoiceModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTestCreatedActivity extends AppCompatActivity {

    private Toolbar TBcreatetestlist;
    private Button createBtn;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test_created);
        initUIs();
        initEvents();
        Load();
        ActionToolBar();
    }

    private void initUIs() {
        TBcreatetestlist = findViewById(R.id.TBcreatetestlist);
        createBtn = findViewById(R.id.ListTestCreated_CreateBtn);
        recyclerView = findViewById(R.id.ListTestCreated_RecyclerView);
    }

    private void initEvents(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListTestCreatedActivity.this, CreateTestActivity.class);
                intent.putExtra("COURSE_ID", getIntent().getStringExtra("COURSE_ID"));
                intent.putExtra("LESSON_ID", getIntent().getStringExtra("LESSON_ID"));
                intent.putExtra("TESTS", getIntent().getStringExtra("TESTS"));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
            }
        });
    }

    private void Load(){
        if(getIntent().hasExtra("TESTS")){ // load from extra data
            try {
                JSONArray arr = new JSONArray(getIntent().getStringExtra("TESTS"));
                List<MutipleChoiceModel> tests = new Gson().fromJson(arr.toString(), new TypeToken<List<MutipleChoiceModel>>() {}.getType());
                TestCreatedAdapter adapter = new TestCreatedAdapter(
                        tests,
                        getIntent().getStringExtra("COURSE_ID"),
                        getIntent().getStringExtra("LESSON_ID")
                );
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else // load from API
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            User user = Ultitities.GetUser(pref);
            String lessonId = getIntent().getStringExtra("LESSON_ID");

            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.GetLessonsById("/lesson/get-lesson-by-id/" + lessonId, user.getAuthToken())
                    .enqueue(new Callback<LessonModel>() {
                        @Override
                        public void onResponse(Call<LessonModel> call, Response<LessonModel> response) {
                            if(response.isSuccessful() && response.body().multipleChoices != null){
                                List<MutipleChoiceModel> tests = response.body().multipleChoices;
                                getIntent().putExtra("TESTS", new Gson().toJson(tests));
                                TestCreatedAdapter adapter = new TestCreatedAdapter(
                                        tests,
                                        getIntent().getStringExtra("COURSE_ID"),
                                        getIntent().getStringExtra("LESSON_ID")
                                );
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<LessonModel> call, Throwable t) {

                        }
                    });
        }


    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatetestlist);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatetestlist.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TBcreatetestlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
