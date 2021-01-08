package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import Retrofit.IUserService;
import es.dmoral.toasty.Toasty;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    Toolbar TBforgot;
    private EditText email;
    private Button recoveryBtn;

    private IUserService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUIs();
        initEvents();
        ActionToolBar();
    }

    private void initUIs() {
        TBforgot = findViewById(R.id.TBforgot);
        email =findViewById(R.id.ForgetPassword_Email);
        recoveryBtn =findViewById(R.id.ForgetPassword_RecoveryBtn);

    }

    private void initEvents(){
        recoveryBtn.setOnClickListener(v -> {
            if(isValidate()){
                ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
                service.ForgotPassword(email.getText().toString()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Intent intent=new Intent(ForgotPasswordActivity.this, RecoveryPasswordActivity.class);
                            intent.putExtra("EMAIL", email.getText().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private boolean isValidate(){
        if(email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            return  true;
        }
        else {
            Toasty.warning(ForgotPasswordActivity.this, "Email không hợp lệ !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(TBforgot);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBforgot.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBforgot.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}