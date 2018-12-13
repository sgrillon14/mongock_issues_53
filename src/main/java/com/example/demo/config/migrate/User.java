package com.example.demo.config.migrate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "email")
@Builder(toBuilder = true)
public class User implements UserDetails, CredentialsContainer {

    @Transient
    @JsonIgnore
    private static final String ROLE_PREFIX = "ROLE_";

    @Id
    private String identifier;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String firstName;

    private String lastName;

    private String company;

    private String lang;

    private Set<String> roles = new HashSet<>();

    @Transient
    @JsonIgnore
    private Set<GrantedAuthority> authorities = new HashSet<>();

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @CreatedDate
    private ZonedDateTime creationDate;

    @LastModifiedDate
    private ZonedDateTime modificationDate;

    public User(final String email, final String password, final String firstName, final String lastName, final String lang) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lang = lang;
        this.roles = new HashSet<>();
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public void syncRolesToAuthorities() {
        final Set<GrantedAuthority> authFromRoles = roles.parallelStream()
                .map(String::toUpperCase)
                .map(s -> ROLE_PREFIX + s)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        authorities.clear();
        authorities.addAll(authFromRoles);
    }
}

