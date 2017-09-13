package main.java.client_agent.abstraction;

public class UserModel {

	private String nickname;
	private String password;

	public UserModel() {
		super();
		nickname = "";
		password = "";
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
