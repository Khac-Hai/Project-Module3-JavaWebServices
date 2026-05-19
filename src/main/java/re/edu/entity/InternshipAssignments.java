package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import re.edu.util.enums.Status;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "InternshipAssignments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "phaseId"}))
public class InternshipAssignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "mentorId", nullable = false)
    private Mentors mentor;

    @ManyToOne
    @JoinColumn(name = "phaseId", nullable = false)
    private InternshipPhases phase;

    private LocalDateTime assignedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

