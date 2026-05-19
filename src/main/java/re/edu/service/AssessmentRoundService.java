package re.edu.service;

import re.edu.dto.request.AssessmentRoundRequest;
import re.edu.dto.response.AssessmentRoundResponse;

import java.util.List;

public interface AssessmentRoundService {

    List<AssessmentRoundResponse>
    getAllRounds(Integer phaseId);

    AssessmentRoundResponse
    getRoundById(Integer roundId);

    AssessmentRoundResponse
    createRound(AssessmentRoundRequest request);

    AssessmentRoundResponse
    updateRound(
            Integer roundId,
            AssessmentRoundRequest request
    );

    void deleteRound(Integer roundId);
}
