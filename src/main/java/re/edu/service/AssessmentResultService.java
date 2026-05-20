package re.edu.service;

import re.edu.dto.request.AssessmentResultRequest;
import re.edu.dto.request.AssessmentResultUpdateRequest;
import re.edu.dto.response.AssessmentResultResponse;

import java.util.List;

public interface AssessmentResultService {
    // 42. GET ALL
    List<AssessmentResultResponse> getAllResults(Integer assignmentId);
    // 43. CREATE
    AssessmentResultResponse createResult(AssessmentResultRequest request);
    // 44. UPDATE
    AssessmentResultResponse updateResult(Integer resultId, AssessmentResultUpdateRequest request);
}