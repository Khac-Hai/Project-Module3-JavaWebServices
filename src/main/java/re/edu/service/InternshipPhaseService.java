package re.edu.service;

import re.edu.dto.request.internship.InternshipPhaseRequest;
import re.edu.dto.response.InternshipPhaseResponse;

import java.util.List;

public interface InternshipPhaseService {
    List<InternshipPhaseResponse> getAllPhases();
    InternshipPhaseResponse getPhaseById(Integer phaseId);
    InternshipPhaseResponse createPhase(
            InternshipPhaseRequest request
    );
    InternshipPhaseResponse updatePhase(
            Integer phaseId,
            InternshipPhaseRequest request
    );
    void deletePhase(Integer phaseId);
}
