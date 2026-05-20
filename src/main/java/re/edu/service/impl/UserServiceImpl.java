package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.dto.request.CreateUserRequest;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.UpdatePasswordRequest;
import re.edu.dto.request.UpdateUserRequest;
import re.edu.dto.response.UserResponse;
import re.edu.util.enums.Role;
import re.edu.entity.Users;
import re.edu.repository.UserRepository;
import re.edu.config.custom.CustomUserDetails;
import re.edu.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(Role role) {
        // Lấy tất cả user theo role (nếu có), ngược lại lấy tất cả
        List<Users> users = (role != null)
                ? userRepository.findAllByRole(role)
                : userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(Integer userId) {
        Users user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(Integer userId, UpdateUserRequest req) {
        Users user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Kiểm tra email trùng
        if (!user.getEmail().equals(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserPassword(Integer userId, UpdatePasswordRequest req) {
        Users targetUser = getTargetUser(userId);
        targetUser.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(targetUser);
        return "Cập nhật mật khẩu thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserStatus(Integer userId, Boolean status) {
        Users targetUser = getTargetUser(userId);
        targetUser.setIsActive(status);
        userRepository.save(targetUser);
        return status ? "Mở khóa tài khoản thành công" : "Khóa tài khoản thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(Integer userId, Role role) {
        Users targetUser = getTargetUser(userId);
        targetUser.setRole(role);
        userRepository.save(targetUser);
        return "Cập nhật role thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(Integer userId) {
        Users targetUser = getTargetUser(userId);
        userRepository.delete(targetUser);
        return "Xóa người dùng thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(CreateUserRequest req) {
        // Kiểm tra trùng username/email
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new DuplicateResourceException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        // Tạo mới user
        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());
        user.setIsActive(true);

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    /**
     * Helper method: lấy user mục tiêu và kiểm tra quyền
     */
    private Users getTargetUser(Integer userId) {
        Users targetUser = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        Users actor = userRepository.findUserByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ADMIN"));

        // ADMIN không được thao tác ADMIN khác
        if (actor.getRole() == Role.ADMIN
                && targetUser.getRole() == Role.ADMIN
                && !actor.getId().equals(targetUser.getId())) {
            throw new AccessDeniedExceptionCustom("ADMIN không thể thao tác ADMIN khác");
        }

        return targetUser;
    }
}
