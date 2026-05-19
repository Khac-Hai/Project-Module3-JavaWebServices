package re.edu.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import re.edu.dto.request.internship.InternshipPhaseRequest;
import re.edu.dto.response.InternshipPhaseResponse;
import re.edu.entity.InternshipPhases;

@Component
@RequiredArgsConstructor
public class InternshipPhaseMapper {

    private final ModelMapper modelMapper;

    public InternshipPhaseResponse toResponse(InternshipPhases phase) {
        return modelMapper.map(phase, InternshipPhaseResponse.class);
    }

    public InternshipPhases toEntity(InternshipPhaseRequest request) {
        return modelMapper.map(request, InternshipPhases.class);
    }

    public void updateEntity(InternshipPhaseRequest request, InternshipPhases phase) {
        modelMapper.map(request, phase);
    }
}
