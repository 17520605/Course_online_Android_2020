package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.io.File;
import java.util.List;

public class LessonDocumentCreatedAdapter extends RecyclerView.Adapter<LessonDocumentCreatedAdapter.ViewHolder> {
    private List<String> docs;

    public LessonDocumentCreatedAdapter(List<String> docs){
        this.docs = docs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_create_lesson_document, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String doc = docs.get(position);
        String ext = doc.substring(doc.lastIndexOf("."));
        switch (ext){
            case ".doc":
            case ".docx":
                holder.icon.setBackgroundResource(R.drawable.icon_word);
                break;
            case ".pptx":
                holder.icon.setBackgroundResource(R.drawable.icon_powerpoint);
                break;
            case ".pdf":
                holder.icon.setBackgroundResource(R.drawable.icon_pdf);
                break;
            case ".xlsx":
            case ".xlsm":
                holder.icon.setBackgroundResource(R.drawable.icon_excel);
                break;
            default:
                holder.icon.setBackgroundResource(R.drawable.icons_info);
                break;
        }

        holder.filename.setText(doc);
        holder.removeBtn.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView filename;
        public Button removeBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.CreateLesson_Document_Item_FileIcon);
            this.filename = itemView.findViewById(R.id.CreateLesson_Document_Item_FileName);
            this.removeBtn = itemView.findViewById(R.id.CreateLesson_Document_Item_RemoveBtn);
        }
    }


}

