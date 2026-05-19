package re.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.entity.Students;

public interface StudentRepository extends JpaRepository<Students, Integer> {

}