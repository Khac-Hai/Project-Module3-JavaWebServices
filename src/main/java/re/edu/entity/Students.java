package re.edu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Students {
    @Id
    @Column(name = "student_id")
    private Long studentId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "student_id")
    private Users user;
    @Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String studentCode;
    @Column(length = 100)
    private String major;
    @Column(name = "class", length = 50)
    private String className;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(length = 255)
    private String address;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}