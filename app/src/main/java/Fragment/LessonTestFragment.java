package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import java.util.List;

import Adapter.ListTestItemAdapter;
import Model.LessonTestResponeModel;
import Model.MutipleChoiceModel;
import Retrofit.ICourseService;
import Retrofit.ServiceClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LessonTestFragment extends Fragment
{
    private List<MutipleChoiceModel> _model;
    private View root;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lesson_tab_multiplechoice, container, false);
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
        recyclerView = root.findViewById(R.id.Lesson_Tabs_TestTab_RecyclerView);
    }

    private void InitEvents(){

    }

    private void Load(){
        String lessonId = getActivity().getIntent().getStringExtra("LESSON_ID");
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        service.GetTestOfLesson("lesson/get-multiple-choice-for-test/" + lessonId)
                .enqueue(new Callback<List<LessonTestResponeModel>>() {
                    @Override
                    public void onResponse(Call<List<LessonTestResponeModel>> call, Response<List<LessonTestResponeModel>> response) {
                            if(response.isSuccessful()){
                                ListTestItemAdapter adapter = new ListTestItemAdapter(response.body(), lessonId);
                                recyclerView.setAdapter(adapter);
                            }
                    }

                    @Override
                    public void onFailure(Call<List<LessonTestResponeModel>> call, Throwable t) {

                    }
                });
    }
}

