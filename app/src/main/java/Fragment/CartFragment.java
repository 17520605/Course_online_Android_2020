package Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import Activity.PaymentActivity;
import Adapter.CartItemAdapter;
import Model.CourseModel;
import Ultilities.Ultitities;


public class CartFragment extends Fragment
{
    private ArrayList<CourseModel> _model = new ArrayList<CourseModel>();
    private View root;
    private RecyclerView courses;
    private ImageView nothing;
    private Button paybtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        initUIs();
        initEvents();
        Sync();
        Load();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Sync(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        JSONArray arr = Ultitities.GetCoursesOnCart(pref);
        try {
            for (int i = 0; i< arr.length(); i++) {
                CourseModel courseModel = new CourseModel();
                courseModel.set_id(arr.getJSONObject(i).getString("_id"));
                courseModel.setImage(arr.getJSONObject(i).getString("image"));
                courseModel.setName(arr.getJSONObject(i).getString("name"));
                courseModel.setPrice(arr.getJSONObject(i).getString("price"));
                courseModel.setDiscount(arr.getJSONObject(i).getString("discount"));
                _model.add(courseModel);
                paybtn.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUIs() {
        courses = root.findViewById(R.id.Home_Cart_Courses);
        nothing = root.findViewById(R.id.Home_Cart_NothingImage);
        paybtn = root.findViewById(R.id.Home_Cart_PayButton);
    }

    private void initEvents(){
        paybtn.setOnClickListener(v -> {
            Intent intent = new Intent( getActivity(), PaymentActivity.class);
            getActivity().startActivity(intent);
        });
    }

    private void Load(){
        if(_model.size() > 0){
            nothing.setVisibility(View.GONE);
            CartItemAdapter adapter = new CartItemAdapter(_model);
            courses.setAdapter(adapter);
        }
        else {
            nothing.setVisibility(View.VISIBLE);
        }
    }
}

