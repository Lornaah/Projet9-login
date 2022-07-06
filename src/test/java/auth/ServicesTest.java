package auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import auth.dto.UserDTO;
import auth.model.User;
import auth.repository.AuthenticationRepository;
import auth.service.AuthenticationService;

@SpringBootTest
public class ServicesTest {

	@Autowired
	AuthenticationService service;

	@Autowired
	AuthenticationRepository repo;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	ClearDB clearDB;

	@BeforeEach
	public void clearDB() {
		clearDB.clearDB();
	}

	@Test
	public void createUser() {
		UserDTO userDTO = new UserDTO("username", "password");

		service.createUser(userDTO);

		assertTrue(repo.findUserByUsername(userDTO.getUsername()).isPresent());
	}

	@Test
	public void login() {
		UserDTO userDTO = new UserDTO("username", "password");
		service.createUser(userDTO);

		User userLogged = service.login(userDTO);

		assertNotNull(userLogged);
		assertTrue(encoder.matches(userDTO.getPassword(), userLogged.getPassword()));
	}

}
