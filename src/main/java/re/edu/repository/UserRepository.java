package re.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import re.edu.util.enums.Role;
import re.edu.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findUserByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Users> findByRole(Role role);
    Optional<Users> getUserById(@Param("id") Integer id);
    List<Users> findAllByRole(Role role);
}
