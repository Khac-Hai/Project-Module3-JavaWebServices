package re.edu.repository;

import re.edu.entity.Mentors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentors, Integer> {
}
