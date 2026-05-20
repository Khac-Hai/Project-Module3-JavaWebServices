package re.edu.service;

import re.edu.dto.request.RoundCriteriaRequest;
import re.edu.dto.response.RoundCriteriaResponse;

import java.util.List;

public interface RoundCriteriaService {
    List<RoundCriteriaResponse> getAllRoundCriteria(Integer roundId);
    RoundCriteriaResponse getRoundCriteriaById(Integer id);
    RoundCriteriaResponse createRoundCriteria(RoundCriteriaRequest request);
    RoundCriteriaResponse updateRoundCriteria(Integer id, RoundCriteriaRequest request);
    void deleteRoundCriteria(Integer id);
}
