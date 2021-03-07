package com.vancone.monster.code.entity.DTO;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Data
@Entity
public class User implements UserDetails {

    @Id
    private String id;

    private String username;

    private String password;

    private String type;

    private Integer role;

    private Integer status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
