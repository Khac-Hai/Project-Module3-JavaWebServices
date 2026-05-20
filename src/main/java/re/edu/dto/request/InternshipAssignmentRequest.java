package re.edu.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipAssignmentRequest {
    private Integer studentId;
    private Integer mentorId;
    private Integer phaseId;
}
