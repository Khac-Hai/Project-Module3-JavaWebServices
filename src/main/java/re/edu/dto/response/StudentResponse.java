package re.edu.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Integer id;
    private String studentCode;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
    private Integer userId;
    private String username;
}
