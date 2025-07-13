package basicticketmanagement.service;

import basicticketmanagement.model.Customer;
import basicticketmanagement.model.Engineer;
import basicticketmanagement.model.User;
import basicticketmanagement.repository.CustomerRepository;
import basicticketmanagement.repository.EngineerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for loading user-specific data during the authentication process.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final EngineerRepository engineerRepository;

    /**
     * Locates the user based on the username.
     * In a real application, you might have a single UserRepository for all User types,
     * or a more sophisticated way to determine the user type.
     * For this example, we check both Customer and Engineer repositories.
     *
     * @param username The username of the user to retrieve.
     * @return A Spring Security UserDetails object.
     * @throws UsernameNotFoundException if the user could not be found or the user has no granted authorities.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find the user as a Customer
        Optional<Customer> customerOptional = customerRepository.findByUsername(username);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (customer.getRole() == null) {
                throw new UsernameNotFoundException("Role is missing for user: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    customer.getUsername(),
                    customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + customer.getRole().name()))
            );
        }

        // If not a Customer, try to find the user as an Engineer
        Optional<Engineer> engineerOptional = engineerRepository.findByUsername(username);
        if (engineerOptional.isPresent()) {
            Engineer engineer = engineerOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    engineer.getUsername(),
                    engineer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + engineer.getRole().name()))
            );
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}