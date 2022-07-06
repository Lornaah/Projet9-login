package auth.service;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import auth.dto.UserDTO;
import auth.model.User;
import auth.repository.AuthenticationRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	AuthenticationRepository authRepository;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public UserDTO createUser(@Valid UserDTO userDTO) {
		User user = new User();
		user.update(userDTO);
		user.setPassword(encoder.encode(user.getPassword()));
		authRepository.save(user);
		userDTO.setId(user.getId());

		return userDTO;
	}

	@Override
	public User login(UserDTO userDTO) {
		Optional<User> user = authRepository.findUserByUsername(userDTO.getUsername());
		if (!user.isPresent()) {
			throw new IllegalStateException("User not found");
		}
		if (!matches(userDTO.getPassword(), user.get().getPassword())) {
			throw new IllegalStateException("Wrong Password");
		}
		return user.get();
	}

	private boolean matches(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}

	@Override
	public Optional<User> findUserById(String userId) {
		return authRepository.findById(Integer.valueOf(userId));
	}

	@Override
	public String getToken(User user) {
		String token = "";
		token = JWT.create().withIssuer(String.valueOf(user.getId()))
				.withExpiresAt(new Date(System.currentTimeMillis() + (long) 3600 * 1000))
				.sign(Algorithm.HMAC256(user.getPassword()));
		return token;
	}

	@Override
	public Optional<User> findByUsername(@Valid String username) {
		return authRepository.findUserByUsername(username);
	}

}
