package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "AssessmentRounds")
public class AssessmentRounds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @ManyToOne
    @JoinColumn(name = "phaseId", nullable = false)
    private InternshipPhases phase;

    private String roundName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

