package re.edu.service;

import re.edu.dto.request.MentorRequest;
import re.edu.dto.response.MentorResponse;
import java.util.List;

public interface MentorService {
    List<MentorResponse> getAllMentors();
    MentorResponse getMentorById(Integer mentorId);
    MentorResponse createMentor(MentorRequest request);
    MentorResponse updateMentor(Integer mentorId, MentorRequest request);
}
