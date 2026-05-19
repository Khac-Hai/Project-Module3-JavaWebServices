package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import re.edu.util.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "internship_assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "phase_id"})
        }
)
@Getter
@Setter
public class InternshipAssignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentors mentor;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private InternshipPhases phase;

    private LocalDateTime assignedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "assignment")
    private List<AssessmentResults> assessmentResults;
}