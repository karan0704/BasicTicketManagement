package basicticketmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-incrementing IDs
    private Long id;

    @Column(unique = true, nullable = false) // Username must be unique and not null
    private String username;

    @Column(nullable = false) // Password must not be null
    private String password;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false) // Role must not be null
    private UserRole role; // New field for user roles

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
