package io.teamway.workplanning.service;

import io.teamway.workplanning.model.Worker;
import io.teamway.workplanning.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository repository;

    public List<Worker> getAllWorkers() {
        return repository.findAll();
    }
}
