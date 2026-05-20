package re.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class EvaluationCriteriaResponse {
    private Integer id;
    private String criterionName;
    private String description;
    private BigDecimal maxScore;
}
