//package re.edu.controller;
//
//import re.edu.dto.request.auth.CreateUserRequest;
//import re.edu.dto.response.ApiResponse;
//import re.edu.dto.response.UserDetailResponse;
//import re.edu.dto.response.UserResponse;
////import re.edu.service.UserService;
//import re.edu.repository.UserRepository;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import jakarta.validation.Valid;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PatchMapping;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // Lấy tất cả user (chỉ ADMIN mới được phép)
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers() {
//        List<UserResponse> users = userRepository.findAll().stream()
//                .map(u -> new UserResponse(
//                        u.getUserId(),
//                        u.getUsername(),
//                        u.getEmail(),
//                        u.getFullName(),
//                        u.getRole().name(),
//                        u.getIsActive(),
//                        u.getPhoneNumber()
//                ))
//                .toList();
//
//        return ResponseEntity.ok(users);
//    }
//    // Lấy user theo id
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<UserDetailResponse>> getById(@PathVariable Integer id) {
//        UserDetailResponse userDetailResponse = userService.getById(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<UserDetailResponse>builder()
//                .success(true)
//                .message("Lấy thông tin người dùng thành công!")
//                .data(userDetailResponse)
//                .build());
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<UserDetailResponse>> create(@Valid @RequestBody CreateUserRequest request) {
//        UserDetailResponse userDetailResponse = userService.create(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<UserDetailResponse>builder()
//                .success(true)
//                .message("Thêm người dùng thành công!")
//                .data(userDetailResponse)
//                .build());
//    }
//
//    @PatchMapping("{id}")
//    public ResponseEntity<ApiResponse<UserDetailResponse>> update(@PathVariable int id, @RequestBody UpdateUserRequest request) {
//        UserDetailResponse userDetailResponse = userService.update(id, request);
//
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<UserDetailResponse>builder()
//                .success(true)
//                .message("Cập nhật thông tin người dùng thành công!")
//                .data(userDetailResponse)
//                .build());
//    }
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUserStatus(@PathVariable int id, @RequestBody boolean active) {
//        UserDetailResponse userDetailResponse = userService.updateStatus(id, active);
//
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<UserDetailResponse>builder()
//                .success(true)
//                .message("Cập nhật trạng thái người dùng thành công!")
//                .data(userDetailResponse)
//                .build());
//    }
//
//    @PatchMapping("/{id}/role")
//    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUserRole(@PathVariable int id, @RequestBody String role) {
//        UserDetailResponse userDetailResponse = userService.updateRole(id, role);
//
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<UserDetailResponse>builder()
//                .success(true)
//                .message("Cập nhật vai trò người dùng thành công!")
//                .data(userDetailResponse)
//                .build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id) {
//        userService.delete(id);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.<String>builder()
//                .success(true)
//                .message("Xóa người dùng thành công!")
//                .data("Người dùng có id " + id + " đã bị xóa.")
//                .build());
//    }
//}