package basicticketmanagement;

import basicticketmanagement.model.Engineer;
import basicticketmanagement.model.UserRole;
import basicticketmanagement.repository.EngineerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder

@SpringBootApplication
public class BasicTicketManagementApplication {
    @Value("${default.engineer.username}") // Inject from properties
    private String defaultEngineerUsername;

    @Value("${default.engineer.password}") // Inject from properties
    private String defaultEngineerPassword;

    public static void main(String[] args) {
        SpringApplication.run(BasicTicketManagementApplication.class, args);
    }

    /**
     * CommandLineRunner to create a default engineer on application startup if one doesn't exist.
     * This provides an "inbuilt" engineer account for initial setup and testing.
     *
     * @param engineerRepository The repository for Engineer entities.
     * @param passwordEncoder The password encoder for hashing the default password.
     * @return A CommandLineRunner bean.
     */
    @Bean
    public CommandLineRunner createDefaultEngineer(EngineerRepository engineerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            //final String defaultEngineerUsername = "default_engineer";
            //final String defaultEngineerPassword = "password"; // This will be encoded


            // Check if the default engineer already exists
            if (engineerRepository.findByUsername(defaultEngineerUsername).isEmpty()) {
                Engineer defaultEngineer = new Engineer();
                defaultEngineer.setUsername(defaultEngineerUsername);
                defaultEngineer.setPassword(passwordEncoder.encode(defaultEngineerPassword)); // Encode the password
                defaultEngineer.setRole(UserRole.ENGINEER); // Set the role
                engineerRepository.save(defaultEngineer);
                System.out.println("Default engineer '" + defaultEngineerUsername + "' created successfully!");
            } else {
                System.out.println("Default engineer '" + defaultEngineerUsername + "' already exists.");
            }
        };
    }
}
