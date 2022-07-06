package auth.dto;

import java.util.Objects;

public class UserDTO {

	private int id;
	private String username;
	private String password;

	public UserDTO() {
	}

	public UserDTO(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public UserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return id == other.id && Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

}
