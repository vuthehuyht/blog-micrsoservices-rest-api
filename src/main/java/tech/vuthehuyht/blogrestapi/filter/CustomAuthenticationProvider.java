package tech.vuthehuyht.blogrestapi.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.vuthehuyht.blogrestapi.exceptions.CustomMessageException;
import tech.vuthehuyht.blogrestapi.models.Role;
import tech.vuthehuyht.blogrestapi.models.User;
import tech.vuthehuyht.blogrestapi.repositories.UserRepository;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Incoming authentication {}", authentication);

        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            log.error("Username {} not found", username);
            throw new CustomMessageException("Username not found", String.valueOf(HttpStatus.NOT_FOUND.value()));
        }

        List<GrantedAuthority> grantedAuthorities = grantedAuthorities(user.get().getRoles().stream().toList());
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);

        log.info("Outcome authentication {}", auth);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

    private List<GrantedAuthority> grantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<String> permission = new HashSet<>();

        if(!roles.isEmpty()) {
            roles.forEach(role -> permission.add(role.getName()));
        }
        permission.forEach(item -> grantedAuthorities.add(new SimpleGrantedAuthority(item)));
        return grantedAuthorities;
    }
}
