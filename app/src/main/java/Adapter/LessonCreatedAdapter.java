package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Activity.ListLessonCreatedActivity;
import Activity.ListQuestionCreatedActivity;
import Activity.ListTestCreatedActivity;
import Activity.UpdateCourseActivity;
import Activity.UpdateLessonActivity;
import Model.LessonModel;
import Model.User;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonCreatedAdapter extends RecyclerView.Adapter<LessonCreatedAdapter.ViewHolder> {
    private List<LessonModel> data;
    private String courseId;
    public LessonCreatedAdapter(List<LessonModel> data, String courseId){
        this.data = data;
        this.courseId = courseId;
    }

    @Override
    public LessonCreatedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lesson_created, parent, false);
        LessonCreatedAdapter.ViewHolder viewHolder = new LessonCreatedAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LessonCreatedAdapter.ViewHolder holder, int position) {
        final LessonModel lessonModel = data.get(position);
        holder.title.setText(lessonModel.getTitle());
        holder.title.setSelected(true);
        holder.number.setText(lessonModel.getOrder());

        holder.edit.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, UpdateLessonActivity.class);
            intent.putExtra("LESSON_ID", lessonModel.get_id());
            intent.putExtra("COURSE_ID", courseId);
            context.startActivity(intent);
        });
        holder.test.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, ListTestCreatedActivity.class);
            intent.putExtra("LESSON_ID", lessonModel.get_id());
            intent.putExtra("COURSE_ID", courseId);
            context.startActivity(intent);
        });
        holder.question.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, ListQuestionCreatedActivity.class);
            intent.putExtra("LESSON_ID", lessonModel.get_id());
            intent.putExtra("COURSE_ID", courseId);
            context.startActivity(intent);
        });

        holder.detete.setOnClickListener(v -> {
            Context context = v.getContext();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            User user =  Ultitities.GetUser(pref);
            ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
            service.DeleteLesson("lesson/delete-lesson/" + lessonModel._id, user.getAuthToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Toasty.success(context, "Xóa bài học thành công", Toast.LENGTH_SHORT, true).show();
                                Context context = v.getContext();
                                Intent intent=new Intent(context, ListLessonCreatedActivity.class);
                                intent.putExtra("COURSE_ID", courseId);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView number;
        public ImageView edit;
        public ImageView test;
        public ImageView question;
        public ImageView detete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.LessonCreated__Title);
            this.number = itemView.findViewById(R.id.LessonCreated__Number);
            this.edit = itemView.findViewById(R.id.LessonCreated_edit);
            this.test = itemView.findViewById(R.id.LessonCreated_test);
            this.question = itemView.findViewById(R.id.LessonCreated_question);
            this.detete = itemView.findViewById(R.id.LessonCreated_delete);
        }
    }


}
