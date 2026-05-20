package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.entity.AssessmentRounds;
import re.edu.entity.InternshipPhases;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.AssessmentRoundRequest;
import re.edu.dto.response.AssessmentRoundResponse;
import re.edu.repository.AssessmentRoundRepository;
import re.edu.repository.InternshipPhaseRepository;
import re.edu.service.AssessmentRoundService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl
        implements AssessmentRoundService {

    private final AssessmentRoundRepository
            assessmentRoundRepository;

    private final InternshipPhaseRepository
            internshipPhaseRepository;

    @Override
    public List<AssessmentRoundResponse>
    getAllRounds(Integer phaseId) {

        List<AssessmentRounds> rounds;

        if (phaseId != null) {

            rounds = assessmentRoundRepository
                    .findByPhase_Id(phaseId);

        } else {

            rounds = assessmentRoundRepository.findAll();
        }

        return rounds.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AssessmentRoundResponse
    getRoundById(Integer roundId) {

        AssessmentRounds round =
                assessmentRoundRepository
                        .findById(roundId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assessment round not found"
                                )
                        );

        return toResponse(round);
    }

    @Override
    public AssessmentRoundResponse
    createRound(
            AssessmentRoundRequest request
    ) {

        validateRequest(request);

        InternshipPhases phase =
                internshipPhaseRepository
                        .findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Internship phase not found"
                                )
                        );

        AssessmentRounds round =
                new AssessmentRounds();

        round.setPhase(phase);

        round.setRoundName(
                request.getRoundName()
        );

        round.setStartDate(
                request.getStartDate()
        );

        round.setEndDate(
                request.getEndDate()
        );

        round.setDescription(
                request.getDescription()
        );

        round.setIsActive(
                request.getIsActive()
        );

        round.setCreatedAt(LocalDateTime.now());

        round.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                assessmentRoundRepository
                        .save(round)
        );
    }

    @Override
    public AssessmentRoundResponse
    updateRound(
            Integer roundId,
            AssessmentRoundRequest request
    ) {

        AssessmentRounds round =
                assessmentRoundRepository
                        .findById(roundId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assessment round not found"
                                )
                        );

        if (request.getPhaseId() != null) {

            InternshipPhases phase =
                    internshipPhaseRepository
                            .findById(
                                    request.getPhaseId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Internship phase not found"
                                    )
                            );

            round.setPhase(phase);
        }

        if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            round.setRoundName(
                    request.getRoundName()
            );
        }

        if (request.getStartDate() != null) {

            round.setStartDate(
                    request.getStartDate()
            );
        }

        if (request.getEndDate() != null) {

            round.setEndDate(
                    request.getEndDate()
            );
        }

        if (request.getDescription() != null) {

            round.setDescription(
                    request.getDescription()
            );
        }

        if (request.getIsActive() != null) {

            round.setIsActive(
                    request.getIsActive()
            );
        }

        round.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                assessmentRoundRepository
                        .save(round)
        );
    }

    @Override
    public void deleteRound(Integer roundId) {

        AssessmentRounds round =
                assessmentRoundRepository
                        .findById(roundId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assessment round not found"
                                )
                        );

        assessmentRoundRepository.delete(round);
    }

    private AssessmentRoundResponse
    toResponse(AssessmentRounds round) {

        AssessmentRoundResponse response =
                new AssessmentRoundResponse();

        response.setId(round.getId());

        response.setPhaseId(
                round.getPhase().getId()
        );

        response.setPhaseName(
                round.getPhase().getName()
        );

        response.setRoundName(
                round.getRoundName()
        );

        response.setStartDate(
                round.getStartDate()
        );

        response.setEndDate(
                round.getEndDate()
        );

        response.setDescription(
                round.getDescription()
        );

        response.setIsActive(
                round.getIsActive()
        );

        return response;
    }

    private void validateRequest(
            AssessmentRoundRequest request
    ) {

        if (request.getPhaseId() == null) {

            throw new IllegalArgumentException(
                    "Phase id is required"
            );
        }

        if (request.getRoundName() == null
                || request.getRoundName().isBlank()) {

            throw new IllegalArgumentException(
                    "Round name is required"
            );
        }

        if (request.getStartDate() == null) {

            throw new IllegalArgumentException(
                    "Start date is required"
            );
        }

        if (request.getEndDate() == null) {

            throw new IllegalArgumentException(
                    "End date is required"
            );
        }

        if (request.getEndDate()
                .isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date must be after start date"
            );
        }
    }
}
