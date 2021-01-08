package Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.LessonDocumentCreatedAdapter;
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

public class CreateLessonActivity extends AppCompatActivity {
    private Toolbar TBcreatenewlesson;
    private EditText title;
    private EditText order;
    private RecyclerView recyclerView;
    private VideoView videoView;
    private Button addDocumentBtn;
    private Button addVideotBtn;
    private Button createBtn;

    private MultipartBody.Part videoPart;
    private List<MultipartBody.Part> documentParts = new ArrayList<>();

    private File videoFile;
    private List<File> documentFiles = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson_item);
        initUIs();
        initEvents();
        ActionToolBar();
    }



    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(reqCode == 1000){ // documents
                if(data != null && data.getData() != null){
                    String dowloadPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(data.getData().getPath());
                    if(file.getPath().contains("/external_storage_download")){
                        String path = file.getPath().replace("/external_storage_download", dowloadPath );
                        file = new File(path);
                    }
                    documentFiles.add(file);
                    List<String> docs = new ArrayList<String>();
                    for(int i = 0; i< documentFiles.size(); i++){
                        docs.add(documentFiles.get(i).getName());
                    }
                    LessonDocumentCreatedAdapter adapter = new LessonDocumentCreatedAdapter(docs);
                    recyclerView.setAdapter(adapter);
                }
                else {

                }
            }
            else
            if(reqCode == 1001){ //video
                if(data != null && data.getData() != null){
                    File file = new File(data.getData().getPath());
                    String externalPath = Environment.getExternalStorageDirectory().getPath();
                    if(file.getPath().contains("/external_files")){
                        String path = file.getPath().replace("/external_files", externalPath );
                        file = new File(path);
                    }

                    videoFile = file;
                    videoView.setVideoURI(data.getData());
                    videoPart = MultipartBody.Part.createFormData("videos", file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
                }
                else {
                    Toast.makeText(CreateLessonActivity.this, "Can't load image",Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Toast.makeText(CreateLessonActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
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
        TBcreatenewlesson = findViewById(R.id.TBcreatenewlesson);
        title = findViewById(R.id.CreateLesson_Title);
        order = findViewById(R.id.CreateLesson_Order);
        addDocumentBtn = findViewById(R.id.CreateLesson_AddDocumentBtn);
        addVideotBtn = findViewById(R.id.CreateLesson_AddVideoBtn);
        recyclerView = findViewById(R.id.CreateLesson_DocumentRecyclerView);
        videoView = findViewById(R.id.CreateLesson_Video);
        createBtn = findViewById(R.id.CreateLesson_CreateBtn);
    }

    private void initEvents(){
        addDocumentBtn.setOnClickListener(v -> {
            openFileManager();
        });
        addVideotBtn.setOnClickListener(v -> {
            openVideoManager();
        });
        createBtn.setOnClickListener(v -> {
            if(isValidate()){
                createLesson();
            }
        });
    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatenewlesson);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatenewlesson.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcreatenewlesson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isValidate(){
        if(title.getText().toString().length()==0){
            Toasty.warning(CreateLessonActivity.this, "Tên bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(order.getText().toString().length()==0){
            Toasty.warning(CreateLessonActivity.this, "Thứ tự bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(videoFile == null){
            Toasty.warning(CreateLessonActivity.this, "Video bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(documentFiles == null || documentFiles.size()==0){
            Toasty.warning(CreateLessonActivity.this, "Tài liệu bài học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void createLesson(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user =  Ultitities.GetUser(pref);

        MultipartBody.Part vidPart = MultipartBody.Part.create(RequestBody.create(MediaType.parse("video/mp4"), ReadFile(videoFile)));


        MultipartBody.Part docPart = MultipartBody.Part.create(RequestBody.create(MediaType.parse("application/pdf"), ReadFile(documentFiles.get(0))));


        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.CreateLesson(   title.getText().toString(),
                                order.getText().toString(),
                                courseId,
                                vidPart,
                                docPart,
                                user.getAuthToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    public byte[] ReadFile(File file){
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return  bytes;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }
}
