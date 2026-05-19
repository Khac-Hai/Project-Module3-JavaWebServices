package re.edu.repository;

import re.edu.entity.RoundCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoundCriteriaRepository extends JpaRepository<RoundCriteria, Integer> {
    List<RoundCriteria> findByRound_Id(Integer roundId);
    Optional<RoundCriteria> findByRound_IdAndCriterion_CriterionId(Integer roundId, Integer criterionId);
}
