package tech.vuthehuyht.blogrestapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String status;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @Column(insertable = false)
    private LocalDateTime updated_at;

    @Column(name = "login_attempt")
    private int loginAttempt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private Collection<Role> roles;
}
