package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "InternshipPhases")
public class InternshipPhases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phaseId;

    @Column(nullable = false, unique = true)
    private String phaseName;

    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
