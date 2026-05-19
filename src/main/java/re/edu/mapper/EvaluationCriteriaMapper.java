package re.edu.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import re.edu.dto.request.evaluation.EvaluationCriteriaRequest;
import re.edu.dto.response.EvaluationCriteriaResponse;
import re.edu.entity.EvaluationCriteria;

@Component
@RequiredArgsConstructor
public class EvaluationCriteriaMapper {

    private final ModelMapper modelMapper;

    public EvaluationCriteriaResponse toResponse(EvaluationCriteria criteria) {
        return modelMapper.map(criteria, EvaluationCriteriaResponse.class);
    }

    public EvaluationCriteria toEntity(EvaluationCriteriaRequest request) {
        return modelMapper.map(request, EvaluationCriteria.class);
    }

    public void updateEntity(EvaluationCriteriaRequest request, EvaluationCriteria criteria) {
        modelMapper.map(request, criteria);
    }
}