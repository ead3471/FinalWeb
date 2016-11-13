package model;

/**
 * Created by Freemind on 2016-11-06.
 */
public class User {
    private int id;
    private String fullName;
    private String login;
    private String password;
    private String photoUrl;
    private String role;
    private float rate;


    public User(int id,String login, String fullName,String password, String photoUrl, String role,float rate) {
        this.id = id;
        this.login=login;
        this.fullName = fullName;
        this.password = password;
        this.photoUrl = photoUrl;
        this.role = role;
        this.rate=rate;
    }



    public User(String fullName, String login,String password, String photoUrl, String role) {
        this(0,login,fullName,password,photoUrl,role,0);
    }

    public User(String fullName,String photoUrl, String role){
        this(fullName,"","",photoUrl,role);

    }



    public String getLogin() {
        return login;
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

    public float getRate() {
        return rate;
    }
}
