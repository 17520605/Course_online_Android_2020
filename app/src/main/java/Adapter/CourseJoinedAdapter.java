package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CourseJoinedActivity;
import Model.CourseJoinedModel;

public class CourseJoinedAdapter extends RecyclerView.Adapter<CourseJoinedAdapter.ViewHolder> {
    private List<CourseJoinedModel> data;

    public CourseJoinedAdapter(List<CourseJoinedModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.joined_course_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CourseJoinedModel course = data.get(position);
        holder.title.setSelected(true);
        if(course.idCourse != null){
            Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.idCourse.getImage())
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.loading2)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.image);
            holder.title.setText(course.idCourse.getName());

        }
        holder.progressBar.setProgress(course.percentCompleted);
        holder.percent.setText(course.percentCompleted + "%");
        holder.author.setText(course.idUser);
        holder.date.setText(course.created_at);
        holder.leannow.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, CourseJoinedActivity.class);
            intent.putExtra("COURSE_ID", course.idCourse.get_id());
            intent.putExtra("COURSE_NAME", course.idCourse.getName());
            intent.putExtra("COURSE_IMAGE", course.idCourse.getImage());
            intent.putExtra("COURSE_PROGRESS", course.percentCompleted);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ProgressBar progressBar;
        public TextView percent;
        public Button leannow;
        public TextView date;
        public TextView author;
        public ViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_Image);
            this.title = (TextView) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_Title);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_ProgressBar);
            this.percent = (TextView) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_Percent);
            this.leannow = (Button) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_leannowbtn);
            this.date = (TextView) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_datecreated);
            this.author = (TextView) itemView.findViewById(R.id.ListCourseJoined_CourseJoinedItem_author);
        }
    }
}
