package re.edu.dto.request.evaluation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriteriaRequest {
    private String criterionName;
    private String description;
    private BigDecimal maxScore;
}
