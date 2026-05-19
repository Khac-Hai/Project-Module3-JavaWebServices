package re.edu.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponse {
    private Integer id;
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String academicRank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
