package auth.service;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import auth.annotations.PassToken;
import auth.annotations.UserLoginToken;
import auth.configuration.PropertyReader;
import auth.model.User;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Autowired
	AuthenticationService authenticationService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String token = "";
		String userId = "";
		if (request.getHeader("Authorization") != null) {
			String[] authorization = request.getHeader("Authorization").split(" ");
			userId = request.getHeader("userId");
			token = authorization[1];
		}
		// Verify we're working on a function with the Handler, not on a class
		if (!(object instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) object;
		Method method = handlerMethod.getMethod();

		if (method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if (passToken.required()) {
				return true;
			}
		}

		// Check for comments that require user rights
		if (method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
			if (userLoginToken.required()) {
				// Executive certification
				if (token == null) {
					throw new RuntimeException("nothing token，Please login again");
				}

				String secret = PropertyReader.getInstance().getProperty("secret");
				try {
					// Algorithm to encode / decode the token
					Algorithm algorithm = Algorithm.HMAC256(secret);
					// Verifier to configure the decoding of encrypted token.
					JWTVerifier verifier = JWT.require(algorithm).withIssuer(userId).build();
					// Get decoded jwt token
					DecodedJWT jwt = verifier.verify(token);

					LocalDate now = convertToLocalDateViaInstant(new Date());
					LocalDate tokenDate = convertToLocalDateViaInstant(jwt.getExpiresAt());
					if (now.isAfter(tokenDate))
						throw new RuntimeException("Token has expired ! ");

				} catch (JWTVerificationException exception) {
					throw new RuntimeException(exception);
				}
				Optional<User> user = authenticationService.findUserById(userId);

				if (!user.isPresent()) {
					throw new RuntimeException("User does not exist, please login again");
				}

				return true;

			}
		}
		return true;
	}

	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
			throws Exception {

	}

}
