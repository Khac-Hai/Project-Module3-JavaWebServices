package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.InternshipPhaseRequest;
import re.edu.dto.response.InternshipPhaseResponse;
import re.edu.entity.InternshipPhases;
import re.edu.repository.InternshipPhaseRepository;
import re.edu.service.InternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<InternshipPhaseResponse> getAllPhases() {
        // Lấy tất cả giai đoạn thực tập
        return internshipPhaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Integer phaseId) {
        // Tìm giai đoạn theo ID
        InternshipPhases phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));
        return toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(InternshipPhaseRequest request) {
        validateDate(request);

        InternshipPhases phase = new InternshipPhases();
        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setCreatedAt(LocalDateTime.now());
        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(internshipPhaseRepository.save(phase));
    }

    @Override
    public InternshipPhaseResponse updatePhase(Integer phaseId, InternshipPhaseRequest request) {
        validateDate(request);

        InternshipPhases phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(internshipPhaseRepository.save(phase));
    }

    @Override
    public void deletePhase(Integer phaseId) {
        InternshipPhases phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));
        internshipPhaseRepository.delete(phase);
    }

    // Convert entity -> response DTO (dùng builder cho gọn gàng)
    private InternshipPhaseResponse toResponse(InternshipPhases phase) {
        return InternshipPhaseResponse.builder()
                .id(phase.getPhaseId())
                .phaseName(phase.getPhaseName())
                .description(phase.getDescription())
                .startDate(phase.getStartDate())
                .endDate(phase.getEndDate())
                .build();
    }

    // Validate ngày bắt đầu và kết thúc
    private void validateDate(InternshipPhaseRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
    }
}
