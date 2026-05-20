package re.edu.service;

import re.edu.dto.request.EvaluationCriteriaRequest;
import re.edu.dto.response.EvaluationCriteriaResponse;

import java.util.List;

public interface EvaluationCriteriaService {
    List<EvaluationCriteriaResponse> getAllCriteria();
    EvaluationCriteriaResponse getCriterionById(Integer criterionId);
    EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request);
    EvaluationCriteriaResponse updateCriterion(Integer criterionId, EvaluationCriteriaRequest request);
    void deleteCriterion(Integer criterionId);
}
