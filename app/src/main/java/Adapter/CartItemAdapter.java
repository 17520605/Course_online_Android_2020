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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.List;

import Activity.CourseActivity;
import Model.CourseModel;
import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private List<CourseModel> data;

    public CartItemAdapter(List<CourseModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //NumberFormat formatter = new DecimalFormat("#,###");
        final CourseModel courseModel = data.get(position);
        Picasso.get().load("http://149.28.24.98:9000/upload/course_image/" + courseModel.getImage())
                .placeholder(R.drawable.loading2)
                .error(R.drawable.loading2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.image);

        holder.title.setText(courseModel.getName());
        holder.title.setSelected(true);

        Long price = (long)(Long.parseLong(courseModel.getPrice())* ((100.0-Float.parseFloat(courseModel.getDiscount()))/100.0));
        holder.price.setText(courseModel.getPrice().compareTo("0")  == 0 ? "FREE" : Ultitities.ConvertToCurrency(price));

//        if(courseModel.getPrice().compareTo("0")!=0){
//            holder.baseprice.setText(Ultitities.ConvertToCurrency(courseModel.getPrice()));
//            holder.baseprice.setPaintFlags(holder.baseprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//        }
//        else {
//            holder.baseprice.setVisibility(View.GONE);
//        }
        holder.image.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("COURSE_ID", courseModel.get_id());
            context.startActivity(intent);
        });

        holder.description.setText(courseModel.getDescription());

        holder.remove.setOnClickListener(v -> {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
            JSONArray arr = Ultitities.GetCoursesOnCart(pref);
            if(arr != null){
                arr.remove(position);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("coursesoncart");
                editor.putString("coursesoncart", arr.toString());
                editor.commit();
            }
            Toasty.success(v.getContext(), "Xóa khóa học thành công !", Toast.LENGTH_SHORT, true).show();
            notifyItemRemoved(position);
            data.remove(position);
            notifyItemRangeChanged(position, data.size());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView price;
        public TextView description;
        public TextView baseprice;
        public ImageView remove;

        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.image = itemView.findViewById(R.id._CartItem_Image);
            this.title = itemView.findViewById(R.id._CartItem_Title);
            this.price = itemView.findViewById(R.id._CartItem_Price);
            //this.baseprice = itemView.findViewById(R.id._CartItem_BasePrice);
            this.description = itemView.findViewById(R.id._CartItem_description);
            this.remove = itemView.findViewById(R.id._CartItem_Remove);
        }
    }
}
