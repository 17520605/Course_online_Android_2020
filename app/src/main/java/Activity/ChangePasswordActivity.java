package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import Model.ChangeProfileResponeModel;
import Model.User;
import Model.UserAccount;
import Retrofit.IUserService;
import Retrofit.AppPreference;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar TBchangepass;
    private User user;
    private EditText oldpassword;
    private EditText newpassword;
    private EditText confirmpassword;
    private Button change;

    private IUserService service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        user = Ultitities.GetUser(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        initUIs();
        ActionToolBar();
        initEventHandles();
    }

    private void initUIs() {
        TBchangepass = findViewById(R.id.TBchangepass);
        oldpassword = findViewById(R.id.changepassword_oldpassword_edt);
        newpassword = findViewById(R.id.changepassword_newpassword_edt);
        confirmpassword = findViewById(R.id.changepassword_confirmpassword_edt);
        change = findViewById(R.id.changepassword_change_btn);

    }

    private void ActionToolBar() {
        setSupportActionBar(TBchangepass);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBchangepass.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBchangepass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEventHandles(){
        change.setOnClickListener(v -> {
            ChangePassword();
        });
    }

    private void ChangePassword(){
        if(CheckPassword()){
            service = new Retrofit.Builder()
                    .baseUrl("http://149.28.24.98:9000/") // API base url
                    .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                    .build()
                    .create(IUserService.class);

            service .changepassword( oldpassword.getText().toString(), newpassword.getText().toString(), user.getAuthToken())
                    .enqueue(new Callback<ChangeProfileResponeModel>() {
                        @Override
                        public void onResponse(Call<ChangeProfileResponeModel> call, Response<ChangeProfileResponeModel> response) {
                            if(response.isSuccessful()){
                                Toasty.success(ChangePasswordActivity.this,"Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                AppPreference.Remove(getApplicationContext());
                                Intent intent = new Intent( ChangePasswordActivity.this, ChangePasswordSuccessfulActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toasty.error(ChangePasswordActivity.this,"Thay đổi mật khẩu thất bại.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeProfileResponeModel> call, Throwable t) {
                            Toasty.warning(ChangePasswordActivity.this, "Lỗi truy cập đến máy chủ.", Toast.LENGTH_SHORT, true).show();
                        }
                    });
        }

    }

    private boolean CheckPassword(){
        if(oldpassword.getText().toString().compareTo(user.getPassword()) == 0){
            if(newpassword.getText().toString().length() >= 8){
                if(newpassword.getText().toString().compareTo(user.getPassword()) != 0){
                    if(newpassword.getText().toString().compareTo(confirmpassword.getText().toString()) == 0){
                        return  true;
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, "Mat khau khong kop", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Mat khau khong dc trung  mk cu", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(ChangePasswordActivity.this, "Mat khau moi phai >= 8 ki tu", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(ChangePasswordActivity.this, "Mat khau sai", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private UserAccount GetUserAccount(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return new UserAccount(
                pref.getString("name", "default"),
                "",
                pref.getString("phone", "default"),
                pref.getString("image", "default"),
                pref.getString("email", "default"),
                pref.getString("id", "default"),
                pref.getString("token", "default"),
                pref.getString("gender", "default"),
                pref.getString("description", "default"),
                pref.getString("address", "default"),
                pref.getString("password", "default")
        );
    }
}