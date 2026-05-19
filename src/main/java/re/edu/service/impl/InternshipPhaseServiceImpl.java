package re.edu.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.internship.InternshipPhaseRequest;
import re.edu.dto.response.InternshipPhaseResponse;
import re.edu.entity.InternshipPhases;
import re.edu.repository.InternshipPhaseRepository;
import re.edu.service.InternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl
        implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<InternshipPhaseResponse> getAllPhases() {

        return internshipPhaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Integer phaseId) {

        InternshipPhases phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        return toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(
            InternshipPhaseRequest request
    ) {

        validateDate(request);

        InternshipPhases phase = new InternshipPhases();

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());

        phase.setCreatedAt(LocalDateTime.now());
        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                internshipPhaseRepository.save(phase)
        );
    }

    @Override
    public InternshipPhaseResponse updatePhase(
            Integer phaseId,
            InternshipPhaseRequest request
    ) {

        validateDate(request);

        InternshipPhases phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());

        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                internshipPhaseRepository.save(phase)
        );
    }

    @Override
    public void deletePhase(Integer phaseId) {

        InternshipPhases phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        internshipPhaseRepository.delete(phase);
    }

    private InternshipPhaseResponse toResponse(
            InternshipPhases phase
    ) {

        InternshipPhaseResponse response =
                new InternshipPhaseResponse();

        response.setId(phase.getPhaseId());
        response.setName(phase.getPhaseName());
        response.setDescription(phase.getDescription());
        response.setStartDate(phase.getStartDate());
        response.setEndDate(phase.getEndDate());

        return response;
    }

    private void validateDate(
            InternshipPhaseRequest request
    ) {

        if (request.getEndDate()
                .isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date must be after start date"
            );
        }
    }
}