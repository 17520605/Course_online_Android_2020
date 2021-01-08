package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import Adapter.SearchResultAdapter;
import Model.Category;
import Model.CourseModel;
import Retrofit.ISearchService;
import Retrofit.ServiceClient;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment
{
    ChipGroup chipGroup;
    ArrayList<Category> items = new ArrayList<>();
    private View root;
    private EditText search;
    private RecyclerView results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        InitUIs();
        InitEvents();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void InitUIs() {
        chipGroup=root.findViewById(R.id.chipGroup);
        search = root.findViewById(R.id.home_search_search_edt);
        results = root.findViewById(R.id.home_search_results_rcv);
        ArrayList<String> suggest = new ArrayList<String>();
        suggest.add("lap trinh");
        suggest.add("ielts");
        suggest.add("game");
        suggest.add("python");
        for(int i=0;i<suggest.size();i++)
        {
            addChip(suggest.get(i));
        }

    }

    private void InitEvents(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty() == false){
                    Search(s.toString());
                }

            }
        });
    }

    private void addChip(String s) {

        // Create a Chip from Layout.
        Chip newChip = (Chip)getLayoutInflater().inflate(R.layout.suggestlabel, chipGroup, false);
        newChip.setTextSize(15);
        newChip.setPadding(5,20,5,20);
        newChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search(s);
            }
        });
        newChip.setChipText(s);
        newChip.setText(s);
        chipGroup.addView(newChip);
    }


    private void Search(String text){
        ISearchService courseService = ServiceClient.getInstance().create(ISearchService.class);
        courseService.SearchCourses("course/search-course/" + text)
                .enqueue(new Callback<List<CourseModel>>() {
                    @Override
                    public void onResponse(Call<List<CourseModel>> call, Response<List<CourseModel>> response) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().size()), Toast.LENGTH_LONG).show();
                        SearchResultAdapter adapter = new SearchResultAdapter(response.body());
                        results.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<CourseModel>> call, Throwable t) {
                        Toasty.warning(getActivity(), "Danh mục tìm kiếm không hoạt động !.", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}

