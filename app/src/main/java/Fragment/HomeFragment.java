package Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tutorial_v1.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.CourseAdapter;
import Adapter.CourseGridViewAdapter;
import Model.Category;
import Model.ListCourseItemModel;
import Retrofit.IUserService;
import Retrofit.ICategoryService;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Model.User;
import Ultilities.Ultitities;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment
{
    private IUserService userService;

    private boolean isLoaded1 = false;
    private boolean isLoaded2 = false;
    private boolean isLoaded3 = false;

    private ImageSlider imageSlider;
    private View rootView;
    private TextView fullname;
    private RecyclerView categories;
    private RecyclerView freecourses;
    private RecyclerView bestcourses;
    private GridView allcourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider= rootView.findViewById(R.id.image_slider);
        List<SlideModel> imageList=new ArrayList<>();

        imageList.add(new SlideModel("https://images.unsplash.com/photo-1510915228340-29c85a43dcfe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", "“The authority of those who teach is often an obstacle to those who want to learn.”-Marcus Tullius Cicero", true));
        imageList.add(new SlideModel("https://learnworthy.net/wp-content/uploads/2019/12/Why-programming-is-the-skill-you-have-to-learn.jpg","“The ink of the scholar is more holy than the blood of the martyr”― Anonymous, Qurʾan"));
        imageList.add(new SlideModel("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", "“The more that you read, the more things you will know. The more that you learn, the more places you’ll go.” – Dr.  Seus"));
        imageSlider.setImageList(imageList,true);

        initUIs();
        Sync();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUIs() {
        fullname = rootView.findViewById(R.id.home_home_fullname_txt);
        categories = rootView.findViewById(R.id.home_categories_rcv);
        allcourse = rootView.findViewById(R.id.home_freecourses_gv);
        bestcourses = rootView.findViewById(R.id.home_bestcourses_rcv);
    }

    private void Sync(){
        AlertDialog alertDialog;
        alertDialog= new SpotsDialog.Builder().setContext(this.getActivity()).build();
        //alertDialog.setTitle("Load...");
        alertDialog.show();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        User user = Ultitities.GetUser(pref);

        IUserService userService = ServiceClient.getInstance().create(IUserService.class);
        userService.login(user.getEmail(), user.getPassword())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                            Ultitities.SaveUser(pref, response.body());
                            fullname.setText(Ultitities.GetUser(pref).getName());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

        ICategoryService categoryService = ServiceClient.getInstance().create(ICategoryService.class);
        categoryService.GetAllCategories("category/get-all-category")
                       .enqueue(new Callback<List<Category>>() {
                           @Override
                           public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                               CategoryAdapter adapter = new CategoryAdapter(response.body());
                               //categories.setHasFixedSize(true);
                               categories.setAdapter(adapter);
                               isLoaded1 = true;
                               if(isLoaded1 && isLoaded2 && isLoaded3){
                                   alertDialog.dismiss();
                               }
                           }
                           @Override
                           public void onFailure(Call<List<Category>> call, Throwable t) {
                               isLoaded1 = true;
                               if(isLoaded1 && isLoaded2 && isLoaded3){
                                   alertDialog.dismiss();
                               }
                               Toasty.warning(getActivity(), "Danh mục khóa học không hoạt động !", Toast.LENGTH_SHORT, true).show();
                           }
                       });

        ICourseService courseService = ServiceClient.getInstance().create(ICourseService.class);
        courseService.GetAllCourses()
                .enqueue(new Callback<List<ListCourseItemModel>>() {
                    @Override
                    public void onResponse(Call<List<ListCourseItemModel>> call, Response<List<ListCourseItemModel>> response) {
                        CourseGridViewAdapter adapter = new CourseGridViewAdapter(getContext(), response.body());
                        allcourse.setVerticalScrollBarEnabled(false);
                        allcourse.setAdapter(adapter);
                        isLoaded2 = true;
                        if(isLoaded1 && isLoaded2 && isLoaded3){
                            alertDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ListCourseItemModel>> call, Throwable t) {
                        isLoaded2 = true;
                        if(isLoaded1 && isLoaded2 && isLoaded3){
                            alertDialog.dismiss();
                        }
                        Toasty.warning(getActivity(), "Danh mục khóa học không hoạt động !", Toast.LENGTH_SHORT, true).show();
                    }
                });

        courseService.GetTopCourses()
                .enqueue(new Callback<List<ListCourseItemModel>>() {
                    @Override
                    public void onResponse(Call<List<ListCourseItemModel>> call, Response<List<ListCourseItemModel>> response) {
                        CourseAdapter adapter = new CourseAdapter(response.body());
                        categories.setHasFixedSize(true);
                        bestcourses.setAdapter(adapter);
                        isLoaded3 = true;
                        if(isLoaded1 && isLoaded2 && isLoaded3){
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListCourseItemModel>> call, Throwable t) {
                        isLoaded3 = true;
                        if(isLoaded1 && isLoaded2 && isLoaded3){
                            alertDialog.dismiss();
                        }
                        Toasty.warning(getActivity(), "Danh mục khóa học không hoạt động !", Toast.LENGTH_SHORT, true).show();
                    }
                });

    }
}
