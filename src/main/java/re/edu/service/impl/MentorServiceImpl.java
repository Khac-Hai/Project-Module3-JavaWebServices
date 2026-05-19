package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.mentor.MentorRequest;
import re.edu.dto.response.MentorResponse;
import re.edu.entity.Mentors;
import re.edu.util.enums.Role;
import re.edu.entity.Users;
import re.edu.repository.MentorRepository;
import re.edu.repository.UserRepository;
import re.edu.config.security.CustomUserDetails;
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

        List<Mentors> mentors = mentorRepository.findAll();

        return mentors.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MentorResponse getMentorById(Integer mentorId) {

        Mentors mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mentor not found"));

        checkViewPermission(mentor);

        return toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.MENTOR) {
            throw new RuntimeException("User role must be MENTOR");
        }

        Mentors mentor = new Mentors();

        mentor.setUser(user);
        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());
        mentor.setCreatedAt(LocalDateTime.now());
        mentor.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                mentorRepository.save(mentor)
        );
    }

    @Override
    public MentorResponse updateMentor(Integer mentorId,
                                       MentorRequest request) {

        Mentors mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mentor not found"));

        checkUpdatePermission(mentor);

        if (request.getDepartment() != null) {
            mentor.setDepartment(request.getDepartment());
        }

        if (request.getAcademicRank() != null) {
            mentor.setAcademicRank(request.getAcademicRank());
        }

        mentor.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                mentorRepository.save(mentor)
        );
    }

    private MentorResponse toResponse(Mentors mentor) {

        MentorResponse response = new MentorResponse();

        response.setId(mentor.getMentorId());
        response.setUserId(mentor.getUser().getUserId());
        response.setUsername(mentor.getUser().getUsername());
        response.setFullName(mentor.getUser().getFullName());
        response.setEmail(mentor.getUser().getEmail());
        response.setPhoneNumber(mentor.getUser().getPhoneNumber());
        response.setDepartment(mentor.getDepartment());
        response.setAcademicRank(mentor.getAcademicRank());

        return response;
    }

    private void checkViewPermission(Mentors mentor) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Users currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getUserId().equals(currentUser.getUserId())) {

            throw new AccessDeniedExceptionCustom(
                    "You can only view your own profile"
            );
        }
    }

    private void checkUpdatePermission(Mentors mentor) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Users currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getUserId().equals(currentUser.getUserId())) {

            throw new AccessDeniedExceptionCustom(
                    "You can only update your own profile"
            );
        }
    }
}