package Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Model.User;
import dmax.dialog.SpotsDialog;
import com.example.tutorial_v1.R;

import org.json.JSONException;
import org.json.JSONObject;

import Retrofit.*;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private TextView regisTextView,forgotPassword;
    private EditText email, password;
    private CompositeDisposable compositeDisposable =new CompositeDisposable();
    private IMyService iMyService;
    private String TaiKhoan, MatKhau;
    private AlertDialog alertDialog;
    private User user;
    private SharedPreferences sharedPreferences;
    private boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setContentView(R.layout.activity_login);

        setUIReference();

        if(CheckUserLogin()){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        regisTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckValidInput()){
                    Login();
                }
            }
        });
    }

    private void Login() {
        login_btn.setClickable(false);
        login_btn.setEnabled(false);
        try {

            iMyService.loginUser(TaiKhoan, MatKhau)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        public void onNext(Response<String> stringResponse) {
                            if(stringResponse.isSuccessful()){
                                if(stringResponse.body().toString().contains("name"))
                                {
                                    String responseString=stringResponse.body().toString();
                                    try {
                                        JSONObject rsp =new JSONObject(responseString);
                                        user = new User(
                                                rsp.getString("active"),
                                                rsp.getString("created_at"),
                                                rsp.getString("_id"),
                                                rsp.getString("email"),
                                                rsp.getString("name"),
                                                MatKhau,
                                                rsp.getString("phone"),
                                                rsp.getString("address"),
                                                rsp.getString("description"),
                                                rsp.getString("role"),
                                                rsp.getString("image"),
                                                rsp.getString("gender"),
                                                rsp.getString("__v"),
                                                rsp.getString("activeToken"),
                                                stringResponse.headers().get("Auth-token")
                                        );

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("active",user.getActive());
                                        editor.putString("created_at",user.getCreated_at());
                                        editor.putString("_id",user.get_id());
                                        editor.putString("name",user.getName());
                                        editor.putString("email",user.getEmail());
                                        editor.putString("password",user.getPassword());
                                        editor.putString("phone",user.getPhone());
                                        editor.putString("address",user.getAddress());
                                        editor.putString("description",user.getDescription());
                                        editor.putString("role",user.getRole());
                                        editor.putString("image",user.getImage());
                                        editor.putString("gender",user.getGender());
                                        editor.putString("__v",user.get__v());
                                        editor.putString("activeToken",user.getActiveToken());
                                        editor.putString("authToken",user.getAuthToken());

                                        editor.commit();
                                        flag=true;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    flag=false;
                                }}
                            else{
                                flag=false;
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            alertDialog.dismiss();
                                        }
                                    }, 500);
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            login_btn.setClickable(true);
                            login_btn.setEnabled(true);
                        }
                        @Override
                        public void onComplete() {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            alertDialog.dismiss();
                                        }
                                    }, 500);
                            if(flag==true) {
                                //Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                                Toasty.success(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("userAcc", user);
                                intent.putExtra("change",0);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_bounce, R.anim.anim_fade_out);
                                finish();
                            }
                            else {
                                //Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                Toasty.error(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT, true).show();
                                login_btn.setClickable(true);
                                login_btn.setEnabled(true);
                            }
                        }
                    });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setUIReference() {
        login_btn=findViewById(R.id.login_btn);
        regisTextView=findViewById(R.id.regisTextView);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forgotPassword=findViewById(R.id.forgotPassword);
    }

    private boolean CheckValidInput() {
        Boolean valid = true;
        TaiKhoan=email.getText().toString();
        MatKhau=password.getText().toString();
        if(TaiKhoan.isEmpty() ||TaiKhoan.length() < 6 || TaiKhoan.length() >40 )
        {
            email.setError("Từ 6 đến 40 ký tự");
            valid = false;
        } else {
            email.setError(null);
        }
        if(MatKhau.isEmpty() || MatKhau.length() <8 ||MatKhau.length()>16 )
        {
            password.setError("Mật khẩu có 8 đến 16 ký tự");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    private boolean CheckUserLogin(){
        if(sharedPreferences != null){
            if(sharedPreferences.getString("_id", "").compareTo("") != 0){
                return true;
            }
        }
        return  false;
    }
}