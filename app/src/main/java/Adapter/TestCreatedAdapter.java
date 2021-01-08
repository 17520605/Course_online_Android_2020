package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import Activity.CreateTestActivity;
import Activity.ListLessonCreatedActivity;
import Activity.ListQuestionCreatedActivity;
import Activity.ListTestCreatedActivity;
import Activity.UpdateCourseActivity;
import Activity.UpdateTestActivity;
import Model.ListCourseItemModel;
import Model.MutipleChoiceModel;
import Model.TestResquestModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestCreatedAdapter extends RecyclerView.Adapter<TestCreatedAdapter.ViewHolder> {
    private List<MutipleChoiceModel> data;
    private String courseId;
    private String lessonId;

    public TestCreatedAdapter(List<MutipleChoiceModel> data, String courseId, String lessonId){
        this.data = data;
        this.courseId = courseId;
        this.lessonId = lessonId;
    }

    @Override
    public TestCreatedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_test_created, parent, false);
        TestCreatedAdapter.ViewHolder viewHolder = new TestCreatedAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TestCreatedAdapter.ViewHolder holder, int position) {
        final MutipleChoiceModel mutipleChoice = data.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.title.setText(mutipleChoice.question);
        holder.edit.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, UpdateTestActivity.class);
            intent.putExtra("COURSE_ID", courseId);
            intent.putExtra("LESSON_ID", lessonId);
            intent.putExtra("TESTS", new Gson().toJson(data));
            intent.putExtra("TEST_ID", mutipleChoice._id);
            context.startActivity(intent);
        });

        holder.delete.setOnClickListener(v -> {
            notifyItemRemoved(position);
            data.remove(position);
            notifyItemRangeChanged(position, data.size());
            UpdateTest(v.getContext());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout wrapper;
        public TextView number;
        public TextView title;
        public ImageView edit;
        public ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.wrapper = (LinearLayout)itemView.findViewById(R.id.Item_TestCreated__Wrapper);
            this.number = (TextView)itemView.findViewById(R.id.Item_TestCreated__Number);
            this.title = (TextView)itemView.findViewById(R.id.Item_TestCreated__Title);
            this.edit = (ImageView) itemView.findViewById(R.id.Item_TestCreated__EditBtn);
            this.delete = (ImageView) itemView.findViewById(R.id.Item_TestCreated__DeleteBtn);
        }
    }

    private void UpdateTest(Context context){
        TestResquestModel resquest = new TestResquestModel();
        resquest.multipleChoices = data;
        String json = new Gson().toJson(resquest);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        User user =  Ultitities.GetUser(pref);
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);

        service.UpdateTest("lesson/add-list-multiple-choice/" + lessonId,
                RequestBody.create(MediaType.parse("application/json"), json),
                user.getAuthToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Intent intent=new Intent(context, ListTestCreatedActivity.class);
                            intent.putExtra("LESSON_ID", lessonId);
                            context.startActivity(intent);
                            Toasty.success(context, "Xóa bài tập thành công !", Toast.LENGTH_SHORT, true).show();
                        }
                        else {
                            Toasty.error(context, "Xóa bài tập thất bại !", Toast.LENGTH_SHORT, true).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


    }
}
