package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CourseDetailActivity;
import Activity.ListLessonCreatedActivity;
import Activity.UpdateCourseActivity;
import Model.ListCourseItemModel;
import Ultilities.Ultitities;

public class CourseCreatedAdapter extends RecyclerView.Adapter<CourseCreatedAdapter.ViewHolder> {
    private List<ListCourseItemModel> data;

    public CourseCreatedAdapter(List<ListCourseItemModel> data){
        this.data = data;
    }

    @Override
    public CourseCreatedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_course_created, parent, false);
        CourseCreatedAdapter.ViewHolder viewHolder = new CourseCreatedAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseCreatedAdapter.ViewHolder holder, int position) {
        final ListCourseItemModel course = data.get(position);
        Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.getImage())
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.image);

        holder.title.setText(course.getName());
        holder.title.setSelected(true);
        holder.date.setText(Ultitities.FortmatDateString(course.getCreated_at()));

        Long price = (long)(Long.parseLong(course.getPrice())* ((100.0-Float.parseFloat(course.getDiscount()))/100.0));
        holder.price.setText(course.getPrice().compareTo("0")  == 0 ? "FREE" : Ultitities.ConvertToCurrency(price));

        if(course.getPrice().compareTo("0")!=0){
            holder.baseprice.setText(Ultitities.ConvertToCurrency(course.getPrice()));
            holder.baseprice.setPaintFlags(holder.baseprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.baseprice.setVisibility(View.GONE);
        }

        holder.wrapper.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, ListLessonCreatedActivity.class);
            intent.putExtra("COURSE_ID", course.get_id());
            context.startActivity(intent);
        });
        holder.detailbtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent=new Intent(context, CourseDetailActivity.class);
            intent.putExtra("COURSE_ID", course.get_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout wrapper;
        public ImageView image;
        public TextView title;
        public TextView date;
        public TextView price;
        public TextView baseprice;
        public Button detailbtn;
        public ViewHolder(View itemView) {
            super(itemView);
            this.wrapper = itemView.findViewById(R.id.CourseCreated__Item_Wrapper);
            this.image = itemView.findViewById(R.id.CourseCreated__Item_Image);
            this.title = itemView.findViewById(R.id.CourseCreated__Item_Title);
            this.date =  itemView.findViewById(R.id.CourseCreated__Item_DateCreate);
            this.price =  itemView.findViewById(R.id.CourseCreated__Item_Price);
            this.baseprice = itemView.findViewById(R.id.CourseCreated__Item_BasePrice);
            this.detailbtn = itemView.findViewById(R.id.CourseCreated__Item_Detail_btn);
        }
    }
}
