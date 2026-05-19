package re.edu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import re.edu.dto.request.auth.LoginRequest;
import re.edu.dto.response.LoginResponse;
import re.edu.entity.Users;
import re.edu.repository.UserRepository;
import re.edu.config.jwt.JwtService;


@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Users user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateAccessToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name(), user.getUsername()));
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        // Lấy token từ header
        String token = authHeader.replace("Bearer ", "");

        // Validate token
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Lấy username từ token
        String username = jwtService.getUsernameFromToken(token);

        // Tìm user trong DB
        Users user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Trả về thông tin user
        return ResponseEntity.ok(new LoginResponse(null, user.getRole().name(), user.getUsername()));
    }

}
