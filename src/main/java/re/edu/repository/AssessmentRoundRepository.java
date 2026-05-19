package re.edu.repository;

import re.edu.entity.AssessmentRounds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRoundRepository
        extends JpaRepository<AssessmentRounds, Integer> {

    List<AssessmentRounds> findByPhase_PhaseId(Integer phaseId);

    List<AssessmentRounds> findByIsActive(Boolean isActive);
}
