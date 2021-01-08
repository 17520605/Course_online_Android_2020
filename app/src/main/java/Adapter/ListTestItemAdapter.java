package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activity.CategoryActivity;
import Activity.ForgotPasswordActivity;
import Activity.ListLessonTestActivity;
import Activity.LoginActivity;
import Model.Category;
import Model.LessonTestResponeModel;

public class ListTestItemAdapter extends RecyclerView.Adapter<ListTestItemAdapter.ViewHolder> {
    private List<LessonTestResponeModel> data;
    private String lessonId;

    public ListTestItemAdapter(List<LessonTestResponeModel> data, String lessonId){
        this.data = data;
        this.lessonId = lessonId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_lesson_tab_test, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LessonTestResponeModel test = data.get(position);
        holder.order.setText(String.valueOf(position+1));
        holder.number.setText(String.valueOf(test.multipleChoices.size()));
        holder.testBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent= new Intent(context, ListLessonTestActivity.class);
            intent.putExtra("TEST_ID", test._id);
            intent.putExtra("LESSON_ID", lessonId);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView order;
        public TextView number;
        public Button testBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            this.order = (TextView) itemView.findViewById(R.id.Lesson_Tab_Test_Item_Order);
            this.number = (TextView) itemView.findViewById(R.id.Lesson_Tab_Test_Item_Number);
            this.testBtn = (Button) itemView.findViewById(R.id.Lesson_Tab_Test_Item_TestBtn);
        }
    }
}
