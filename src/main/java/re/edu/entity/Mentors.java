package re.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Mentors")
public class Mentors {
    @Id
    private Long mentorId;

    private String department;
    private String academicRank;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mentorId")
    private Users user;
}

