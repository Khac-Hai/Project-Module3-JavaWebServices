package re.edu.config.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import re.edu.entity.Users;

@Builder
public class CustomUserDetails implements UserDetails{
    private Users user;
    private Collection<? extends GrantedAuthority> auth;

    public Users getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auth;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public boolean isActive() {
        return user.getIsActive();
    }
}
