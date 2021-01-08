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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import Model.CategorySummaryModel;
import Model.CourseModel;
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


public class UpdateCourseActivity extends AppCompatActivity {
    private CourseModel _model;

    private File file;
    private Context context;

    private ImageView image;
    private TextView name;
    private TextView goal;
    private TextView description;
    private Spinner category;
    private TextView price;
    private TextView discount;
    private Button changeimageBtn;
    private Button updateBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        initUIs();
        initEvents();
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
                Toast.makeText(UpdateCourseActivity.this, "Can't load image",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(UpdateCourseActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void initUIs() {
        context = this;
        image = findViewById(R.id.UpdateCourse__Image);
        name = findViewById(R.id.UpdateCourse__Title);
        goal = findViewById(R.id.UpdateCourse__Goal);
        description = findViewById(R.id.UpdateCourse__Description);
        category = findViewById(R.id.UpdateCourse__Category);
        price = findViewById(R.id.UpdateCourse__Price);
        discount = findViewById(R.id.UpdateCourse__Discount);
        changeimageBtn = findViewById(R.id.UpdateCourse__ChangeImageBtn);
        updateBtn = findViewById(R.id.UpdateCourse__UpdateBtn);
    }

    private void initEvents(){
        changeimageBtn.setOnClickListener(v -> {
            openGallery();
        });
        updateBtn.setOnClickListener(v -> {
            if(isValidate()){
                String courseId = getIntent().getStringExtra("COURSE_ID");
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                User user =  Ultitities.GetUser(pref);
                ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
                service.UpdateCourse(   "course/update/" + courseId,
                        RequestBody.create(MediaType.parse("text/plain"), name.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), goal.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), description.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), ((CategorySummaryModel)category.getSelectedItem())._id),
                        RequestBody.create(MediaType.parse("text/plain"), price.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), discount.getText().toString()),
                        file == null ? null : MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file)),
                        user.getAuthToken())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    Intent intent=new Intent(UpdateCourseActivity.this, ListCourseCreatedActivity.class);
                                    finish();
                                    startActivity(intent);
                                    Toasty.success(UpdateCourseActivity.this, "cập nhật khóa học thành công", Toast.LENGTH_SHORT, true).show();
                                }
                                else {
                                    Toasty.error(UpdateCourseActivity.this, "cập nhật không thành công", Toast.LENGTH_SHORT, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(UpdateCourseActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT, true).show();
                            }
                        });
            }
        });
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
    private void load(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);

        // Load danh sach loai khoa hoc
        ICourseService categoryService = ServiceClient.getInstance().create(ICourseService.class);
        categoryService.GetAllCategorySummaries("category/get-all-category")
                .enqueue(new Callback<List<CategorySummaryModel>>() {
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
                         }
                );

        // Laod thong tin khoa hoc
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetCourseById("course/getbyid/" + courseId).enqueue(new Callback<CourseModel>() {
            @Override
            public void onResponse(Call<CourseModel> call, Response<CourseModel> response) {
                if(response.isSuccessful()){
                    CourseModel course = response.body();
                    Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.getImage())
                            .placeholder(R.drawable.loading2)
                            .error(R.drawable.loading2)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(image);
                    name.setText(course.getName());
                    goal.setText(course.getGoal());
                    SetCategory(course.getCategory());
                    description.setText(course.getDescription());
                    price.setText(course.getPrice());
                    discount.setText(course.getDiscount());
                }
            }

            @Override
            public void onFailure(Call<CourseModel> call, Throwable t) {
                Toasty.warning(UpdateCourseActivity.this, "Lỗi hệ thống máy chủ !", Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    private void SetCategory( String categoryId){
        if(category != null){
            ArrayAdapter<CategorySummaryModel> adapter = (ArrayAdapter<CategorySummaryModel>)this.category.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if(adapter.getItem(i)._id.compareTo(categoryId)==0){
                    this.category.setSelection(i);
                    break;
                }
            }
        }
    }

    private boolean isValidate(){
        if(name.getText().toString().length()==0){
            Toasty.warning(UpdateCourseActivity.this, "Tên khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(goal.getText().toString().length()==0){
            Toasty.warning(UpdateCourseActivity.this, "Mục tiêu khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(description.getText().toString().length()==0){
            Toasty.warning(UpdateCourseActivity.this, "Mô tả khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
        if(category.getSelectedItem() == null){
            Toasty.warning(UpdateCourseActivity.this, "Thể loại khóa học rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }
}