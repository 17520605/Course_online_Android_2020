package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tutorial_v1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;

import Fragment.SettingFragment;
import Fragment.HomeFragment;
import Fragment.SearchFragment;
import Fragment.CartFragment;
import Fragment.NotificationFragment;
import Model.UserAccount;

import static android.view.View.GONE;


public class HomeActivity extends AppCompatActivity {
    Toolbar homeTB;
    BottomNavigationView bottomNav;
    public UserAccount userAccount = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUIReference();
        homeTB.setVisibility(GONE);
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    private void setUIReference() {
        bottomNav = findViewById(R.id.bottomNav);
        homeTB=findViewById(R.id.homeTB);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = new Fragment();
        switch (item.getItemId()) {
            case R.id.nav_btn_account:
                homeTB.setVisibility(View.VISIBLE);
                homeTB.setTitle("Account");
                homeTB.setTitleTextColor(-1);
                fragment = new SettingFragment();
                break;
            case R.id.nav_btn_home:
                homeTB.setVisibility(GONE);
                homeTB.setTitle("");
                homeTB.setTitleTextColor(-1);
                fragment = new HomeFragment();
                break;
            case R.id.nav_btn_search:
                homeTB.setVisibility(GONE);
                homeTB.setTitle("Search");
                homeTB.setTitleTextColor(-1);
                fragment = new SearchFragment();
                break;
            case R.id.nav_btn_cart:
                homeTB.setVisibility(View.VISIBLE);
                homeTB.setTitle("Cart");
                homeTB.setTitleTextColor(-1);
                fragment = new CartFragment();
                break;
            case R.id.nav_btn_notification:
                homeTB.setVisibility(View.VISIBLE);
                homeTB.setTitle("Notificattion");
                homeTB.setTitleTextColor(-1);
                fragment = new NotificationFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        return true;
    }
}