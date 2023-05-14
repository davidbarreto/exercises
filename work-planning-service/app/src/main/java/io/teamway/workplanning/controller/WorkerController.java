package io.teamway.workplanning.controller;

import io.teamway.workplanning.model.Worker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @GetMapping("")
    public List<Worker> getAllWorkers() {
        return Collections.emptyList();
    }
}
