package basicticketmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for registering a new Customer.
 * This DTO is used when an Engineer creates a new Customer account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationDTO {
    private String username;
    private String password;
}
