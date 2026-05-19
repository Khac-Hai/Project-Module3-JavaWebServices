package re.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDetailResponse {
    private Integer id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private boolean isActive;
    private String phoneNumber;
}
