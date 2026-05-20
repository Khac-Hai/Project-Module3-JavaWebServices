package re.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re.edu.util.enums.Role;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private final String type = "Bearer";
    private String username;
    private Role role;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private Date expirationDate;
}