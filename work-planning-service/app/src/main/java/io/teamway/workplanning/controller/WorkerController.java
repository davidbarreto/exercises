package io.teamway.workplanning.controller;

import io.teamway.workplanning.model.Worker;
import io.teamway.workplanning.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping("")
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }
}
