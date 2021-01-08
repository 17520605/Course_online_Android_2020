package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.ListTestAdapter;
import Adapter.ListTestItemAdapter;
import Model.LessonTestResponeModel;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLessonTestActivity extends AppCompatActivity {

    private Toolbar TBcreatequestionlist;
    private Button submitBtn;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list_test);
        initUIs();
        initEvents();
        Load();
        ActionToolBar();
    }

    private void initUIs() {
        TBcreatequestionlist = findViewById(R.id.TBcreatequestionlist);
        submitBtn = findViewById(R.id.ListLessonTest_SummitBtn);
        recyclerView = findViewById(R.id.ListLessonTest_RecyclerView);
    }

    private void initEvents(){

    }

    private void Load(){
        String lessonId = getIntent().getStringExtra("LESSON_ID");
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetTestOfLesson("lesson/get-multiple-choice-for-test/" + lessonId)
                .enqueue(new Callback<List<LessonTestResponeModel>>() {
                    @Override
                    public void onResponse(Call<List<LessonTestResponeModel>> call, Response<List<LessonTestResponeModel>> response) {
                        if(response.isSuccessful()){
//                            String testId = getIntent().getStringExtra("TEST_ID");
//                            for(int i = 0; i< response.body().size(); i++){
//                                if(testId.compareTo(response.body().get(i)._id) == 0){
//                                    ListTestAdapter adapter = new ListTestAdapter(response.body().get(i).multipleChoices);
//                                    recyclerView.setAdapter(adapter);
//                                }
//                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LessonTestResponeModel>> call, Throwable t) {

                    }
                });


    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatequestionlist);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatequestionlist.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TBcreatequestionlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
