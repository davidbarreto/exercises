package io.teamway.workplanning.service;

import io.teamway.workplanning.exception.ElementNotFoundException;
import io.teamway.workplanning.model.Worker;
import io.teamway.workplanning.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    private static final String RESOURCE_NAME = "Worker";

    @Autowired
    private WorkerRepository workerRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorker(Long id) {
        return workerRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(RESOURCE_NAME, id));
    }

    public Worker createWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker updateWorker(Long id, Worker workerDetails) {
        Worker worker = getWorker(id);
        worker.setName(workerDetails.getName());
        return workerRepository.save(worker);
    }

    public void deleteWorker(Long id) {
        getWorker(id);
        workerRepository.deleteById(id);
    }
}
