package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.config.jwt.JwtService;
import re.edu.dto.request.LoginRequest;
import re.edu.dto.response.LoginResponse;
import re.edu.dto.response.UserResponse;
import re.edu.entity.Users;
import re.edu.exception.ResourceNotFoundException;
import re.edu.repository.UserRepository;
import re.edu.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtService.generateAccessToken(request.getUsername());
        Users user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name()) // giả sử role là Enum, lấy tên
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserResponse getMe(String username) {
        Users user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        return userMapper.toResponse(user);
    }
}
