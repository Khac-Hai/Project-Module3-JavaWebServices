package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.EvaluationCriteriaRequest;
import re.edu.dto.response.EvaluationCriteriaResponse;
import re.edu.entity.EvaluationCriteria;
import re.edu.repository.EvaluationCriteriaRepository;
import re.edu.service.EvaluationCriteriaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<EvaluationCriteriaResponse> getAllCriteria() {
        // Lấy tất cả tiêu chí đánh giá
        return evaluationCriteriaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EvaluationCriteriaResponse getCriterionById(Integer criterionId) {
        // Tìm tiêu chí theo ID
        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));
        return toResponse(criterion);
    }

    @Override
    public EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request) {
        validateCreateRequest(request);

        // Kiểm tra trùng tên
        if (evaluationCriteriaRepository.existsByCriterionName(request.getCriterionName())) {
            throw new DuplicateResourceException("Tên tiêu chí đã tồn tại");
        }

        // Tạo mới tiêu chí
        EvaluationCriteria criterion = new EvaluationCriteria();
        criterion.setCriterionName(request.getCriterionName());
        criterion.setDescription(request.getDescription());
        criterion.setMaxScore(request.getMaxScore());
        criterion.setCreatedAt(LocalDateTime.now());
        criterion.setUpdatedAt(LocalDateTime.now());

        return toResponse(evaluationCriteriaRepository.save(criterion));
    }

    @Override
    public EvaluationCriteriaResponse updateCriterion(Integer criterionId, EvaluationCriteriaRequest request) {
        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        // Cập nhật các field nếu có trong request
        if (isValid(request.getCriterionName())) criterion.setCriterionName(request.getCriterionName());
        if (request.getDescription() != null) criterion.setDescription(request.getDescription());
        if (request.getMaxScore() != null) {
            if (request.getMaxScore().doubleValue() <= 0) {
                throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0");
            }
            criterion.setMaxScore(request.getMaxScore());
        }

        criterion.setUpdatedAt(LocalDateTime.now());
        return toResponse(evaluationCriteriaRepository.save(criterion));
    }

    @Override
    public void deleteCriterion(Integer criterionId) {
        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));
        evaluationCriteriaRepository.delete(criterion);
    }

    // Convert entity -> response DTO (dùng builder cho gọn gàng)
    private EvaluationCriteriaResponse toResponse(EvaluationCriteria criterion) {
        return EvaluationCriteriaResponse.builder()
                .id(criterion.getCriterionId())
                .criterionName(criterion.getCriterionName())
                .description(criterion.getDescription())
                .maxScore(criterion.getMaxScore())
                .build();
    }

    // Validate dữ liệu khi tạo mới
    private void validateCreateRequest(EvaluationCriteriaRequest request) {
        if (!isValid(request.getCriterionName())) {
            throw new IllegalArgumentException("Tên tiêu chí là bắt buộc");
        }
        if (request.getMaxScore() == null) {
            throw new IllegalArgumentException("Điểm tối đa là bắt buộc");
        }
        if (request.getMaxScore().doubleValue() <= 0) {
            throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0");
        }
    }

    // Helper check chuỗi hợp lệ
    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}
