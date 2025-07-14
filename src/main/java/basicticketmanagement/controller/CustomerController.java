package basicticketmanagement.controller;

import basicticketmanagement.dto.CustomerRegistrationDTO;
import basicticketmanagement.model.Customer;
import basicticketmanagement.model.UserRole;
import basicticketmanagement.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRegistrationDTO registrationDTO) {
        /*if (customer.getId() != null) {
            return ResponseEntity.badRequest().build();
        }*/
        if (registrationDTO.getUsername() == null || registrationDTO.getPassword() == null || registrationDTO.getUsername().trim().isEmpty() || registrationDTO.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Basic validation
        }
        /*return ResponseEntity.ok(customerService.createCustomer(customer));*/
        Customer customer = new Customer();
        customer.setUsername(registrationDTO.getUsername());
        customer.setPassword(registrationDTO.getPassword()); // Password will be encoded in service
        customer.setRole(UserRole.CUSTOMER); // ***FIX: Set role here or ensure service sets it***
        return ResponseEntity.ok(customerService.createCustomer(customer)); // Service now expects Customer
    }


    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById((id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
