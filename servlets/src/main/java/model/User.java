package model;

/**
 * Created by Freemind on 2016-11-06.
 */
public class User {
    private int id;
    private String fullName;
    private String password;
    private String photoUrl;
    private String role;


    public User(int id, String fullName, String password, String photoUrl, String role) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    public User(String fullName, String password, String photoUrl, String role) {
        this(0,fullName,password,photoUrl,role);
    }




    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
