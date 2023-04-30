package login;

public class login {
	
	private String user_id;
	private String user_password;

	public login() {}
	
	public login(String UserID, String UserPassword) {
		user_id = UserID;
		user_password = UserPassword;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	@Override
	public String toString() {
		return "login [user_id=" + user_id + ", user_password=" + user_password 
				+ "]";
	}
	
}
