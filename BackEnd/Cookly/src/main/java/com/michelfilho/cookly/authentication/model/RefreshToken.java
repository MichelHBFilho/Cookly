package com.michelfilho.cookly.authentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true)
    private String token;

    private Instant expiryDate;

    public RefreshToken(
            User user,
            String token,
            Instant expiryDate
    ) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
