package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "RoundCriteria",
        uniqueConstraints = @UniqueConstraint(columnNames = {"roundId", "criterionId"}))
public class RoundCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundCriterionId;

    @ManyToOne
    @JoinColumn(name = "roundId", nullable = false)
    private AssessmentRounds round;

    @ManyToOne
    @JoinColumn(name = "criterionId", nullable = false)
    private EvaluationCriteria criterion;

    @Column(nullable = false)
    private Double weight;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

