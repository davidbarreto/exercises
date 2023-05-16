package io.teamway.workplanning.mapper;

import io.teamway.workplanning.dto.WorkerDTO;
import io.teamway.workplanning.model.Worker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Worker toEntity(WorkerDTO dto) {
        return modelMapper.map(dto, Worker.class);
    }

    public WorkerDTO toDTO(Worker entity) {
        return modelMapper.map(entity, WorkerDTO.class);
    }
}
