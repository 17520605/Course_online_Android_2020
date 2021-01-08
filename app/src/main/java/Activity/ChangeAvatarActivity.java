package Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;

import Model.User;
import Retrofit.IUserService;
import Ultilities.Ultitities;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeAvatarActivity extends AppCompatActivity {
    private User user;
    private IUserService service;
    private boolean flag=false,flag2=false;
    private File file;
    private ImageView image;
    private Button openGalleryBtn;
    private Button openCameraBtn;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        initUIs();
        initEventHandles();
        ReloadContent();
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
                file = new File(imageUri.getPath().substring(4)); // bo "/raw"
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                image.setImageBitmap(bitmap);
            }
            else {
                Toast.makeText(ChangeAvatarActivity.this, "Can't load image",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ChangeAvatarActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void initUIs() {
        image = findViewById(R.id.ChangeAvatar_Image);
        openCameraBtn = findViewById(R.id.ChangeAvatar_OpenCameraBtn);
        openGalleryBtn = findViewById(R.id.ChangeAvatar_OpenGalleryBtn);
        updateBtn = findViewById(R.id.ChangeAvatar_UpdateBtn);
    }

    private void initEventHandles(){
        openGalleryBtn.setOnClickListener(v -> {
            OpenGallery();
        });

        openCameraBtn.setOnClickListener(v -> {
            OpenCamera();
        });

        updateBtn.setOnClickListener(v -> {
            if(file != null){
                UploadImage(file);
                finish();
            }
        });
    }

    private void ReloadContent(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = Ultitities.GetUser(pref);
        if(user != null && !user.getImage().isEmpty()){
            Picasso.get().load("http://149.28.24.98:9000/upload/user_image/" + user.getImage())
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.logo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(image);
        }
    }

    private void OpenGallery(){
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

    private void OpenCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, 1001);
            }
            else {
                //permission already granted
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1001);
            }
        }
        else {
            //system os is less then marshmallow
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 100);
        }
    }

    private void UploadImage(File file){
        AlertDialog alertDialog = new SpotsDialog.Builder().setContext(ChangeAvatarActivity.this).build();
        alertDialog.show();

        RequestBody fileReqBody = RequestBody.create( MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        Toast.makeText(ChangeAvatarActivity.this, file.getPath(), Toast.LENGTH_LONG).show();
        service = new Retrofit.Builder()
                .baseUrl("http://149.28.24.98:9000/") // API base url
                .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                .build()
                .create(IUserService.class);

        service.changeavatar(part, user.getAuthToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            ReloadContent();
                            alertDialog.dismiss();
                            Intent intent = new Intent(ChangeAvatarActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(ChangeAvatarActivity.this, "That bai", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ChangeAvatarActivity.this, "Loi he thong", Toast.LENGTH_LONG).show();
                    }
                });
    }

}