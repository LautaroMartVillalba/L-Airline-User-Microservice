package ar.com.l_airline.security;

import ar.com.l_airline.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsCustom implements org.springframework.security.core.userdetails.UserDetails {

    private String username;
    private String password;

    public UserDetailsCustom(User user) {
        this.username = user.getName();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
