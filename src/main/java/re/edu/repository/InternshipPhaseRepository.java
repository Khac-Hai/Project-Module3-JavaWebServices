package re.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.entity.InternshipPhases;

@Repository
public interface InternshipPhaseRepository
        extends JpaRepository<InternshipPhases, Integer> {
}
