package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.StudentRequest;
import re.edu.dto.response.StudentResponse;
import re.edu.entity.Students;
import re.edu.entity.Users;
import re.edu.repository.StudentRepository;
import re.edu.repository.UserRepository;
import re.edu.config.security.CustomUserDetails;
import re.edu.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentResponse> getAllStudents() {

        List<Students> students = studentRepository.findAll();

        return students.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Integer studentId) {

        Students student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        checkViewPermission(student);

        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Students student = new Students();

        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return toResponse(
                studentRepository.save(student)
        );
    }

    @Override
    public StudentResponse updateStudent(Integer studentId,
                                         StudentRequest request) {

        Students student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        checkUpdatePermission(student);

        if (request.getStudentCode() != null) {
            student.setStudentCode(request.getStudentCode());
        }

        if (request.getMajor() != null) {
            student.setMajor(request.getMajor());
        }

        if (request.getClassName() != null) {
            student.setClassName(request.getClassName());
        }

        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getAddress() != null) {
            student.setAddress(request.getAddress());
        }

        return toResponse(
                studentRepository.save(student)
        );
    }

    private StudentResponse toResponse(Students student) {

        StudentResponse response =
                modelMapper.map(student, StudentResponse.class);

        response.setUserId(student.getUser().getId());
        response.setUsername(student.getUser().getUsername());

        return response;
    }

    private void checkViewPermission(Students student) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId()
                    .equals(userDetails.getUser().getId())) {

                throw new RuntimeException("Access denied");
            }
        }
    }

    private void checkUpdatePermission(Students student) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId()
                    .equals(userDetails.getUser().getId())) {

                throw new RuntimeException("Access denied");
            }
        }
    }
}
