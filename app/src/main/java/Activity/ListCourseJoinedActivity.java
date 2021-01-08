package Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.CourseJoinedAdapter;
import Model.CourseJoinedModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListCourseJoinedActivity extends AppCompatActivity {
    public RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course_joined);
        initUIs();
        Load();
    }
    private void initUIs() {
        recyclerview = findViewById(R.id.ListCourseJoined_RecyclerView);
    }

    private void Load(){
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        service.GetJoinedCourses("join/get-courses-joined-by-user/" + user.get_id())
            .enqueue(new Callback<List<CourseJoinedModel>>() {
                @Override
                public void onResponse(Call<List<CourseJoinedModel>> call, Response<List<CourseJoinedModel>> response) {
                    if(response != null && response.body()!= null){
                        CourseJoinedAdapter adapter = new CourseJoinedAdapter(response.body());
                        recyclerview.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<List<CourseJoinedModel>> call, Throwable t) {

                }
            });
    }
}