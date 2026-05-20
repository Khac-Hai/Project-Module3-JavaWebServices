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
import re.edu.config.custom.CustomUserDetails;
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
        // Lấy tất cả sinh viên
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Integer studentId) {
        // Tìm sinh viên theo ID
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        checkViewPermission(student);
        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        // Lấy user liên kết
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Tạo mới sinh viên
        Students student = new Students();
        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(Integer studentId, StudentRequest request) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        checkUpdatePermission(student);

        // Cập nhật các field nếu có trong request
        if (request.getStudentCode() != null) student.setStudentCode(request.getStudentCode());
        if (request.getMajor() != null) student.setMajor(request.getMajor());
        if (request.getClassName() != null) student.setClassName(request.getClassName());
        if (request.getDateOfBirth() != null) student.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) student.setAddress(request.getAddress());

        return toResponse(studentRepository.save(student));
    }

    // Convert entity -> response DTO (dùng ModelMapper + bổ sung field từ User)
    private StudentResponse toResponse(Students student) {
        StudentResponse response = modelMapper.map(student, StudentResponse.class);
        response.setUserId(student.getUser().getId());
        response.setUsername(student.getUser().getUsername());
        return response;
    }

    // Kiểm tra quyền xem hồ sơ
    private void checkViewPermission(Students student) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_STUDENT".equals(role) &&
                !student.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new RuntimeException("Bạn chỉ có thể xem hồ sơ của chính mình");
        }
    }

    // Kiểm tra quyền cập nhật hồ sơ
    private void checkUpdatePermission(Students student) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_STUDENT".equals(role) &&
                !student.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new RuntimeException("Bạn chỉ có thể cập nhật hồ sơ của chính mình");
        }
    }
}
