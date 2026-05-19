package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "AssessmentResults",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assignmentId", "roundId", "criterionId"}))
public class AssessmentResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "assignmentId", nullable = false)
    private InternshipAssignments assignment;

    @ManyToOne
    @JoinColumn(name = "roundId", nullable = false)
    private AssessmentRounds round;

    @ManyToOne
    @JoinColumn(name = "criterionId", nullable = false)
    private EvaluationCriteria criterion;

    @Column(nullable = false)
    private Double score;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "evaluatedBy", nullable = false)
    private Users evaluatedBy;

    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
