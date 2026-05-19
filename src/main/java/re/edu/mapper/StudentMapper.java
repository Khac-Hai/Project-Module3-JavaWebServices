package re.edu.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import re.edu.dto.response.StudentResponse;
import re.edu.entity.Students;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final ModelMapper modelMapper;

    public StudentResponse toResponse(Students student) {
        StudentResponse response = modelMapper.map(student, StudentResponse.class);
        response.setId(student.getStudentId());
        if (student.getUser() != null) {
            response.setUserId(student.getUser().getUserId());
            response.setUsername(student.getUser().getUsername());
            response.setFullName(student.getUser().getFullName());
            response.setEmail(student.getUser().getEmail());
            response.setPhoneNumber(student.getUser().getPhoneNumber());
        }
        return response;
    }
}
