package org.masonord.delivery.security;

import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.repository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = UserMapper.toUserDto(userDao.findUserByEmail(email));

        if (userDto != null) {
            Set<UserRoles> userRoles = new HashSet<>();

            userRoles.add(UserRoles.valueOf(userDto.getRole()));

            List<GrantedAuthority> authorities = getUserAuthority(userRoles);

            return buildUserForAuthentication(userDto, authorities);
        }

        throw new UsernameNotFoundException("user with email " + email + " does not exist");
    }

    private List<GrantedAuthority> getUserAuthority(Set<UserRoles> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();

        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getValue()));
        });

        return new ArrayList<GrantedAuthority>(roles);
    }

    private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
