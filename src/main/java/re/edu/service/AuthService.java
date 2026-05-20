package re.edu.service;

import re.edu.dto.request.UpdatePasswordRequest;
import re.edu.dto.request.CreateUserRequest;
import re.edu.dto.request.LoginRequest;
import re.edu.dto.request.VerifyTokenRequest;
import re.edu.dto.response.JwtResponse;
import re.edu.dto.response.VerifyTokenResponse;
import re.edu.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(CreateUserRequest req);
    JwtResponse login(LoginRequest req);
    VerifyTokenResponse verifyToken(VerifyTokenRequest req);
    UserResponse getCurrentUser();
}