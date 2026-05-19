package re.edu.service;

import re.edu.dto.request.auth.LoginRequest;
import re.edu.dto.response.LoginResponse;
import re.edu.dto.response.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserResponse getMe(String username);
}