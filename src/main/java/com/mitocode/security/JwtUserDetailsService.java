package com.mitocode.security;

import com.mitocode.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service //S2
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = repo.findOneByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("User not exists" +username);

        List<GrantedAuthority> roles = new ArrayList<>();
        user.getRoles().forEach(rol -> roles.add(new SimpleGrantedAuthority(rol.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }
}
