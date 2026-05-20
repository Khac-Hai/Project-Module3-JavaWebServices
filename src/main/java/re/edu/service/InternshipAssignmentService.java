package re.edu.service;

import re.edu.dto.request.InternshipAssignmentRequest;
import re.edu.dto.request.UpdateAssignmentStatusRequest;
import re.edu.dto.response.InternshipAssignmentResponse;

import java.util.List;

public interface InternshipAssignmentService {
    List<InternshipAssignmentResponse> getAllAssignments();
    InternshipAssignmentResponse getAssignmentById(Integer assignmentId);
    InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request);
    InternshipAssignmentResponse updateAssignmentStatus(Integer assignmentId, UpdateAssignmentStatusRequest request);
}
