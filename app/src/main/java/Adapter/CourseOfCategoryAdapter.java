package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CourseActivity;
import Model.ListCourseItemModel;
import Ultilities.Ultitities;

public class CourseOfCategoryAdapter extends RecyclerView.Adapter<CourseOfCategoryAdapter.ViewHolder> {

    private List<ListCourseItemModel> data;

    public CourseOfCategoryAdapter(List<ListCourseItemModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListCourseItemModel course = data.get(position);
        Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.getImage())
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.image);
        holder.name.setText(course.getName());
        holder.name.setSelected(true);
        holder.description.setText(course.getDescription());


        //holder.price.setText(course.getPrice().compareTo("0")  == 0 ? "FREE" : course.getPrice() + " đ" );

        Long price = (long)(Long.parseLong(course.getPrice())* ((100.0-Float.parseFloat(course.getDiscount()))/100.0));
        holder.price.setText(course.getPrice().compareTo("0")  == 0 ? "FREE" : Ultitities.ConvertToCurrency(price));

        if(course.getPrice().compareTo("0")!=0){
            holder.baseprice.setText(Ultitities.ConvertToCurrency(course.getPrice()));
            holder.baseprice.setPaintFlags(holder.baseprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.baseprice.setSelected(true);
        }
        else {
            holder.baseprice.setVisibility(View.GONE);
        }

        holder.image.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("COURSE_ID", course.get_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView description;
        public TextView price;
        public TextView baseprice;


        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.search_image);
            this.name = itemView.findViewById(R.id.search_name);
            this.description = itemView.findViewById(R.id.search_description);
            this.price = itemView.findViewById(R.id.search_price);
            this.baseprice = itemView.findViewById(R.id.search__Item_BasePrice);
        }
    }
}
