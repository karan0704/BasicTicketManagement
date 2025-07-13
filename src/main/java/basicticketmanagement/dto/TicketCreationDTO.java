package basicticketmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/*
 * Data Transfer Object (DTO) for creating a new Ticket.
 * This class defines the minimal set of information required from the client
 * to create a ticket. The customerId will now be derived from the authenticated user.
 * It still includes an optional engineerId for immediate assignment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreationDTO {
    private String description;
    private Long customerId;
    private Long engineerId;
}