package re.edu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AssessmentRoundResponse {
    private Integer id;
    private Integer phaseId;
    private String phaseName;
    private String roundName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean isActive;
}
