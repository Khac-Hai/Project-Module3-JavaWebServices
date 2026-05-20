package re.edu.service;

import re.edu.dto.request.UpdatePasswordRequest;
import re.edu.dto.request.CreateUserRequest;
import re.edu.dto.request.UpdateUserRequest;
import re.edu.dto.response.UserResponse;
import re.edu.util.enums.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(Role role);
    UserResponse getUserById(Integer userId);
    UserResponse updateUser(Integer userId, UpdateUserRequest req);
    String updateUserPassword(Integer userId, UpdatePasswordRequest req);
    String updateUserStatus(Integer userId, Boolean status);
    String updateUserRole(Integer userId, Role role);
    String deleteUser(Integer userId);
    UserResponse createUser(CreateUserRequest req);
}