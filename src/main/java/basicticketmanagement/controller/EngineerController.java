package basicticketmanagement.controller;

import basicticketmanagement.model.Engineer;
import basicticketmanagement.service.EngineerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engineers")
@RequiredArgsConstructor
public class EngineerController {

    private final EngineerService engineerService;

    @PostMapping
    public ResponseEntity<Engineer> createEngineer(@RequestBody Engineer engineer) {
        Engineer saved = engineerService.createEngineer(engineer);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Engineer>> getAllEngineers() {
        return ResponseEntity.ok(engineerService.getAllEngineers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Engineer> getEngineerById(@PathVariable Long id) {
        return engineerService.getEngineerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Engineer> updateEngineer(@PathVariable Long id, @RequestBody Engineer input) {
        try {
            Engineer updatedEngineer = engineerService.updateEngineer(id, input);
            return ResponseEntity.ok(updatedEngineer);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngineer(@PathVariable Long id) {
        try {
            engineerService.deleteEngineer(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
