package re.edu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPhaseResponse {
    private Integer id;
    private String phaseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}

