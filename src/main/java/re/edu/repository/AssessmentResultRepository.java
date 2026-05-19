package re.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.entity.AssessmentResults;

import java.util.List;
import java.util.Optional;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResults, Integer> {
    List<AssessmentResults> findByAssignment_AssignmentId(Integer assignmentId);
    List<AssessmentResults> findByRound_Id(Integer roundId);
    List<AssessmentResults> findByCriterion_CriterionId(Integer criterionId);
    Optional<AssessmentResults> findByAssignment_AssignmentIdAndRound_IdAndCriterion_CriterionId(Integer assignmentId, Integer roundId, Integer criterionId);
}
