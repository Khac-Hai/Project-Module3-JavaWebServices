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
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

    private final AssessmentRoundRepository assessmentRoundRepository;
    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<AssessmentRoundResponse> getAllRounds(Integer phaseId) {
        // Nếu có phaseId thì lọc theo phase, ngược lại lấy tất cả
        List<AssessmentRounds> rounds = (phaseId != null)
                ? assessmentRoundRepository.findByPhase_PhaseId(phaseId)
                : assessmentRoundRepository.findAll();

        return rounds.stream().map(this::toResponse).toList();
    }

    @Override
    public AssessmentRoundResponse getRoundById(Integer roundId) {
        AssessmentRounds round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found"));
        return toResponse(round);
    }

    @Override
    public AssessmentRoundResponse createRound(AssessmentRoundRequest request) {
        validateRequest(request);

        InternshipPhases phase = internshipPhaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found"));

        AssessmentRounds round = new AssessmentRounds();
        round.setPhase(phase);
        round.setRoundName(request.getRoundName());
        round.setStartDate(request.getStartDate());
        round.setEndDate(request.getEndDate());
        round.setDescription(request.getDescription());
        round.setIsActive(request.getIsActive());
        round.setCreatedAt(LocalDateTime.now());
        round.setUpdatedAt(LocalDateTime.now());

        return toResponse(assessmentRoundRepository.save(round));
    }

    @Override
    public AssessmentRoundResponse updateRound(Integer roundId, AssessmentRoundRequest request) {
        AssessmentRounds round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found"));

        // Cập nhật các field nếu có trong request
        if (request.getPhaseId() != null) {
            InternshipPhases phase = internshipPhaseRepository.findById(request.getPhaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found"));
            round.setPhase(phase);
        }
        if (isValid(request.getRoundName())) round.setRoundName(request.getRoundName());
        if (request.getStartDate() != null) round.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) round.setEndDate(request.getEndDate());
        if (request.getDescription() != null) round.setDescription(request.getDescription());
        if (request.getIsActive() != null) round.setIsActive(request.getIsActive());

        round.setUpdatedAt(LocalDateTime.now());

        return toResponse(assessmentRoundRepository.save(round));
    }

    @Override
    public void deleteRound(Integer roundId) {
        AssessmentRounds round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found"));
        assessmentRoundRepository.delete(round);
    }

    // Convert entity -> response DTO
    private AssessmentRoundResponse toResponse(AssessmentRounds round) {
        return AssessmentRoundResponse.builder()
                .id(round.getId())
                .phaseId(round.getPhase().getPhaseId())
                .phaseName(round.getPhase().getPhaseName())
                .roundName(round.getRoundName())
                .startDate(round.getStartDate())
                .endDate(round.getEndDate())
                .description(round.getDescription())
                .isActive(round.getIsActive())
                .build();
    }

    // Validate dữ liệu đầu vào khi tạo round
    private void validateRequest(AssessmentRoundRequest request) {
        if (request.getPhaseId() == null) throw new IllegalArgumentException("Phase id is required");
        if (!isValid(request.getRoundName())) throw new IllegalArgumentException("Round name is required");
        if (request.getStartDate() == null) throw new IllegalArgumentException("Start date is required");
        if (request.getEndDate() == null) throw new IllegalArgumentException("End date is required");
        if (request.getEndDate().isBefore(request.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date");
    }

    // Helper check chuỗi hợp lệ
    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}
