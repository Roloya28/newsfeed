package com.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String name;

    private Long followersCount = 0L;
    private Long followingsCount = 0L;
    private LocalDateTime deletedAt;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.followersCount = 0L;
        this.followingsCount = 0L;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
