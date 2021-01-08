package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Model.LessonTestResponeModel;
import Model.MutipleChoiceSumaryModel;

public class ListTestAdapter extends RecyclerView.Adapter<ListTestAdapter.ViewHolder> {
    private List<MutipleChoiceSumaryModel> data;

    public ListTestAdapter(List<MutipleChoiceSumaryModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_test, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MutipleChoiceSumaryModel test = data.get(position);
        holder.order.setText(String.valueOf(position+1));
        holder.question.setText(String.valueOf(test.question));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView order;
        public TextView question;
        public RadioButton A;
        public RadioButton B;
        public RadioButton C;
        public RadioButton D;
        public ViewHolder(View itemView) {
            super(itemView);
            this.order = (TextView) itemView.findViewById(R.id.ListLessonTest_Item_Order);
            this.question = (TextView) itemView.findViewById(R.id.ListLessonTest_Item_Question);
            this.A = (RadioButton) itemView.findViewById(R.id.ListLessonTest_Item_AnsA);
            this.B = (RadioButton) itemView.findViewById(R.id.ListLessonTest_Item_AnsB);
            this.C = (RadioButton) itemView.findViewById(R.id.ListLessonTest_Item_AnsC);
            this.D = (RadioButton) itemView.findViewById(R.id.ListLessonTest_Item_AnsD);
        }
    }
}
