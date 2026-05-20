package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.RoundCriteriaRequest;
import re.edu.dto.response.RoundCriteriaResponse;
import re.edu.entity.AssessmentRounds;
import re.edu.entity.EvaluationCriteria;
import re.edu.entity.RoundCriteria;
import re.edu.repository.AssessmentRoundRepository;
import re.edu.repository.EvaluationCriteriaRepository;
import re.edu.repository.RoundCriteriaRepository;
import re.edu.service.RoundCriteriaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {

    private final RoundCriteriaRepository roundCriteriaRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;
    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<RoundCriteriaResponse> getAllRoundCriteria(Integer roundId) {
        // Lấy tất cả tiêu chí theo roundId (nếu có), ngược lại lấy tất cả
        List<RoundCriteria> list = (roundId != null)
                ? roundCriteriaRepository.findByRound_Id(roundId)
                : roundCriteriaRepository.findAll();

        return list.stream().map(this::toResponse).toList();
    }

    @Override
    public RoundCriteriaResponse getRoundCriteriaById(Integer id) {
        // Tìm tiêu chí theo ID
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí vòng"));
        return toResponse(roundCriteria);
    }

    @Override
    public RoundCriteriaResponse createRoundCriteria(RoundCriteriaRequest request) {
        validateRequest(request);

        // Kiểm tra tiêu chí đã tồn tại trong round chưa
        boolean exists = roundCriteriaRepository.findByRound_IdAndCriterion_CriterionId(
                request.getRoundId(), request.getCriterionId()
        ).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Tiêu chí đã tồn tại trong vòng này");
        }

        AssessmentRounds round = assessmentRoundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vòng đánh giá"));

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(request.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        // Tạo mới RoundCriteria
        RoundCriteria roundCriteria = new RoundCriteria();
        roundCriteria.setRound(round);
        roundCriteria.setCriterion(criterion);
        roundCriteria.setWeight(request.getWeight());
        roundCriteria.setCreatedAt(LocalDateTime.now());
        roundCriteria.setUpdatedAt(LocalDateTime.now());

        return toResponse(roundCriteriaRepository.save(roundCriteria));
    }

    @Override
    public RoundCriteriaResponse updateRoundCriteria(Integer id, RoundCriteriaRequest request) {
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí vòng"));

        if (request.getWeight() != null) {
            if (request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Trọng số phải lớn hơn 0");
            }
            roundCriteria.setWeight(request.getWeight());
        }

        roundCriteria.setUpdatedAt(LocalDateTime.now());
        return toResponse(roundCriteriaRepository.save(roundCriteria));
    }

    @Override
    public void deleteRoundCriteria(Integer id) {
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí vòng"));
        roundCriteriaRepository.delete(roundCriteria);
    }

    // Convert entity -> response DTO (dùng builder cho gọn gàng)
    private RoundCriteriaResponse toResponse(RoundCriteria rc) {
        return RoundCriteriaResponse.builder()
                .roundCriterionId(rc.getRoundCriterionId())
                .roundId(rc.getRound().getId())
                .roundName(rc.getRound().getRoundName())
                .criterionId(rc.getCriterion().getCriterionId())
                .criterionName(rc.getCriterion().getCriterionName())
                .weight(rc.getWeight())
                .build();
    }

    // Validate dữ liệu đầu vào khi tạo mới
    private void validateRequest(RoundCriteriaRequest request) {
        if (request.getRoundId() == null) {
            throw new IllegalArgumentException("Mã vòng là bắt buộc");
        }
        if (request.getCriterionId() == null) {
            throw new IllegalArgumentException("Mã tiêu chí là bắt buộc");
        }
        if (request.getWeight() == null) {
            throw new IllegalArgumentException("Trọng số là bắt buộc");
        }
        if (request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Trọng số phải lớn hơn 0");
        }
    }
}
