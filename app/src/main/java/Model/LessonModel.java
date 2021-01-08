package Model;

import java.util.List;

public class LessonModel {
    public String _id;
    public List<String> doc;
    public List<MutipleChoiceModel> multipleChoices;
    public List<PopupQuestionModel> popupQuestion;
    public boolean isCompleted;
    public String video;
    public String idCourse;
    public String title;
    public String order;
    public String __v;

    public String getTitle() {
        return title;
    }
    public String getOrder() {
        return order;
    }
    public String get_id() {
        return _id;
    }
}
