package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CourseActivity;
import Model.ListCourseItemModel;
import Ultilities.Ultitities;

public class CourseGridViewAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflter;
    private List<ListCourseItemModel> data;
    public CourseGridViewAdapter(Context context, List<ListCourseItemModel> data){
        this.context = context;
        this.data = data;
        this.inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int i) {
        return data.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListCourseItemModel course = data.get(i);
        view = inflter.inflate(R.layout.course_item, null);
        ImageView image = view.findViewById(R.id.course_image);
        TextView title = view.findViewById(R.id.course_title);
        RatingBar rating = view.findViewById(R.id.course_rating);
        TextView total = view.findViewById(R.id.course_totalVote);
        TextView fee = view.findViewById(R.id.course_fee);
        TextView discount = view.findViewById(R.id.course_discount);

        Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + course.getImage())
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(image);
        title.setText(course.getName());

        Long price = (long)(Long.parseLong(course.getPrice())* ((100.0-Float.parseFloat(course.getDiscount()))/100.0));
        fee.setText(course.getPrice().compareTo("0")  == 0 ? "FREE" : Ultitities.ConvertToCurrency(price));
        total.setText(course.getVote().getTotalVote());
        rating.setRating(Float.valueOf(course.getVote().getEVGVote()));
        image.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("COURSE_ID", course.get_id());
            intent.putExtra("AUTHOR_NAME", course.getIdUser().name);
            context.startActivity(intent);
        });

        return view;
    }
}