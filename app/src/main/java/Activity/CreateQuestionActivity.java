package Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;


public class CreateQuestionActivity extends AppCompatActivity {

    Toolbar TBcreatequestion;
    public RecyclerView courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_question_created);
        setUIReference();
        ActionToolBar();
        //Load();

    }
    private void setUIReference() {
        TBcreatequestion = findViewById(R.id.TBcreatequestion);
    }
    private void ActionToolBar() {
        setSupportActionBar(TBcreatequestion);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatequestion.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcreatequestion.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}