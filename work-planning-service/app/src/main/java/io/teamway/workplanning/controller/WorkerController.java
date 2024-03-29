package io.teamway.workplanning.controller;

import io.teamway.workplanning.dto.WorkerDTO;
import io.teamway.workplanning.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public List<WorkerDTO> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/{id}")
    public WorkerDTO getWorker(@PathVariable Long id) {
        return workerService.getWorker(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkerDTO createWorker(@RequestBody WorkerDTO worker) {
        return workerService.createWorker(worker);
    }

    @PutMapping("/{id}")
    public WorkerDTO updateWorker(@PathVariable Long id, @RequestBody WorkerDTO workerDetails) {
        return workerService.updateWorker(id, workerDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
    }
}
