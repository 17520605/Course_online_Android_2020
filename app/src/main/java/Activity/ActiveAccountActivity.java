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

import Model.User;
import Retrofit.IUserService;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActiveAccountActivity extends AppCompatActivity {
    Toolbar TBactivecode;
    private EditText VerifyCode_EditText;
    private Button Verify_Button;

    private IUserService service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initUIs();
        ActionToolBar();
        initEventHandles();
    }

    private void initUIs() {
        VerifyCode_EditText = findViewById(R.id.code);
        Verify_Button = findViewById(R.id.active_code);
        TBactivecode = findViewById(R.id.TBactivecode);
    }
    private void ActionToolBar() {
        setSupportActionBar(TBactivecode);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBactivecode.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBactivecode.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEventHandles(){
        Verify_Button.setOnClickListener(v -> {
            service = new Retrofit.Builder()
                    .baseUrl("http://149.28.24.98:9000/") // API base url
                    .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                    .build()
                    .create(IUserService.class);
            if(VerifyCode_EditText != null && VerifyCode_EditText.getText().toString() != ""){
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                User user = Ultitities.GetUser(pref);
                service .active(user.getEmail(), VerifyCode_EditText.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response!=null && response.code() == 200){
                                    Toasty.success(ActiveAccountActivity.this,"Kích hoạt thành công",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ActiveAccountActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toasty.error(ActiveAccountActivity.this,"Kích hoạt không thành công",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.warning(ActiveAccountActivity.this, "Lỗi truy cập đến máy chủ.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}