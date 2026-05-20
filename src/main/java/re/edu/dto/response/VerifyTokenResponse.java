package re.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re.edu.util.enums.Role;

@Getter
@Setter
@Builder
public class VerifyTokenResponse {
    private Boolean valid;
    private String username;
    private Role role;
}
