package Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.tutorial_v1.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import Adapter.LessonTabAdapter;
import Model.CourseProgressModel;
import Model.LessonModel;
import Model.PopupQuestionModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LessonActivity extends AppCompatActivity {
    private LessonModel _model;
    private int questionIndex = 0;

    private Toolbar TBleanlesson;
    private LinearLayout wrapper;
    private MediaController controller;
    private VideoView video;
    private TextView title;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AlertDialog questionDialog;
    private View dialogView;
    private ImageView questionImage;
    private TextView questionTitle;
    private RadioButton questionAnswerA;
    private RadioButton questionAnswerB;
    private RadioButton questionAnswerC;
    private RadioButton questionAnswerD;
    private Button questionSubmitBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initUIs();
        initEvents();
        ActionToolBar();
        Sync();

    }
    private void ActionToolBar() {
        setSupportActionBar(TBleanlesson);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBleanlesson.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBleanlesson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initUIs() {
        wrapper = findViewById(R.id.Lesson_Wrapper);
        video = findViewById(R.id.Lesson_Video);
        title = findViewById(R.id.Lesson_Title);
        tabLayout = findViewById(R.id.Lesson_TabLayout);
        viewPager = findViewById(R.id.Lesson_ViewPager);
        TBleanlesson = findViewById(R.id.TBleanlesson);
        AlertDialog.Builder builder = new AlertDialog.Builder(LessonActivity.this);
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup_question, wrapper , false);
        builder.setView(dialogView);
        questionDialog = builder.create();
        questionImage = dialogView.findViewById(R.id.Lesson_PopupQuestion_Image);
        questionTitle = dialogView.findViewById(R.id.Lesson_PopupQuestion_Title);
        questionAnswerA = dialogView.findViewById(R.id.Lesson_PopupQuestion_Answer_A);
        questionAnswerB = dialogView.findViewById(R.id.Lesson_PopupQuestion_Answer_B);
        questionAnswerC = dialogView.findViewById(R.id.Lesson_PopupQuestion_Answer_C);
        questionAnswerD = dialogView.findViewById(R.id.Lesson_PopupQuestion_Answer_D);
        questionAnswerD = dialogView.findViewById(R.id.Lesson_PopupQuestion_Answer_D);
        questionSubmitBtn = dialogView.findViewById(R.id.Lesson_PopupQuestion_SubmitBtn);
    }

    private void initEvents(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        video.setOnCompletionListener(mp -> {
            try {
                UpdateLessonProgress();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        video.setOnLongClickListener(v -> {
            LoadAndOpenPopupQuestion();
            return true;
        });

        questionSubmitBtn.setOnClickListener(v -> {
            PopupQuestionModel question = _model.popupQuestion.get(questionIndex);
            if(question.answer.compareTo("A") == 0 && questionAnswerA.isChecked()){
                questionDialog.hide();
            }
            else
            if(question.answer.compareTo("B") == 0 && questionAnswerB.isChecked()){
                questionDialog.hide();
            }
            else
            if(question.answer.compareTo("C") == 0 && questionAnswerC.isChecked()){
                questionDialog.hide();
            }
            else
            if(question.answer.compareTo("D") == 0 && questionAnswerD.isChecked()){
                questionDialog.hide();
            }
            else{
                questionAnswerA.setChecked(false);
                questionAnswerB.setChecked(false);
                questionAnswerC.setChecked(false);
                questionAnswerD.setChecked(false);
                Toast.makeText(LessonActivity.this, "Câu trả lời không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Sync(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        String lessonId = getIntent().getStringExtra("LESSON_ID");

        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetLessonsById("/lesson/get-lesson-by-id/" + lessonId, user.getAuthToken())
                .enqueue(new Callback<LessonModel>() {
                    @Override
                    public void onResponse(Call<LessonModel> call, Response<LessonModel> response) {
                        if(response.isSuccessful()){
                            Load(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LessonModel> call, Throwable t) {

                    }
                });

    }

    private void Load(LessonModel model){
        if(model != null){
            _model = model;
            video.setVideoPath("http://149.28.24.98:9000/upload/lesson/" + _model.video);
            controller = new MediaController(LessonActivity.this);
            controller.setAnchorView(video);
            video.setMediaController(controller);

            tabLayout.addTab(tabLayout.newTab().setText("Tài liệu"));
            tabLayout.addTab(tabLayout.newTab().setText("Hỏi đáp"));
            tabLayout.addTab(tabLayout.newTab().setText("Trắc nghiệm"));

            final LessonTabAdapter adapter = new LessonTabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(), _model);
            viewPager.setAdapter(adapter);

        }
    }

    private void UpdateLessonProgress() throws JSONException {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);

        String courseId = getIntent().getStringExtra("COURSE_ID");
        String lessonId = getIntent().getStringExtra("LESSON_ID");

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "");

        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.UpdateLessonProgress("join/update-progress-lesson-of-course/" + user.get_id() + "/"+courseId +"/" + lessonId)
                .enqueue(new Callback<CourseProgressModel>() {
                    @Override
                    public void onResponse(Call<CourseProgressModel> call, Response<CourseProgressModel> response) {
                        if(response.isSuccessful()){

                        }
                    }

                    @Override
                    public void onFailure(Call<CourseProgressModel> call, Throwable t) {

                    }
                });
    }

    private void LoadAndOpenPopupQuestion(){
        if(questionIndex < _model.popupQuestion.size()){
            PopupQuestionModel question = _model.popupQuestion.get(questionIndex);

            Picasso.get().load("http://149.28.24.98:9000/upload/lesson/" + question.image)
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.loading2)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(questionImage);
            questionTitle.setText(question.question);
            questionAnswerA.setText(question.A);
            questionAnswerB.setText(question.B);
            questionAnswerC.setText(question.C);
            questionAnswerD.setText(question.D);

            questionDialog.show();

            video.pause();
        }
    }
}