package basicticketmanagement.service;

import basicticketmanagement.model.Customer;
import basicticketmanagement.model.Engineer;
import basicticketmanagement.model.Ticket;
import basicticketmanagement.model.TicketStatus;
import basicticketmanagement.repository.CustomerRepository;
import basicticketmanagement.repository.EngineerRepository;
import basicticketmanagement.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Ticket entities.
 * This class encapsulates the business logic for creating, acknowledging,
 * updating, retrieving, and deleting tickets.
 */
@Service
@RequiredArgsConstructor // Using Lombok's RequiredArgsConstructor for constructor injection
public class TicketService {

    private final TicketRepository ticketRepo;
    private final CustomerRepository customerRepo;
    private final EngineerRepository engineerRepo;

    /**
     * Retrieves a Customer by their username.
     * This method is used by the TicketController to get the authenticated
     * customer's ID for ticket creation.
     *
     * @param username The username of the customer to retrieve.
     * @return The Customer object if found.
     * @throws EntityNotFoundException if no customer with the given username is found.
     */
    public Customer getCustomerByUsername(String username) {
        return customerRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with username: " + username));
    }

    /**
     * Creates a new ticket.
     * The customer creating the ticket is identified by their ID.
     *
     * @param customerId  The ID of the customer creating the ticket.
     * @param description The description of the ticket.
     * @param engineerId  An optional ID of the engineer to immediately acknowledge the ticket.
     * @return The newly created Ticket object.
     * @throws EntityNotFoundException if the customer or specified engineer is not found.
     */
    public Ticket createTicket(Long customerId, String description, Long engineerId) {
        // Find the customer by ID, throwing an exception if not found
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + customerId));

        // Create a new Ticket instance
        Ticket ticket = new Ticket();
        ticket.setCreatedBy(customer); // Set the customer who created the ticket
        ticket.setDescription(description); // Set the ticket description

        // If an engineer ID is provided, assign the engineer and set status to ACKNOWLEDGED
        if (engineerId != null) {
            Engineer engineer = engineerRepo.findById(engineerId)
                    .orElseThrow(() -> new EntityNotFoundException("Engineer not found with id " + engineerId));
            ticket.setAcknowledgedBy(engineer);
            ticket.setStatus(TicketStatus.ACKNOWLEDGED); // Set status to ACKNOWLEDGED if assigned
        } else {
            // If no engineer is assigned, set status to CREATED
            ticket.setStatus(TicketStatus.CREATED);
        }

        // Save the new ticket to the repository
        return ticketRepo.save(ticket);
    }

    /**
     * Acknowledges a ticket by assigning an engineer and updating its status.
     *
     * @param ticketId   The ID of the ticket to acknowledge.
     * @param engineerId The ID of the engineer acknowledging the ticket.
     * @return The updated Ticket object.
     * @throws EntityNotFoundException if the ticket or engineer is not found.
     */
    public Ticket acknowledgeTicket(Long ticketId, Long engineerId) {
        // Find the ticket by ID, throwing an exception if not found
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id " + ticketId));

        // Find the engineer by ID, throwing an exception if not found
        Engineer engineer = engineerRepo.findById(engineerId)
                .orElseThrow(() -> new EntityNotFoundException("Engineer not found with id " + engineerId));

        // Update the ticket status and assigned engineer
        ticket.setStatus(TicketStatus.ACKNOWLEDGED);
        ticket.setAcknowledgedBy(engineer);

        // Save the updated ticket
        return ticketRepo.save(ticket);
    }

    /**
     * Retrieves all tickets.
     *
     * @return A list of all Ticket objects.
     */
    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    /**
     * Retrieves a ticket by its ID.
     *
     * @param id The ID of the ticket to retrieve.
     * @return An Optional containing the Ticket if found, or empty if not.
     */
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepo.findById(id);
    }

    /**
     * Updates an existing ticket.
     *
     * @param id            The ID of the ticket to update.
     * @param ticketDetails The Ticket object containing updated details.
     * @return The updated Ticket object.
     * @throws EntityNotFoundException if no ticket with the given ID is found.
     */
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        return ticketRepo.findById(id)
                .map(ticket -> {
                    // Update only the description and status from the provided details
                    ticket.setDescription(ticketDetails.getDescription());
                    ticket.setStatus(ticketDetails.getStatus());
                    // You might want to allow updating acknowledgedBy as well,
                    // depending on your business rules. For now, it's commented out.
                    // ticket.setAcknowledgedBy(ticketDetails.getAcknowledgedBy());
                    return ticketRepo.save(ticket); // Save the updated ticket
                })
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id " + id));
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param id The ID of the ticket to delete.
     * @throws EntityNotFoundException if no ticket with the given ID is found.
     */
    public void deleteTicket(Long id) {
        // Check if the ticket exists before attempting to delete
        if (!ticketRepo.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found with id " + id);
        }
        ticketRepo.deleteById(id); // Delete the ticket
    }
}
