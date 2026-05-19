package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EvaluationCriteria")
public class EvaluationCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criterionId;

    @Column(nullable = false, unique = true)
    private String criterionName;

    private String description;

    @Column(nullable = false)
    private BigDecimal maxScore;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
