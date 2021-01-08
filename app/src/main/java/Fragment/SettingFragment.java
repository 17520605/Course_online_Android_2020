package Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import Activity.AccountSettingActivity;
import Activity.ActiveAccountActivity;
import Activity.ChangePasswordActivity;
import Activity.ContactSupportActivity;
import Activity.HistoryActivity;
import Activity.ListCourseCreatedActivity;
import Activity.ListCourseJoinedActivity;
import Activity.LoginActivity;
import Model.User;
import Retrofit.IUserService;
import Ultilities.Ultitities;

public class SettingFragment extends Fragment
{

    private User user;
    private IUserService service;

    private View rootView;
    private TextView Name;
    private ImageView Active_ImageView;
    private ImageView courseCreated;
    private ImageView avatar;
    private ImageView NonActive_ImageView;
    private ImageView logout;
    private ImageView accountSetting;
    private ImageView contact;
    private ImageView changepassword;
    private ImageView coursesJoined;
    private ImageView historycourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        initUIs();
        initEventHandles();
        Load();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Update UI from the child-activity.
    }

    private void initUIs() {
        avatar = rootView.findViewById(R.id.accountFrag_user_avatar);
        courseCreated = rootView.findViewById(R.id.Home_Setting_CourseCreated);
        Name = rootView.findViewById(R.id.accountFrag_user_name);
        accountSetting = rootView.findViewById(R.id.account_setting);
        contact = rootView.findViewById(R.id.contact_support);
        Active_ImageView = rootView.findViewById(R.id.active_btn);
        NonActive_ImageView = rootView.findViewById(R.id.nonactive_btn);
        coursesJoined = rootView.findViewById(R.id.Setting_Account_CoursesJoined);
        changepassword = rootView.findViewById(R.id.home_account_changepassword_btn);
        logout = rootView.findViewById(R.id.account_account_logout_btn);
        historycourse = rootView.findViewById(R.id.payment_history);
    }

    private void initEventHandles(){
        accountSetting.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), AccountSettingActivity.class);
            getActivity().startActivity(intent);
        });
        courseCreated.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), ListCourseCreatedActivity.class);
            getActivity().startActivity(intent);
        });
        coursesJoined.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), ListCourseJoinedActivity.class);
            getActivity().startActivity(intent);
        });
        NonActive_ImageView.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), ActiveAccountActivity.class);
            getActivity().startActivity(intent);
        });
        historycourse.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), HistoryActivity.class);
            getActivity().startActivity(intent);
        });
        contact.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), ContactSupportActivity.class);
            getActivity().startActivity(intent);
        });

        changepassword.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), ChangePasswordActivity.class);
            getActivity().startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            pref.edit().clear().commit();
            Intent intent = new Intent( getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);

        });
    }

    private void Load(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        user = Ultitities.GetUser(pref);
        if(user != null){
            this.Name.setText(user.getName());
            this.Name.setSelected(true);
            if(user.getActive().compareTo("1")==0){
                Active_ImageView.setVisibility(View.VISIBLE);
                NonActive_ImageView.setVisibility(View.GONE);
            }
            else {
                Active_ImageView.setVisibility(View.GONE);
                NonActive_ImageView.setVisibility(View.VISIBLE);
            }

            Picasso.get().load("http://149.28.24.98:9000/upload/user_image/" + user.getImage())
                    .placeholder(R.drawable.noavatar1)
                    .error(R.drawable.noavatar1)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(avatar);
        }
    }
}

