package auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auth.repository.AuthenticationRepository;

@Service
public class ClearDB {

	@Autowired
	AuthenticationRepository authenticationRepository;

	public void clearDB() {
		authenticationRepository.deleteAll();
	}

}
