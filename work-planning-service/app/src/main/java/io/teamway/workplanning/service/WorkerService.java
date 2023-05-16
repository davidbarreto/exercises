package io.teamway.workplanning.service;

import io.teamway.workplanning.dto.WorkerDTO;
import io.teamway.workplanning.exception.ElementNotFoundException;
import io.teamway.workplanning.mapper.WorkerMapper;
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

    @Autowired
    private WorkerMapper workerMapper;

    public List<WorkerDTO> getAllWorkers() {
        return workerRepository.findAll().stream()
                       .map(workerMapper::toDTO)
                       .toList();
    }

    public WorkerDTO getWorker(Long id) {
        return workerRepository.findById(id)
                       .map(workerMapper::toDTO)
                       .orElseThrow(() -> new ElementNotFoundException(RESOURCE_NAME, id));
    }

    public WorkerDTO createWorker(WorkerDTO worker) {
        return workerMapper.toDTO(workerRepository.save(workerMapper.toEntity(worker)));
    }

    public WorkerDTO updateWorker(Long id, WorkerDTO workerDetails) {
        Worker worker = findWorker(id);
        worker.setName(workerDetails.getName());
        return workerMapper.toDTO(workerRepository.save(worker));
    }

    public void deleteWorker(Long id) {
        getWorker(id);
        workerRepository.deleteById(id);
    }

    private Worker findWorker(Long id) {
        return workerRepository.findById(id)
                       .orElseThrow(() -> new ElementNotFoundException(RESOURCE_NAME, id));
    }
}
