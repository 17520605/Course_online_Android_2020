package Model;

public class CourseModel {
    public String _id;
    public String __v;
    public Vote vote;
    public String discount;
    public String ranking;
    public String created_at;
    public String is_checked;
    public String is_required;
    public String name;
    public String idUser;
    public String image;
    public String goal;
    public String description;
    public String category;
    public String price;
    public String author;


    public String get_id() {
        return _id;
    }

    public String get__v() {
        return __v;
    }

    public Vote getVote() {
        return vote;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
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

    public String getIdUser() {
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

    public String getCategory() {
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

    public void setIdUser(String idUser) {
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
