package id.web.fahmikudo.meeting.mom.security;

import id.web.fahmikudo.meeting.mom.model.User;
import id.web.fahmikudo.meeting.mom.security.model.JWTUser;
import id.web.fahmikudo.meeting.mom.security.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class JWTUserFactory {
    private JWTUserFactory() {
    }

    public static JWTUser create(User user) {
        return new JWTUser(
                user.getUsername(), user.getPassword(), mapToGrantedAuthorities(user.getRoles().), user.getEnabled()
        );
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toList());
    }
}
