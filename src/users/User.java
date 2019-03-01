package users;

public class User{
    private String name;
    private String password;
    private boolean isLoggedIn;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    @Override
    public String toString() {
        return "users.User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

