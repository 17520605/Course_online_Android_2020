package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import Retrofit.ICourseService;
import Retrofit.IUserService;
import Retrofit.ServiceClient;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private EditText token;
    private EditText newpassword;
    private EditText comfirmpassword;
    private Button comfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        initUIs();
        initEvents();
    }

    private void initUIs() {
        token =findViewById(R.id.RecoveryPassword_Token);
        newpassword =findViewById(R.id.RecoveryPassword_NewPassword);
        comfirmpassword =findViewById(R.id.RecoveryPassword_ConfirmPassword);
        comfirmBtn =findViewById(R.id.RecoveryPassword_ConfirmBtn);
    }

    private void initEvents(){
        comfirmBtn.setOnClickListener(v -> {
            if(isValidate()){
                String email = getIntent().getStringExtra("EMAIL");
                ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
                service.RecoveryPassword( email, token.getText().toString(), newpassword.getText().toString())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Toasty.success(RecoveryPasswordActivity.this, "Đổi mật khẩu thành công !", Toast.LENGTH_SHORT, true).show();
                                Intent intent=new Intent(RecoveryPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toasty.error(RecoveryPasswordActivity.this, "Đổi mật khẩu thất bại !", Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toasty.error(RecoveryPasswordActivity.this, "Đổi mật khẩu thất bại !", Toast.LENGTH_SHORT, true).show();
                        }
                    });
            }
        });
    }

    private boolean isValidate(){
        if(token.getText().length() == 0){
            Toasty.warning(RecoveryPasswordActivity.this, "Token không hợp lệ !", Toast.LENGTH_SHORT, true).show();
            return  false;
        }
        else
        if(newpassword.getText().length() < 8){
            Toasty.warning(RecoveryPasswordActivity.this, "Mật khẩu phải trên 8 kí tự !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(newpassword.getText().toString().compareTo(comfirmpassword.getText().toString()) != 0){
            Toasty.warning(RecoveryPasswordActivity.this, "Nhập lại mật khẩu không trùng khớp !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return  true;
    }
}