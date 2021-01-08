package Activity;

import androidx.appcompat.app.AppCompatActivity;

import Model.User;
import Retrofit.*;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText gender;
    private EditText phone;
    private EditText address;
    private EditText password;
    private EditText email;
    private EditText description;
    private Button sign_up_btn;
    private TextView sing_in_btn;

    private String Name, Password, Phone, Address, Description, Gender, Email;
    private String response = "AA";
    private IUserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUIs();
        initEventHandles();
    }

    private void initUIs() {
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        description = findViewById(R.id.description);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        sing_in_btn = findViewById(R.id.sign_in);
    }

    private void initEventHandles(){

        // Thuc hien dang ky
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValidate()){
                    try{
                        IUserService service = new Retrofit.Builder()
                                .baseUrl("http://149.28.24.98:9000/") // API base url
                                .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                                .build()
                                .create(IUserService.class);
                        service.register(   name.getText().toString(),
                                password.getText().toString(),
                                phone.getText().toString(),
                                address.getText().toString(),
                                description.getText().toString(),
                                gender.getText().toString(),
                                email.getText().toString()
                        )
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.isSuccessful()){
                                            Toasty.success(RegisterActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT, true).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toasty.error(RegisterActivity.this, "Đăng kí thất bại !", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toasty.warning(RegisterActivity.this, "Máy chủ đăng gặp lỗi !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    catch (Exception ex){
                    }
                }
            }
        });

        // Quay lai dang nhap
        sing_in_btn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
        });
    }

    private boolean isValidate() {
        return true;
    }
}