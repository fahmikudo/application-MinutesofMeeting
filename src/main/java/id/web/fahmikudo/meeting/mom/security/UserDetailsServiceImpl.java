package id.web.fahmikudo.meeting.mom.security;

import id.web.fahmikudo.meeting.mom.model.User;
import id.web.fahmikudo.meeting.mom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> u = userRepo.findByUsername(username);
        if (u != null) {
            return JWTUserFactory.create(u.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }

}
