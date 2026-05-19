package re.edu.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import re.edu.entity.Users;
import re.edu.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService{
//        implements UserDetailsService
//    private final UserRepository userRepository;
//
//    @Override
//    public CustomUserDetails loadUserByUsername(String loginInfo) throws UsernameNotFoundException {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        Users user = userRepository.findByUsernameOrEmailOrPhoneNumber(loginInfo, loginInfo, loginInfo)
//                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng!"));
//
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
//
//        return CustomUserDetails.builder()
//                .user(user)
//                .auth(authorities)
//                .build();
//    }
}
