package re.edu.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import re.edu.dto.response.MentorResponse;
import re.edu.entity.Mentors;

@Component
@RequiredArgsConstructor
public class MentorMapper {

    private final ModelMapper modelMapper;

    public MentorResponse toResponse(Mentors mentor) {
        MentorResponse response = modelMapper.map(mentor, MentorResponse.class);
        response.setId(mentor.getMentorId());
        if (mentor.getUser() != null) {
            response.setUserId(mentor.getUser().getUserId());
            response.setUsername(mentor.getUser().getUsername());
            response.setFullName(mentor.getUser().getFullName());
            response.setEmail(mentor.getUser().getEmail());
            response.setPhoneNumber(mentor.getUser().getPhoneNumber());
        }
        return response;
    }
}
