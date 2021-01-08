package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Activity.LessonActivity;
import Model.LessonModel;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private List<LessonModel> data;

    public LessonAdapter(List<LessonModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.joined_lesson, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LessonModel lesson = data.get(position);
        holder.order.setText(lesson.order);
        holder.title.setText(lesson.title);
        holder.title.setSelected(true);
        if(lesson.isCompleted){
            holder.check.setVisibility(View.VISIBLE);
        }
        else {
            holder.check.setVisibility(View.GONE);
        }
        holder.wrapper.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, LessonActivity.class);
            intent.putExtra("LESSON_ID", lesson._id);
            intent.putExtra("COURSE_ID", lesson.idCourse);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout wrapper;
        public TextView order;
        public TextView title;
        public ImageView check;
        public ViewHolder(View itemView) {
            super(itemView);
            this.wrapper = itemView.findViewById(R.id.CourseJoined_Lesson_Wrapper);
            this.order = itemView.findViewById(R.id.CourseJoined_Lesson_OrderNumber);
            this.title = itemView.findViewById(R.id.CourseJoined_Lesson_Title);
            this.check = itemView.findViewById(R.id.CourseJoined_Lesson_Check);
        }
    }
}
