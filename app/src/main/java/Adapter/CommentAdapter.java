package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CourseActivity;
import Model.CommentModel;
import Model.ListCourseItemModel;
import Ultilities.Ultitities;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> data;

    public CommentAdapter(List<CommentModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.course_cmt_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CommentModel comment = data.get(position);
        Picasso.get().load("http://149.28.24.98:9000/upload/user_image/" + comment.idUser.image)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.image);
        holder.name.setText(comment.idUser.name);
        holder.content.setText(comment.content);
        holder.rating.setRating(comment.numStar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public RatingBar rating;
        public TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.Course__Comment_Image);
            this.name = itemView.findViewById(R.id.Course__Comment_Name);
            this.rating = itemView.findViewById(R.id.Course__Comment_RattingBar);
            this.content = itemView.findViewById(R.id.Course__Comment_Content);
        }
    }
}
