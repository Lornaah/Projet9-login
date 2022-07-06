package auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import auth.dto.UserDTO;
import auth.model.User;
import auth.service.AuthenticationService;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	AuthenticationService service;

	@Autowired
	ClearDB clearDB;

	@BeforeEach
	public void clearDB() {
		clearDB.clearDB();
	}

	@Test
	public void testUserValidate() throws Exception {

		UserDTO dto = new UserDTO("username", "password");

		mockMvc.perform(post("/user/validate").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
		Optional<User> user = service.findByUsername("username");

		assertTrue(user.isPresent());

	}

	@Test
	public void testLogin() throws Exception {
		UserDTO dto = new UserDTO("username", "password");
		service.createUser(dto);

		MvcResult result = mockMvc.perform(
				post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk()).andReturn();

		System.err.println(result.getResponse().getContentAsString());
		UserWithToken response = objectMapper.readValue(result.getResponse().getContentAsString(), UserWithToken.class);

		assertNotNull(response);
		assertTrue(response.userName.equals("username"));
		assertTrue(Integer.valueOf(response.userId) > 0);
		assertTrue(response.token != null);
	}

	/**
	 * Class used to instanciate an object representing the /login response and then
	 * do assertions.
	 */
	private static class UserWithToken {
		private String userName;
		private String userId;
		private String token;

		public UserWithToken() {
		}

		public void setUserName(String username) {
			this.userName = username;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public void setToken(String token) {
			this.token = token;
		}

	}

}
