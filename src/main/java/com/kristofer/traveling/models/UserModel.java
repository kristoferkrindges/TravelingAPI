package com.kristofer.traveling.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kristofer.traveling.models.Enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_table")
public class UserModel implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String at;
    private String password;
    private String photo;
    private String banner;
    private Date birthdate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<PostModel> posts = new ArrayList<>();
    @OneToMany(mappedBy = "follower")
    private List<FollowerModel> followers = new ArrayList<>();
    @OneToMany(mappedBy = "following")
    private List<FollowingModel> following = new ArrayList<>();
    @OneToMany(mappedBy = "creator")
    private List<StorieModel> stories = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
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
