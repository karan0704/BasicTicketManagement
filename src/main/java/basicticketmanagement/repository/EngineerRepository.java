package basicticketmanagement.repository;

import basicticketmanagement.model.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EngineerRepository extends JpaRepository<Engineer, Long> {
    Optional<Engineer> findByUsername(String username); // New method for finding engineer by username
}
