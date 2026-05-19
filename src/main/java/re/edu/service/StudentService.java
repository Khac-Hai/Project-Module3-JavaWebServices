package re.edu.service;

import re.edu.dto.request.StudentRequest;
import re.edu.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Integer studentId);

    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Integer studentId, StudentRequest request);
}