package Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import Fragment.LessonDiscussFragment;
import Fragment.LessonDocumentFragment;
import Fragment.LessonTestFragment;
import Model.LessonModel;

public class LessonTabAdapter extends FragmentPagerAdapter {
    LessonModel _model;
    private Context context;
    int totalTabs;

    public LessonTabAdapter(Context context, @NonNull FragmentManager fm, int totalTabs, LessonModel model) {
        super(fm, totalTabs);
        this.context = context;
        this.totalTabs = totalTabs;
        this._model = model;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LessonDocumentFragment documentFragment = new LessonDocumentFragment(_model.doc);
                return documentFragment;
            case 1:
                LessonDiscussFragment  discussFragment = new LessonDiscussFragment();

                return discussFragment;
            case 2:
                LessonTestFragment testFragment  = new LessonTestFragment();
                return testFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
