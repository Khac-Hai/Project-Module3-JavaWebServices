package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.AssessmentResultRequest;
import re.edu.dto.request.AssessmentResultUpdateRequest;
import re.edu.dto.response.AssessmentResultResponse;
import re.edu.entity.*;
import re.edu.repository.*;
import re.edu.config.custom.CustomUserDetails;
import re.edu.service.AssessmentResultService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl implements AssessmentResultService {

    private final AssessmentResultRepository resultRepository;
    private final InternshipAssignmentRepository assignmentRepository;
    private final AssessmentRoundRepository roundRepository;
    private final EvaluationCriteriaRepository criteriaRepository;

    @Override
    public List<AssessmentResultResponse> getAllResults(Integer assignmentId) {
        // Nếu có assignmentId thì lọc theo assignment, ngược lại lấy tất cả
        List<AssessmentResults> results = (assignmentId != null)
                ? resultRepository.findByAssignment_AssignmentId(assignmentId)
                : resultRepository.findAll();

        return results.stream().map(this::toResponse).toList();
    }

    @Override
    public AssessmentResultResponse createResult(AssessmentResultRequest request) {
        // Lấy dữ liệu liên quan, nếu không có thì throw exception
        InternshipAssignments assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        AssessmentRounds round = roundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Round not found"));

        EvaluationCriteria criterion = criteriaRepository.findById(request.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Criterion not found"));

        Users currentUser = getCurrentUser();

        // Chỉ mentor được phân công mới được đánh giá
        if (!assignment.getMentor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Bạn không được phân công sinh viên này");
        }

        // Kiểm tra trùng lặp kết quả
        boolean exists = resultRepository
                .findByAssignment_AssignmentIdAndRound_IdAndCriterion_CriterionId(
                        request.getAssignmentId(), request.getRoundId(), request.getCriterionId()
                ).isPresent();

        if (exists) {
            throw new IllegalArgumentException("Assessment result already exists");
        }

        // Tạo mới kết quả đánh giá
        AssessmentResults result = new AssessmentResults();
        result.setAssignment(assignment);
        result.setRound(round);
        result.setCriterion(criterion);
        result.setScore(request.getScore());
        result.setComments(request.getComments());
        result.setEvaluatedBy(currentUser);
        result.setEvaluationDate(LocalDateTime.now());

        return toResponse(resultRepository.save(result));
    }

    @Override
    public AssessmentResultResponse updateResult(Integer resultId, AssessmentResultUpdateRequest request) {
        AssessmentResults result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment result not found"));

        Users currentUser = getCurrentUser();

        // Chỉ được sửa kết quả do chính mình đánh giá
        if (!result.getEvaluatedBy().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Bạn không thể sửa kết quả của mentor khác");
        }

        result.setScore(request.getScore());
        result.setComments(request.getComments());

        return toResponse(resultRepository.save(result));
    }

    // Lấy user hiện tại từ SecurityContext
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getUser();
    }

    // Convert entity -> response DTO
    private AssessmentResultResponse toResponse(AssessmentResults result) {
        return AssessmentResultResponse.builder()
                .resultId(result.getResultId())
                .assignmentId(result.getAssignment().getAssignmentId())
                .studentId(result.getAssignment().getStudent().getId())
                .studentName(result.getAssignment().getStudent().getUser().getFullName())
                .mentorId(result.getAssignment().getMentor().getId())
                .mentorName(result.getAssignment().getMentor().getUser().getFullName())
                .roundId(result.getRound().getId())
                .roundName(result.getRound().getRoundName())
                .criterionId(result.getCriterion().getCriterionId())
                .criterionName(result.getCriterion().getCriterionName())
                .score(result.getScore())
                .comments(result.getComments())
                .evaluatedById(result.getEvaluatedBy().getId())
                .evaluatedByName(result.getEvaluatedBy().getFullName())
                .evaluationDate(result.getEvaluationDate())
                .build();
    }
}
