package auth.service;

import java.util.Optional;

import javax.validation.Valid;

import auth.dto.UserDTO;
import auth.model.User;

public interface AuthenticationService {

	UserDTO createUser(@Valid UserDTO userDTO);

	User login(UserDTO userDTO);

	Optional<User> findUserById(String userId);

	String getToken(User user);

	Optional<User> findByUsername(@Valid String username);

}
