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


public class HistoryActivity extends AppCompatActivity {

    Toolbar TBhistorycourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commingsoon);
        setUIReference();
        ActionToolBar();

    }
    private void setUIReference() {
        TBhistorycourse = findViewById(R.id.TBhistorycourse);
    }

    private void ActionToolBar() {
        setSupportActionBar(TBhistorycourse);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBhistorycourse.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBhistorycourse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}