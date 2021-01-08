package Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import org.json.JSONException;
import org.json.JSONObject;

import Model.UserAccount;
import Retrofit.IMyService;
import Retrofit.RetrofitClient;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInfoActivity extends AppCompatActivity {

    Toolbar TBupdateinfor;
    Button updateBtn;
    EditText inName, inPhone, inEmail, inAddress, inDescription,inBirthday,inGender;
    UserAccount userAccount = new UserAccount();
    String name,phone,email,address, description, birthday ,gender;
    IMyService iMyService;
    boolean flag = false;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        userAccount = (UserAccount) getIntent().getSerializableExtra("userAcc");
        setUIReference();
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
      //get user info from account fragment
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidInput()) updateProfile();
            }
        });
        ActionToolBar();
    }

    private boolean checkValidInput() {
        boolean valid = false;
        //inName, inPhone, inEmail, inAddress, inDescription,inBirthday,inGender;
        //name, phone,email, address, description, birthday ,gender;
        name    = inName.getText().toString();
        phone   = inPhone.getText().toString();
        address = inAddress.getText().toString();
        description = inDescription.getText().toString();
        gender  =inGender.getText().toString();
        if (name.isEmpty() || phone.isEmpty() ||address.isEmpty() || description.isEmpty() || gender.isEmpty()) {
            Toasty.warning(this, "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT, true).show();
            valid = false;
        } else valid = true;
        return valid;
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

    private void setUIReference() {
        //inName, inPhone, inEmail, inAddress, inDescription,inBirthday,inGender;
        //name, phone,email, address, description, birthday ,gender;
        inName = findViewById(R.id.input_name);
        inPhone = findViewById(R.id.input_phone);
        TBupdateinfor =findViewById(R.id.TBupdateinfor);
        inAddress = findViewById(R.id.input_address);
        inDescription = findViewById(R.id.input_description);
        inGender = findViewById(R.id.input_gender);
        updateBtn = findViewById(R.id.update_btn);
    }


    private void updateProfile() {
        updateBtn.setClickable(false);
        updateBtn.setEnabled(false);

        alertDialog.show();
        iMyService.changeProfile(name, phone, address, description, gender, userAccount.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<String> response) {


                        if (response.isSuccessful()) {


                            if (response.body().toString().contains("success")) {
                                String responeString = response.body().toString();
                                try {
                                    JSONObject jo = new JSONObject(responeString);
                                   //update user info
                                    flag = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                flag = false;
                            }
                        } else {
                            int a = response.code();
                            Toast.makeText(UserInfoActivity.this, "code: " + a, Toast.LENGTH_LONG).show();
                            flag = false;
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
                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        flag = false;

                    }

                    @Override
                    public void onComplete() {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();

                                    }
                                }, 500);

                        if (flag == true) {
                            Toasty.success(UserInfoActivity.this, "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT).show();
                            final Intent data = new Intent();

                            data.putExtra("usernewAcc", userAccount);

                            setResult(Activity.RESULT_OK, data);

                            finish();
                        } else
                            Toasty.error(UserInfoActivity.this, "Cập nhật thất bại !", Toast.LENGTH_SHORT, true).show();
                        updateBtn.setEnabled(true);
                        updateBtn.setClickable(true);
                    }
                });
    }
}