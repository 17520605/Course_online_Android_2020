package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import Model.ChangeProfileResponeModel;
import Model.User;
import Retrofit.IUserService;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class AccountSettingActivity extends AppCompatActivity {

    private Toolbar TBupdateinfor;
    private ImageView avatar;
    private EditText Name_EditText;
    private EditText Phone_EditText;
    private EditText Email_EditText;
    private EditText Address_EditText;
    private EditText Description_EditText;
    private EditText Gender_EditText;
    private Button Change_Button;
    private IUserService service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        initUIs();
        initEventHandles();
        ActionToolBar();
        ReloadContent();
    }

    private void initUIs() {
        avatar = findViewById(R.id.accountsetting_avatar_img);
        Name_EditText = findViewById(R.id.input_name);
        TBupdateinfor = findViewById(R.id.TBupdateinfor);
        Phone_EditText = findViewById(R.id.input_phone);
        Email_EditText = findViewById(R.id.input_email);
        Address_EditText = findViewById(R.id.input_address);
        Description_EditText = findViewById(R.id.input_description);
        Gender_EditText = findViewById(R.id.input_gender);
        Change_Button = findViewById(R.id.update_btn);
        Email_EditText.setEnabled(false);
    }

    private void initEventHandles(){
        Change_Button.setOnClickListener(v -> {
            ChangeInfo();
        });

        avatar.setOnClickListener(v -> {
            Intent intent=new Intent(AccountSettingActivity.this, ChangeAvatarActivity.class);
            startActivity(intent);
        });
    }

    private void ActionToolBar() {
        setSupportActionBar(TBupdateinfor);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TBupdateinfor.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBupdateinfor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void ChangeInfo(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user =  Ultitities.GetUser(pref);

        service = new Retrofit.Builder()
                .baseUrl("http://149.28.24.98:9000/") // API base url
                .addConverterFactory(GsonConverterFactory.create()) // Factory phụ thuộc vào format trả về
                .build()
                .create(IUserService.class);

        //==============================get Share references===============================================================
        service.change( Name_EditText.getText().toString(),
                        Phone_EditText.getText().toString(),
                        Address_EditText.getText().toString(),
                        Description_EditText.getText().toString(),
                        Gender_EditText.getText().toString(),
                        user.getAuthToken())
                .enqueue(new Callback<ChangeProfileResponeModel>() {
                    @Override
                    public void onResponse(Call<ChangeProfileResponeModel> call, Response<ChangeProfileResponeModel> response) {
                        String message = "NULL";
                        if(response!=null && response.body()!=null) message = response.body().getMessage();
                        String code = Integer.toString(response.code()) ;
                        //Toast.makeText(AccountSettingActivity.this, code, Toast.LENGTH_LONG).show();

                        if(response.isSuccessful()){
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Ultitities.SaveUser(pref,response.body().getUser());
                            ReloadContent();
                            Toasty.success(AccountSettingActivity.this,"Thay đổi thành công !",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AccountSettingActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toasty.error(AccountSettingActivity.this,"Thay đổi thất bại !",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangeProfileResponeModel> call, Throwable t) {
                        //Toast.makeText(getActivity(), "Loi ket noi may chu", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ReloadContent(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        if(user != null){
            Name_EditText.setText(user.getName());
            Phone_EditText.setText(user.getPhone());
            Email_EditText.setText(user.getEmail());
            Address_EditText.setText(user.getAddress());
            Description_EditText.setText(user.getDescription());
            Gender_EditText.setText(user.getGender());
            Picasso.get().load("http://149.28.24.98:9000/upload/user_image/" + user.getImage())
                    .placeholder(R.drawable.noavatar1)
                    .error(R.drawable.noavatar1)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(avatar);
        }
    }
}