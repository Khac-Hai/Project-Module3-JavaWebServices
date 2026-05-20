package re.edu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorResponse {
    private Integer id;
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String academicRank;
}