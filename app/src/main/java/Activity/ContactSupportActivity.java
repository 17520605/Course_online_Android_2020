package Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import Retrofit.IUserService;

public class ContactSupportActivity extends AppCompatActivity {

    Toolbar TBcontactsuport;
    private Button callusbtn;

    private IUserService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initUIs();
        ActionToolBar();
    }
    private void initUIs() {
        TBcontactsuport = findViewById(R.id.TBcontactsupport);
        callusbtn =findViewById(R.id.call_btn);
        callusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0969819947"));
                startActivity(intent);
            }
        });
    }
    private void ActionToolBar() {
        setSupportActionBar(TBcontactsuport);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TBcontactsuport.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TBcontactsuport.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}