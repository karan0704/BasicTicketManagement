package basicticketmanagement.service;

// src\main\java\basicticketmanagement\service\CustomerService.java (New File)

import basicticketmanagement.model.Customer;
import basicticketmanagement.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setUsername(customerDetails.getUsername());
                    customer.setPassword(customerDetails.getPassword());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Customer not found with id " + id));
    }
}
