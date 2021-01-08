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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import Model.CategorySummaryModel;
import Model.User;
import Retrofit.ICourseService;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import Retrofit.ServiceClient;

public class CreateCourseActivity extends AppCompatActivity {
    private Toolbar TBcreatenewcourse;
    private Context context;
    private EditText name;
    private EditText goal;
    private EditText description;
    private Spinner category;
    private EditText price;
    private EditText discount;
    private ImageView image;
    private Button createCourseBtn;
    private Button changeImageBtn;
    private File file;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_create_course_item);
        initUIs();
        initEvens();
        ActionToolBar();
        load();
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
                    file = new File(imageUri.getPath().substring(4)); // bo "/raw"
                }
                else {
                    file = new File(imageUri.getPath());
                }
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                image.setImageURI(data.getData());
                //image.setImageBitmap(bitmap);
            }
            else {
                Toast.makeText(CreateCourseActivity.this, "Can't load image",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(CreateCourseActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void load(){
        // Load combobox loại khóa học
        ICourseService categoryService = ServiceClient.getInstance().create(ICourseService.class);
        categoryService.GetAllCategorySummaries("category/get-all-category").enqueue(new Callback<List<CategorySummaryModel>>() {
            @Override
            public void onResponse(Call<List<CategorySummaryModel>> call, Response<List<CategorySummaryModel>> response) {
                if(response.isSuccessful()){
                    ArrayAdapter<CategorySummaryModel> adapter = new ArrayAdapter<CategorySummaryModel>(context, R.layout.item_spinner_category, response.body()){
                        @Override
                        public View getView(int position, View view, ViewGroup parent) {
                            final CategorySummaryModel category = getItem(position);
                            if (view == null) {
                                view = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_category, parent, false);
                            }
                            TextView name = (TextView) view.findViewById(R.id.Item_Spinner_Category_Name);
                            name.setText(category.name);
                            return view;
                        }
                    };

                    category.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<CategorySummaryModel>> call, Throwable t) {

            }
        });
    }

    private void initUIs() {
        name = findViewById(R.id.CreateCourse_Name);
        goal = findViewById(R.id.CreateCourse_Goal);
        description = findViewById(R.id.CreateCourse_Description);
        price = findViewById(R.id.CreateCourse_Price);
        category = findViewById(R.id.CreateCourse_Category);
        discount = findViewById(R.id.CreateCourse_Discount);
        createCourseBtn = findViewById(R.id.CreateCourse_CreateCourseBtn);
        changeImageBtn = findViewById(R.id.CreateCourse_ChangeImageBtn);
        image = findViewById(R.id.CreateCourse_Image);
        TBcreatenewcourse = findViewById(R.id.TBcreatenewcourse);
    }

    private void ActionToolBar() {
        setSupportActionBar(TBcreatenewcourse);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcreatenewcourse.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcreatenewcourse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEvens() {
        changeImageBtn.setOnClickListener(v -> {
            openGallery();
        });

        createCourseBtn.setOnClickListener(v -> {
            if(isValidate()){
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                User user =  Ultitities.GetUser(pref);
                ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
                service.CreateCourse(   RequestBody.create(MediaType.parse("text/plain"), name.getText().toString()),
                                        RequestBody.create(MediaType.parse("text/plain"), goal.getText().toString()),
                                        RequestBody.create(MediaType.parse("text/plain"), description.getText().toString()),
                                        RequestBody.create(MediaType.parse("text/plain"), ((CategorySummaryModel)category.getSelectedItem())._id),
                                        RequestBody.create(MediaType.parse("text/plain"), price.getText().toString()),
                                        RequestBody.create(MediaType.parse("text/plain"), discount.getText().toString()),
                                        MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file)), //RequestBody.create(MediaType.parse("image/jpg"), file)
                                        user.getAuthToken())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    Intent intent=new Intent(CreateCourseActivity.this, ListCourseCreatedActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toasty.success(CreateCourseActivity.this, "Tạo khóa học thành công !", Toast.LENGTH_SHORT, true).show();
                                }
                                else {
                                    Toasty.error(CreateCourseActivity.this, "Tạo không thành công !", Toast.LENGTH_SHORT, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(CreateCourseActivity.this, "Lỗi hệ thống !", Toast.LENGTH_SHORT, true).show();
                            }
                        });
            }
        });

    }

    private boolean isValidate(){
        if(name.getText().toString().length()==0){
            Toasty.warning(CreateCourseActivity.this, "Tên khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(goal.getText().toString().length()==0){
            Toasty.warning(CreateCourseActivity.this, "Mục tiêu khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(description.getText().toString().length()==0){
            Toasty.warning(CreateCourseActivity.this, "Mô tả khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(category.getSelectedItem() == null){
            Toasty.warning(CreateCourseActivity.this, "Thể loại khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(file == null){
            Toasty.warning(CreateCourseActivity.this, "Hình ảnh khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
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
}
