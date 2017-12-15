package main.java.client_agent.abstraction;

public class UserModel {

    private String nickname;
    private String password;
    private String resourceHubAddress;
    private String userID;

    public UserModel() {
        nickname = "";
        password = "";
        resourceHubAddress = "";
        userID = "";
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getResourceHubAddress() {
        return resourceHubAddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        nickname = userID.split("@")[0];
        resourceHubAddress = userID.split("@")[1];
    }
}
