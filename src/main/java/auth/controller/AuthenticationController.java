package auth.controller;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import auth.annotations.UserLoginToken;
import auth.dto.UserDTO;
import auth.model.User;
import auth.service.AuthenticationService;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;

	@CrossOrigin
	@PostMapping("user/validate")
	public void validate(@Valid @RequestBody UserDTO user, BindingResult result) {
		if (!result.hasErrors()) {
			authService.createUser(user);
		}
	}

	@CrossOrigin
	@PostMapping("/login")
	public String login(@Valid @RequestBody UserDTO user, BindingResult result) {
		JSONObject jsonObject = new JSONObject();

		if (!result.hasErrors()) {
			try {
				User userLogged = authService.login(user);
				String token = authService.getToken(userLogged);
				jsonObject.put("token", token);
				jsonObject.put("userId", userLogged.getId());
				jsonObject.put("userName", userLogged.getUsername());

			} catch (IllegalStateException e) {
				jsonObject.put("message", e.getMessage());
				return jsonObject.toString();
			}
		}
		return jsonObject.toString();
	}

	@UserLoginToken
	@CrossOrigin
	@GetMapping("/verifyToken")
	public void verifyToken(String tokenToVerify) {

	}

}
