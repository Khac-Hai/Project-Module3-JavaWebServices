package re.edu.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriteriaRequest {
    private Integer roundId;
    private Integer criterionId;
    private BigDecimal weight;
}
