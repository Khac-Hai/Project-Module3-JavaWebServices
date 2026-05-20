package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.MentorRequest;
import re.edu.dto.response.MentorResponse;
import re.edu.entity.Mentors;
import re.edu.util.enums.Role;
import re.edu.entity.Users;
import re.edu.repository.MentorRepository;
import re.edu.repository.UserRepository;
import re.edu.config.custom.CustomUserDetails;
import re.edu.service.MentorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;

    @Override
    public List<MentorResponse> getAllMentors() {
        // Lấy tất cả mentor
        return mentorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MentorResponse getMentorById(Integer mentorId) {
        // Tìm mentor theo ID
        Mentors mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mentor"));

        checkViewPermission(mentor);
        return toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (user.getRole() != Role.MENTOR) {
            throw new IllegalArgumentException("Vai trò người dùng phải là MENTOR");
        }

        Mentors mentor = new Mentors();
        mentor.setUser(user);
        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());
        mentor.setCreatedAt(LocalDateTime.now());
        mentor.setUpdatedAt(LocalDateTime.now());

        return toResponse(mentorRepository.save(mentor));
    }

    @Override
    public MentorResponse updateMentor(Integer mentorId, MentorRequest request) {
        Mentors mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mentor"));

        checkUpdatePermission(mentor);

        if (request.getDepartment() != null) mentor.setDepartment(request.getDepartment());
        if (request.getAcademicRank() != null) mentor.setAcademicRank(request.getAcademicRank());

        mentor.setUpdatedAt(LocalDateTime.now());
        return toResponse(mentorRepository.save(mentor));
    }

    // Convert entity -> response DTO (dùng builder cho gọn gàng)
    private MentorResponse toResponse(Mentors mentor) {
        return MentorResponse.builder()
                .id(mentor.getId())
                .userId(mentor.getUser().getId())
                .username(mentor.getUser().getUsername())
                .fullName(mentor.getUser().getFullName())
                .email(mentor.getUser().getEmail())
                .phoneNumber(mentor.getUser().getPhoneNumber())
                .department(mentor.getDepartment())
                .academicRank(mentor.getAcademicRank())
                .build();
    }

    // Kiểm tra quyền xem hồ sơ
    private void checkViewPermission(Mentors mentor) {
        Users currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedExceptionCustom("Bạn chỉ có thể xem hồ sơ của chính mình");
        }
    }

    // Kiểm tra quyền cập nhật hồ sơ
    private void checkUpdatePermission(Mentors mentor) {
        Users currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedExceptionCustom("Bạn chỉ có thể cập nhật hồ sơ của chính mình");
        }
    }

    // Lấy user hiện tại từ SecurityContext
    private Users getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
}
