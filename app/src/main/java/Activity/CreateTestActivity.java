package Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Adapter.TestCreatedAdapter;
import Model.MutipleChoiceModel;
import Model.TestResquestModel;
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


public class CreateTestActivity extends AppCompatActivity {

    private Toolbar TBcreateTest;
    private TextView question;
    private ImageView image;
    private TextView ansA;
    private TextView ansB;
    private TextView ansC;
    private TextView ansD;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioButton radioD;
    private Button createBtn;
    private Button chooseimage;

    File imagefile = null;
    String imagename = "";

    List<MutipleChoiceModel> tests = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multichoice_created);
        initUIs();
        initEvents();
        ActionToolBar();
        Load();
    }

    private void initUIs() {
        TBcreateTest = findViewById(R.id.TBcreateTest);
        question = findViewById(R.id.CreateTest_Question);
        image = findViewById(R.id.CreateTest_Image);
        ansA = findViewById(R.id.CreateTest_A);
        ansB = findViewById(R.id.CreateTest_B);
        ansC = findViewById(R.id.CreateTest_C);
        ansD = findViewById(R.id.CreateTest_D);
        radioA = findViewById(R.id.CreateTest_A_Rbtn);
        radioB = findViewById(R.id.CreateTest_B_Rbtn);
        radioC = findViewById(R.id.CreateTest_C_Rbtn);
        radioD = findViewById(R.id.CreateTest_D_Rbtn);
        createBtn = findViewById(R.id.CreateTest_CreateBtn);
        chooseimage = findViewById(R.id.CreateTest_ChooseImageBtn);
    }

    private void initEvents(){
        createBtn.setOnClickListener(v -> {
            CreateTest();
        });

        chooseimage.setOnClickListener(v -> {
            openGallery();
        });

    }

    private void Load(){
        if(getIntent().hasExtra("TESTS")){ // load from extra data
            try {
                JSONArray arr = new JSONArray(getIntent().getStringExtra("TESTS"));
                tests = new Gson().fromJson(arr.toString(), new TypeToken<List<MutipleChoiceModel>>() {}.getType());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidate(){
        if(question.getText().length() == 0){
            Toasty.warning(this, "Câu hỏi không được bỏ trống !", Toast.LENGTH_SHORT, true).show();
            return  false;
        }

        if(ansA.getText().length() == 0 || ansB.getText().length() == 0 || ansC.getText().length() == 0 || ansD.getText().length() == 0){
            Toasty.warning(this, "Đáp án bị bỏ trống !", Toast.LENGTH_SHORT, true).show();
            return  false;
        }

        if(radioA.isChecked() == false && radioB.isChecked() == false && radioC.isChecked() == false && radioD.isChecked() == false){
            Toasty.warning(this, "Chưa chọn đáp án đúng!", Toast.LENGTH_SHORT, true).show();
            return  false;
        }

        if(imagename == null || imagename.length() == 0){
            Toasty.warning(this, "Chưa chọn hình ảnh !", Toast.LENGTH_SHORT, true).show();
            return  false;
        }
        return true;
    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreateTest);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBcreateTest.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcreateTest.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1000:{
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1000);
                }
                else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
            case 1001:{
                //  Toast.makeText(this, "asd: "+ PackageManager.PERMISSION_GRANTED, Toast.LENGTH_SHORT).show();
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1001);
                }
                else
                {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(data != null && data.getData() != null){
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                if(imageUri.getPath().startsWith("/raw")){
                    imagefile = new File(imageUri.getPath().substring(4)); // bo "/raw"
                }
                else {
                    imagefile = new File(imageUri.getPath());
                }
                Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getPath());
                image.setImageURI(data.getData());

                UploadImage();

            }
            else {
                Toast.makeText(this, "Can't load image",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, 1000);
            }
            else {
                //permission already granted
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1000);
            }
        }
        else {
            //system os is less then marshmallow
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1000);
        }
    }

    private void UploadImage(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user =  Ultitities.GetUser(pref);
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.UploadTestImage(    MultipartBody.Part.createFormData("image", imagefile.getName(),
                                    RequestBody.create(MediaType.parse("image/jpg"), imagefile)), //RequestBody.create(MediaType.parse("image/jpg"), file)
                                    user.getAuthToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toasty.success(CreateTestActivity.this, "Tải ảnh lên thành công !", Toast.LENGTH_SHORT, true).show();
                            String str = null;
                            try {
                                if(response.body().string().length() > 0){
                                    JSONObject json =  new Gson().fromJson(response.body().string(), JSONObject.class);
                                    imagename = json.getString("image");
                                }
                                else {
                                    imagename = "1609835401514-554751128-FAQ-Q-and-A-icon-symbol-questions-answer-Graphics-5337172-1.jpg";
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toasty.error(CreateTestActivity.this, "Tải ảnh lên thất bại !", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(CreateTestActivity.this, "Lỗi !", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void CreateTest(){
        if(isValidate()){

            if(tests == null){
                tests = new ArrayList<>();
            }

            MutipleChoiceModel test = new MutipleChoiceModel();
            test.A = ansA.getText().toString();
            test.B = ansB.getText().toString();
            test.C = ansC.getText().toString();
            test.D = ansD.getText().toString();
            test.question = question.getText().toString();
            test.image = imagename;
            if(radioA.isChecked()){
                test.answer = "A";
            }
            else
            if(radioB.isChecked()){
                test.answer = "B";
            }
            else
            if(radioC.isChecked()){
                test.answer = "C";
            }
            else
            if(radioD.isChecked()){
                test.answer = "D";
            }

            tests.add(test);

            TestResquestModel resquest = new TestResquestModel();
            resquest.multipleChoices = tests;
            String json = new Gson().toJson(resquest);

            String lessonId = getIntent().getStringExtra("LESSON_ID");
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            User user =  Ultitities.GetUser(pref);
            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.UpdateTest("lesson/add-list-multiple-choice/" + lessonId,
                                RequestBody.create(MediaType.parse("application/json"), json),
                                user.getAuthToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Intent intent=new Intent(CreateTestActivity.this, ListTestCreatedActivity.class);
                                intent.putExtra("LESSON_ID", lessonId);
                                CreateTestActivity.this.startActivity(intent);
                                Toasty.success(CreateTestActivity.this, "Thêm bài tập thành công !", Toast.LENGTH_SHORT, true).show();
                            }
                            else {
                                Toasty.error(CreateTestActivity.this, "Thêm bài tập thất bại !", Toast.LENGTH_SHORT, true).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        }


    }
}