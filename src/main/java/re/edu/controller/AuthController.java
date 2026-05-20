package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.edu.dto.request.LoginRequest;
import re.edu.dto.request.VerifyTokenRequest;
import re.edu.dto.response.JwtResponse;
import re.edu.dto.response.VerifyTokenResponse;
import re.edu.dto.response.UserResponse;
import re.edu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest req
    ) {
        return ResponseEntity.ok(
                authService.login(req)
        );
    }

    /**
     * VERIFY TOKEN
     */
    @PostMapping("/verify")
    public ResponseEntity<VerifyTokenResponse> verify(
            @RequestBody VerifyTokenRequest req
    ) {

        return ResponseEntity.ok(
                authService.verifyToken(req)
        );
    }

    /**
     * CURRENT USER
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {

        return ResponseEntity.ok(
                authService.getCurrentUser()
        );
    }
}
