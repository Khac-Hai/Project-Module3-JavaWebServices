package re.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResultResponse {

    private Integer id;
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private Integer roundId;
    private String roundName;
    private Long criterionId;
    private String criterionName;
    private String criterionDescription;
    private BigDecimal maxScore;
    private Double score;
    private String comments;
    private Long evaluatedById;
    private String evaluatedByName;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}