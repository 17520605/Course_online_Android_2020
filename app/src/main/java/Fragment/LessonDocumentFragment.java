package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.LessonDocumentAdapter;


public class LessonDocumentFragment extends Fragment
{
    private List<String> documents;
    private RecyclerView recyclerView;
    private View root;

    public LessonDocumentFragment(List<String> documents){
        super();
        this.documents = documents;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lesson_tab_document, container, false);
        InitUIs();
        InitEvents();
        Load();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void InitUIs() {
        recyclerView = root.findViewById(R.id.Lesson_DocumentTab_RecyclerView);
    }

    private void InitEvents(){

    }

    private void Load(){
        if(documents != null){
            LessonDocumentAdapter adapter = new LessonDocumentAdapter(documents);
            recyclerView.setAdapter(adapter);
        }
    }

}

