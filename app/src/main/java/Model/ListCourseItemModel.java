package Model;

public class ListCourseItemModel {
    private String _id;
    private String __v;
    private Vote vote;
    private String discount;
    private String ranking;
    private String created_at;
    private String is_checked;
    private String is_required;
    private String name;
    private UserSummaryModel idUser;
    private String image;
    private String goal;
    private String description;
    private CategorySummaryModel category;
    private String price;

    public String get_id() {
        return _id;
    }

    public String get__v() {
        return __v;
    }

    public Vote getVote() {
        return vote;
    }

    public String getDiscount() {
        return discount;
    }

    public String getRanking() {
        return ranking;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getIs_checked() {
        return is_checked;
    }

    public String getIs_required() {
        return is_required;
    }

    public String getName() {
        return name;
    }

    public UserSummaryModel getIdUser() {
        return idUser;
    }

    public String getImage() {
        return image;
    }

    public String getGoal() {
        return goal;
    }

    public String getDescription() {
        return description;
    }

    public CategorySummaryModel getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setIs_checked(String is_checked) {
        this.is_checked = is_checked;
    }

    public void setIs_required(String is_required) {
        this.is_required = is_required;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdUser(UserSummaryModel idUser) {
        this.idUser = idUser;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(CategorySummaryModel category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

