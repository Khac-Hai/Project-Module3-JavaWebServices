package re.edu.dto.response;

import lombok.*;
import re.edu.util.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAssignmentResponse {
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private Integer mentorId;
    private String mentorName;
    private Integer phaseId;
    private String phaseName;
    private LocalDateTime assignedDate;
    private Status status;
}
