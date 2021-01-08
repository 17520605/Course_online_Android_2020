package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import Adapter.CommentAdapter;
import Adapter.CourseAdapter;
import Model.CommentModel;
import Model.CourseJoinedModel;
import Model.CourseModel;
import Model.ListCourseItemModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Ultilities.Ultitities.FortmatDateString;


public class CourseActivity extends AppCompatActivity {

    private CourseModel _model;
    private ImageView image;
    private TextView title;
    private TextView author;
    private TextView updatedate;
    private TextView goal;
    private TextView description;
    private RatingBar rating;
    private TextView rank;
    private TextView totalvote;
    private RecyclerView comments;
    private RecyclerView recommendcourses;
    private TextView price;
    private TextView baseprice;
    private Button addToCartBtn;
    private Button joinnowBtn;
    private Button learnnowBtn;
    private Button writeComment;
    private LinearLayout wrapper;

    private AlertDialog dialog;
    private View dialogView;

    private RatingBar dialog_ratingBar;
    private TextView dialog_comment;
    private Button dialog_send;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        initUIs();
        initEvents();
        Load();
    }

    private void initUIs() {
        wrapper = findViewById(R.id.CourseOverview_Wrapper);
        image = findViewById(R.id.CourseOverview_Image);
        title = findViewById(R.id.CourseOverview_Title);
        author = findViewById(R.id.CourseOverview_Author);
        rank = findViewById(R.id.CourseOverview_Rank);
        updatedate = findViewById(R.id.CourseOverview_UpdateDate);
        goal = findViewById(R.id.CourseOverview_Goal);
        description = findViewById(R.id.CourseOverview_Description);
        rating = findViewById(R.id.CourseOverview_Ratting);
        totalvote = findViewById(R.id.CourseOverview_TotalVote);
        comments = findViewById(R.id.CourseOverview_Comments);
        recommendcourses = findViewById(R.id.CourseOverview_RecommendCourses);
        price = findViewById(R.id.CourseOverview_Price);
        baseprice = findViewById(R.id.CourseOverview_BasePrice);
        addToCartBtn = findViewById(R.id.CourseOverview_AddToCartButton);
        joinnowBtn = findViewById(R.id.CourseOverview_JoindNowBtn);
        learnnowBtn = findViewById(R.id.CourseOverview_LearnNowBtn);
        writeComment = findViewById(R.id.CourseOverview_WriteCommentButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
        dialogView= LayoutInflater.from(this).inflate(R.layout.create_rate_dialog, wrapper , false);
        builder.setView(dialogView);
        dialog = builder.create();

        dialog_ratingBar = dialogView.findViewById(R.id.Course__CommetDialog_RattingBar);
        dialog_comment = dialogView.findViewById(R.id.Course__CommetDialog_CommentText);
        dialog_send = dialogView.findViewById(R.id.Course__CommetDialog_SendBtn);

    }

    private void initEvents(){
        addToCartBtn.setOnClickListener(v -> {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            JSONArray courses = Ultitities.GetCoursesOnCart(pref);
            if(courses != null){
                JSONArray arr = Ultitities.GetCoursesOnCart(pref);
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("_id", _model.get_id());
                    obj.put("name", _model.getName());
                    obj.put("image", _model.getImage());
                    obj.put("price", _model.getPrice());
                    obj.put("discount", _model.getDiscount());
                    arr.put(obj);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("coursesoncart");
                    editor.putString("coursesoncart", arr.toString());
                    editor.commit();
                    Toasty.success(CourseActivity.this, "Thêm khóa học thành công !", Toast.LENGTH_SHORT, true).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.warning(CourseActivity.this, "Thêm khóa học không thành công !", Toast.LENGTH_SHORT, true).show();
                }

            }
            addToCartBtn.setText("Đã có trong giỏ hàng");
            addToCartBtn.setEnabled(false);
        });

        joinnowBtn.setOnClickListener(v -> {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            User user = Ultitities.GetUser(pref);
            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.JoinCourse(user.get_id(), _model.get_id())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                addToCartBtn.setVisibility(View.GONE);
                                joinnowBtn.setVisibility(View.GONE);
                                learnnowBtn.setVisibility(View.VISIBLE);
                                Toasty.success(CourseActivity.this, "Tham gia thành công !", Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toasty.error(CourseActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT, true).show();
                        }
                    });
        });

        learnnowBtn.setOnClickListener(v -> {
            Intent intent = new Intent( this, ListCourseJoinedActivity.class);
            startActivity(intent);
        });

        writeComment.setOnClickListener(v -> {
            CleanCommentDialog();
            dialog.show();
        });

        dialog_send.setOnClickListener(v -> {
            SendRatting();
        });
    }

    private void Load(){
        String courseId = getIntent().getStringExtra("COURSE_ID");
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetCourseById("course/getbyid/" + courseId).enqueue(new Callback<CourseModel>() {
            @Override
            public void onResponse(Call<CourseModel> call, Response<CourseModel> response) {
                _model = response.body();
                Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + _model.getImage())
                        .placeholder(R.drawable.loading2)
                        .error(R.drawable.loading2)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(image);
                title.setText(_model.getName());

                rank.setText(_model.ranking);
                updatedate.setText( FortmatDateString(_model.getCreated_at()));
                goal.setText(_model.getGoal());
                description.setText(_model.getDescription());
                if(getIntent().getStringExtra("AUTHOR_NAME") != null && getIntent().getStringExtra("AUTHOR_NAME").length() > 0){
                    author.setText(getIntent().getStringExtra("AUTHOR_NAME"));
                }
                else {
                    author.setText(_model.getIdUser());
                }
                Long longprice = (long)(Long.parseLong(_model.getPrice())* ((100.0-Float.parseFloat(_model.getDiscount()))/100.0));
                price.setText(Ultitities.ConvertToCurrency(longprice));
                baseprice.setText(Ultitities.ConvertToCurrency(_model.getPrice()));
                baseprice.setPaintFlags(baseprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                if(_model.getDiscount().compareTo("0")==0){
                    baseprice.setVisibility(View.GONE);
                }
                CheckCourse(courseId);
            }

            @Override
            public void onFailure(Call<CourseModel> call, Throwable t) {
                Toasty.warning(CourseActivity.this, "Lỗi hệ thống máy chủ !", Toast.LENGTH_SHORT, true).show();
            }
        });

        //Load suggested courses
        ICourseService courseService = ServiceClient.getInstance().create(ICourseService.class);
        courseService.GetTopCourses().enqueue(new Callback<List<ListCourseItemModel>>() {
            @Override
            public void onResponse(Call<List<ListCourseItemModel>> call, Response<List<ListCourseItemModel>> response) {
                CourseAdapter adapter = new CourseAdapter(response.body());
                recommendcourses.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ListCourseItemModel>> call, Throwable t) {

            }
        });

        // load comments
        service.GetALLCommentOfCourse("rate/get-rate-by-course/" + courseId)
                .enqueue(new Callback<List<CommentModel>>() {
                    @Override
                    public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                        CommentAdapter adapter = new CommentAdapter(response.body());
                        comments.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<CommentModel>> call, Throwable t) {

                    }
                });

    }


    private void CheckCourse(String courseId){
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = Ultitities.GetUser(pref);
        service.GetJoinedCourses("join/get-courses-joined-by-user/" + user.get_id())
                .enqueue(new Callback<List<CourseJoinedModel>>() {
                    @Override
                    public void onResponse(Call<List<CourseJoinedModel>> call, Response<List<CourseJoinedModel>> response) {
                        if(response != null && response.body()!= null){
                            addToCartBtn.setVisibility(View.VISIBLE);
                            for(int i = 0; i < response.body().size(); i++){
                                CourseJoinedModel course = response.body().get(i);
                                if(course.idCourse != null && course.idCourse._id.compareTo(courseId)==0){
                                    addToCartBtn.setVisibility(View.GONE);
                                    joinnowBtn.setVisibility(View.GONE);
                                    learnnowBtn.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }

                            if(_model.getPrice().compareTo("0")==0){ // kiểm tra khóa học free
                                addToCartBtn.setVisibility(View.GONE);
                                joinnowBtn.setVisibility(View.VISIBLE);
                                learnnowBtn.setVisibility(View.GONE);
                            }
                            // kiểm tra giỏ hàng
                            if(IsOnCart(_model.get_id())){
                                addToCartBtn.setText("Đã có trong giỏ hàng");
                                addToCartBtn.setEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseJoinedModel>> call, Throwable t) {

                    }
                });
    }

    private boolean IsOnCart(String courseid) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JSONArray arr = Ultitities.GetCoursesOnCart(pref);
        try {
            for(int i = 0; i<arr.length(); i++){
                if(arr.getJSONObject(i).getString("_id").compareTo(courseid) == 0){
                    return  true;
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isValidate(){
        if(dialog_ratingBar.getRating() <= 0.0){
            Toasty.warning(CourseActivity.this, "Phải chọn số sao !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(dialog_comment.getText().length() <= 0){
            Toasty.warning(CourseActivity.this, "Lời bình luận rỗng !", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void CleanCommentDialog(){
        dialog_ratingBar.setRating(0);
        dialog_comment.setText("");
    }

    private void SendRatting(){
        if(isValidate()){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            User user = Ultitities.GetUser(pref);
            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.RateCourse( user.get_id(),
                                _model.get_id(),
                                dialog_comment.getText().toString(),
                                dialog_ratingBar.getRating(),
                                user.getAuthToken()
                    ).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200){
                                Toasty.success(CourseActivity.this, "Đánh giá thành công", Toast.LENGTH_SHORT, true).show();
                            }
                            else
                            if(response.code() == 500){
                                Toasty.error(CourseActivity.this, "Bạn chưa hoàn thành hơn 80% khóa học !", Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toasty.error(CourseActivity.this, "Lỗi kết nối !", Toast.LENGTH_SHORT, true).show();
                        }
                    });
        }
    }
}