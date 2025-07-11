package basicticketmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Entity
@Table(name = "engineers")
public class Engineer extends User {
    public Engineer(Long id, String username, String password) {
        super(id, username, password);
    }

}
