package com.mifinity.card.api.security.jwt;

import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.enums.ProfileEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {
    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getProfile())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
        return   authorities ;
    }
}
