package re.edu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AssessmentResultResponse {
    private Integer resultId;
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private Integer mentorId;
    private String mentorName;
    private Integer roundId;
    private String roundName;
    private Integer criterionId;
    private String criterionName;
    private BigDecimal score;
    private String comments;
    private Integer evaluatedById;
    private String evaluatedByName;
    private LocalDateTime evaluationDate;
}
