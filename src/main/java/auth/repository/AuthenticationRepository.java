package auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auth.model.User;

@Repository
public interface AuthenticationRepository extends JpaRepository<User, Integer> {

	public Optional<User> findUserByUsername(String username);
}
