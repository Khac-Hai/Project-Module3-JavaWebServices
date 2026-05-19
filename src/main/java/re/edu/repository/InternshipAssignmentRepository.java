package re.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.util.enums.Status;
import re.edu.entity.InternshipAssignments;

import java.util.List;
import java.util.Optional;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignments, Integer> {

    List<InternshipAssignments> findByMentor_Id(Integer mentorId);
    List<InternshipAssignments> findByStudent_Id(Integer studentId);
    List<InternshipAssignments> findByPhase_PhaseId(Integer phaseId);
    List<InternshipAssignments> findByStatus(Status status);
    Optional<InternshipAssignments> findByStudent_IdAndPhase_PhaseId(Integer studentId, Integer phaseId);
}
