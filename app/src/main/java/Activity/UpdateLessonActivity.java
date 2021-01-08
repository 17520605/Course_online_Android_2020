package Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Adapter.LessonDocumentCreatedAdapter;
import Model.LessonModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateLessonActivity extends AppCompatActivity {
    private LessonModel _model;
    private Toolbar TBupdatenewlesson;
    private EditText title;
    private EditText order;
    private RecyclerView recyclerView;
    private VideoView videoView;
    //private Button addDocumentBtn;
   // private Button addVideotBtn;
    private Button UpdateBtn;


    private MultipartBody.Part videoPart;
    private List<MultipartBody.Part> documentParts = new ArrayList<>();

    private File videoFile;
    private List<File> documentFiles = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lesson);
        initUIs();
        initEvents();
        ActionToolBar();
        Load();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(reqCode == 1000){ // documents
                if(data != null && data.getData() != null){
//                    File file = new File(data.getData().getPath());
//                    documentFiles.add(file);

                }
                else {

                }
            }
            else
            if(reqCode == 1001){ //video
                if(data != null && data.getData() != null){
                    File file = new File(data.getData().getPath());
                    videoFile = file;
                    videoView.setVideoURI(data.getData());
                    //videoPart = MultipartBody.Part.createFormData("videos", file.getName(), RequestBody.create(MediaType.parse("video/*"), file.getPath()));
                }
                else {
                    Toast.makeText(UpdateLessonActivity.this, "Can't load image",Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Toast.makeText(UpdateLessonActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void Load(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        String lessonId = getIntent().getStringExtra("LESSON_ID");
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetLessonsById("/lesson/get-lesson-by-id/" + lessonId, user.getAuthToken())
                .enqueue(new Callback<LessonModel>() {
                    @Override
                    public void onResponse(Call<LessonModel> call, Response<LessonModel> response) {
                        if(response.isSuccessful()){
                            _model = response.body();
                            title.setText(_model.title);
                            order.setText(_model.order);

                            LessonDocumentCreatedAdapter adapter = new LessonDocumentCreatedAdapter(_model.doc);
                            recyclerView.setAdapter(adapter);

                            videoView.setVideoPath("http://149.28.24.98:9000/upload/lesson/" + _model.video);
                            videoView.start();
                        }
                    }

                    @Override
                    public void onFailure(Call<LessonModel> call, Throwable t) {

                    }
                });
    }

    private void openFileManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, 1000);
            }
            else {
                //permission already granted
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1000);
            }
        }
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 1000);
        }
    }

    private void openVideoManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, 1001);
            }
            else {
                //permission already granted
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, 1001);
            }
        }
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, 1001);
        }
    }

    private void initUIs() {
        TBupdatenewlesson = findViewById(R.id.TBupdatenewlesson);
        title = findViewById(R.id.UpdateLesson_Title);
        order = findViewById(R.id.UpdateLesson_Order);
        //addDocumentBtn = findViewById(R.id.UpdateLesson_AddDocumentBtn);
       // addVideotBtn = findViewById(R.id.UpdateLesson_AddVideoBtn);
        recyclerView = findViewById(R.id.UpdateLesson_DocumentRecyclerView);
        videoView = findViewById(R.id.UpdateLesson_Video);
        UpdateBtn = findViewById(R.id.UpdateLesson_CreateBtn);
    }

    private void initEvents(){
//        addDocumentBtn.setOnClickListener(v -> {
//            openFileManager();
//        });
//        addVideotBtn.setOnClickListener(v -> {
//            openVideoManager();
//        });
        UpdateBtn.setOnClickListener(v -> {
            if(isValidate()){
                UpdateLesson();
            }
        });
    }

    private void ActionToolBar() {
        setSupportActionBar(TBupdatenewlesson);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBupdatenewlesson.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBupdatenewlesson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isValidate(){
        if(title.getText().toString().length()==0){
            Toasty.warning(UpdateLessonActivity.this, "Tên bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(order.getText().toString().length()==0){
            Toasty.warning(UpdateLessonActivity.this, "Thứ tự bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }

    private void UpdateLesson(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        String lessonId = getIntent().getStringExtra("LESSON_ID");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user =  Ultitities.GetUser(pref);
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.UpdateLesson(   "lesson/update-lesson/" + lessonId,
                                courseId,
                                order.getText().toString(),
                                title.getText().toString(),
                                user.getAuthToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toasty.success(UpdateLessonActivity.this, "Cập nhật bài học thành công!", Toast.LENGTH_SHORT, true).show();
                            Intent intent=new Intent(UpdateLessonActivity.this, ListLessonCreatedActivity.class);
                            intent.putExtra("COURSE_ID", courseId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
