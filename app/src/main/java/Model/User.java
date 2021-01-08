package Model;

import java.io.Serializable;

public class User implements Serializable {

    private String active;
    private String created_at;
    private String _id;
    private String email;
    private String name;
    private String password;
    private String phone;
    private String address;
    private String description;
    private String role;
    private String image;
    private String gender;
    private String __v;
    private String activeToken;
    private String authToken;

    public String getActive() {
        return active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getRole() {
        return role;
    }

    public String getImage() {
        return image;
    }

    public String getGender() {
        return gender;
    }

    public String get__v() {
        return __v;
    }

    public String getActiveToken() { return activeToken; }

    public String getAuthToken() { return authToken; }


    public void setActive(String active) {
        this.active = active;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveToken(String activeToken) {
        this.activeToken = activeToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User(String __v) {

    }

    public User(String active, String created_at, String _id, String email, String name, String password, String phone, String address, String description, String role, String image, String gender, String __v, String activeToken , String authToken) {
        this.active = active;
        this.created_at = created_at;
        this._id = _id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.role = role;
        this.image = image;
        this.gender = gender;
        this.__v = __v;
        this.activeToken = activeToken;
        this.authToken = authToken;
    }
}
