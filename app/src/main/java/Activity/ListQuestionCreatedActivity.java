package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

public class ListQuestionCreatedActivity extends AppCompatActivity {

    private Toolbar TBcreatequestionlist;
    private Button createBtn;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        initUIs();
        initEvents();
        ActionToolBar();
    }

    private void initUIs() {
        TBcreatequestionlist = findViewById(R.id.TBcreatequestionlist);
        createBtn = findViewById(R.id.ListQuestionCreated_CreateBtn);
        recyclerView = findViewById(R.id.ListQuestionCreated_RecyclerView);
    }

    private void initEvents(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListQuestionCreatedActivity.this, CreateQuestionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
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
