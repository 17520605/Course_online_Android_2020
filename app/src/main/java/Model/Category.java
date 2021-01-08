package Model;

public class Category {
    private String _id;
    private String name;
    private String image;
    private String __v;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String get__v() {
        return __v;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
