package re.edu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundCriteriaResponse {
    private Integer roundCriterionId;
    private Integer roundId;
    private String roundName;
    private Integer criterionId;
    private String criterionName;
    private BigDecimal weight;
}
