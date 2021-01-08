package Model;

public class CategorySummaryModel {
    public String _id;
    public String name;

    @Override
    public String toString(){
        return name == null ? "" : name;
    }
}
